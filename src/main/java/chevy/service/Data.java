package chevy.service;

import chevy.utils.Log;
import com.jayway.jsonpath.JsonPath;
import net.harawata.appdirs.AppDirsFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

/**
 * Dati dell'app in formato
 * <a href="https://ecma-international.org/publications-and-standards/standards/ecma-404/">JSON</a>
 * persistenti attraverso esecuzioni.
 */
public class Data {
    private static final File file = findFile();
    private static String root; // È un oggetto JSON: {...}

    public static <T> T get(String path) {
        if (root == null) {
            read();
        }
        return JsonPath.read(root, path);
    }

    public static void set(String path, Object value) {
        if (root == null) {
            read();
        }
        root = JsonPath.parse(root).set(path, value).jsonString();
    }

    /**
     * Assicura che il file JSON esista e sia utilizzabile.
     */
    private static void checkFile() {
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new RuntimeException(file.getAbsolutePath() + " is a directory, move it or remove it.");
            }
            if (!file.canRead()) {
                throw new RuntimeException(file.getAbsolutePath() + " is not readable, check its permissions.");
            }
            if (!file.canWrite()) {
                throw new RuntimeException(file.getAbsolutePath() + " is not writeable, check its permissions.");
            }
        } else {
            createPristineFile();
        }
    }

    /**
     * Crea il file JSON con i dati predefiniti.
     */
    public static void createPristineFile() {
        try {
            Path to = Paths.get(file.getPath());
            Path parent = to.getParent();
            if (parent != null) {
                if (Files.notExists(parent)) {
                    Files.createDirectories(parent);
                }
            }
            URL url = Data.class.getResource("/defaultChevyData.json");
            // .toURI() risolve il problema per cui il path comincia con / su window, es: /C:/Users/...
            Path from = Paths.get(Objects.requireNonNull(url).toURI());
            Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);
        } catch (NullPointerException | IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Il file JSON è salvato nella cartella dei dati dell'utente. Per esempio, su un sistema POSIX aderente a
     * <a href="https://specifications.freedesktop.org/basedir-spec/0.6/">XDG Base Directory Specification</a>
     * la posizione sara <code>$XDG_DATA_HOME/chevy/data.json</code>. Mentre su MS Windows la posizione sarà
     * <code>%appdata%\chevy\data.json</code>.
     */
    private static File findFile() {
        String prefix = AppDirsFactory.getInstance().getUserDataDir("chevy", null, null);
        String path = prefix + File.separator + "data.json";
        Log.info("App data location: " + path);
        return new File(path);
    }

    /**
     * Carica il contenuto del file JSON in root.
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
     * Salva root nel file JSON e lo invalida, cosicché al prossimo utilizzo andrà ricaricato.
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