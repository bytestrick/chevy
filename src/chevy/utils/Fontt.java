package chevy.utils;

import chevy.utils.Log;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class Fontt {
    public static Font load(String path) {
        Font font = null;
        try {
            InputStream is = Fontt.class.getResourceAsStream("/" + path);
            font = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (IOException | FontFormatException e) {
            Log.error("Font '" + path + "' non trovato.");
            e.printStackTrace();
            System.exit(1);
        }
        return font;
    }
}
