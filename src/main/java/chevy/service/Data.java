package chevy.service;

import chevy.utils.Log;
import com.jayway.jsonpath.JsonPath;
import net.harawata.appdirs.AppDirsFactory;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.file.Files;

/**
 * Dati dell'app in formato
 * <a href="https://ecma-international.org/publications-and-standards/standards/ecma-404/">JSON</a>
 * persistenti attraverso esecuzioni
 */
public final class Data {
    /** Posizione del file determinata alla creazione dell'app */
    private static final File file = findFile();
    /** La radice della struttura, è un <code>JSON Object</code> */
    private static String root;

    /**
     * @param path per il dato desiderato
     * @param <T>  può essere qualsiasi cosa
     * @return il valore contenuto a quel percorso
     */
    public synchronized static <T> T get(String path) {
        if (root == null) {
            read();
        }
        return JsonPath.read(root, "$." + path);
    }

    /**
     * @param path  per il dato che si vuole modificare
     * @param value il valore da impostare
     */
    public synchronized static void set(String path, Object value) {
        if (root == null) {
            read();
        }
        root = JsonPath.parse(root).set("$." + path, value).jsonString();
    }

    /**
     * Incrementa un valore
     *
     * @param path  percorso al quale si trova il valore da incrementare
     * @param value quantità da aggiungere
     */
    public synchronized static void increase(String path, Integer value) {
        Integer oldValue = get(path);
        set(path, oldValue + value);
    }

    /**
     * Incrementa un valore di 1
     *
     * @param path percorso al quale si trova il valore da incrementare
     */
    public synchronized static void increment(String path) {increase(path, 1);}

    /**
     * Assicura che il file JSON esista e sia utilizzabile
     */
    private static void checkFile() {
        if (file.exists()) {
            final String path = file.getAbsolutePath();
            if (file.isDirectory()) {
                throw new RuntimeException(path + " is a directory, move it or remove it.");
            }
            if (!file.canRead()) {
                throw new RuntimeException(path + " is not readable, check its permissions.");
            }
            if (!file.canWrite()) {
                throw new RuntimeException(path + " is not writeable, check its permissions.");
            }
        } else {
            createPristineFile();
        }
    }

    /**
     * Crea il file JSON con i dati predefiniti
     */
    public static void createPristineFile() {
        try {
            if (file.getParentFile().mkdirs()) {
                Log.info("Directory create");
            }
            try (InputStream in = Data.class.getResourceAsStream("/defaultChevyData.json")) {
                try (BufferedOutputStream out =
                             new BufferedOutputStream(new FileOutputStream(file))) {
                    assert in != null;
                    out.write(in.readAllBytes());
                }
            }
            root = null;
            Log.info(file + " è stato creato");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Il file JSON è salvato nella cartella dei dati dell'utente. Per esempio, su un sistema
     * POSIX aderente a
     * <a href="https://specifications.freedesktop.org/basedir-spec/0.6/">XDG Base Directory Specification</a>
     * la posizione sarà <code>$XDG_DATA_HOME/chevy/data.json</code>. Mentre su MS Windows la
     * posizione sarà
     * <code>%LocalAppData%\chevy\data.json</code>.
     */
    private static File findFile() {
        String prefix = AppDirsFactory.getInstance().getUserDataDir("chevy", null, null);
        String path = prefix + File.separator + "data.json";
        System.out.println("App data location: " + path);
        return new File(path);
    }

    /**
     * Carica il contenuto del file JSON in {@link #root}
     */
    public static void read() {
        checkFile();
        try {
            root = Files.readString(file.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Salva {@link #root} nel file JSON e lo invalida, cosicché al prossimo utilizzo andrà
     * ricaricato
     */
    public static void write() {
        if (root != null) {
            try (Writer writer = new FileWriter(file)) {
                writer.write(root);
                root = null;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}