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
 * App data in <a href="https://ecma-international.org/publications-and-standards/standards/ecma-404/">JSON</a> format persistent through executions
 */
public final class Data {
    /**
     * Position of the file determined at the creation of the app
     */
    private static final File file = findFile();
    /**
     * The root of the structure, it's a <code>JSON Object</code>
     */
    private static String root;

    /**
     * @param path for the value to get
     * @param <T>  can be any type
     * @return the value contained at that path
     */
    public synchronized static <T> T get(String path) {
        if (root == null) {
            read();
        }
        return JsonPath.read(root, "$." + path);
    }

    /**
     * @param path  for the data to set
     * @param value the value to set
     */
    public synchronized static void set(String path, Object value) {
        if (root == null) {
            read();
        }
        root = JsonPath.parse(root).set("$." + path, value).jsonString();
    }

    /**
     * Increment a value
     *
     * @param path  path to the value to increment
     * @param value quantity to increment
     */
    public synchronized static void increase(String path, Integer value) {
        Integer oldValue = get(path);
        set(path, oldValue + value);
    }

    /**
     * Increment a value by 1
     *
     * @param path path to the value to increment
     */
    public synchronized static void increment(String path) {
        increase(path, 1);
    }

    /**
     * Check if the JSON file exists and is usable
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
     * Create the JSON file with the default data
     */
    public static void createPristineFile() {
        try {
            if (file.getParentFile().mkdirs()) {
                Log.info("The app data directory has been created");
            }
            try (InputStream in = Data.class.getResourceAsStream("/defaultChevyData.json")) {
                try (BufferedOutputStream out =
                             new BufferedOutputStream(new FileOutputStream(file))) {
                    assert in != null;
                    out.write(in.readAllBytes());
                }
            }
            root = null;
            Log.info(file + " was created");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * The file JSON is saved in the user data folder. For example, on a POSIX system compliant with
     * <a href="https://specifications.freedesktop.org/basedir-spec/0.6/">XDG Base Directory Specification</a>
     * the position will be <code>$XDG_DATA_HOME/chevy/data.json</code>. While on MS Windows the location will be
     * <code>%LocalAppData%\chevy\data.json</code>.
     */
    private static File findFile() {
        String prefix = AppDirsFactory.getInstance().getUserDataDir("chevy", null, null);
        String path = prefix + File.separator + "data.json";
        System.out.println("App data location: " + path);
        return new File(path);
    }

    /**
     * Load the JSON file content into {@link #root}
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
     * Save {@link #root} into the JSON file and invalidate it, so that at the next use it will be reloaded
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
