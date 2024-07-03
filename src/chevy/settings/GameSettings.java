package chevy.settings;

import chevy.utils.Log;

import java.util.zip.InflaterOutputStream;

import static chevy.utils.Log.Level.INFO;
import static chevy.utils.Log.Level.WARN;

public class GameSettings {
    public static final int FPS = 60;

    /**
     * Quanto deve durare (ms) un frame per avere gli FPS desiderati
     */
    public static final int FRAME_TARGET_TIME = 1000 / FPS;

    public static final int SIZE_TILE = 16;

    /**
     * Numero minimo di tile da visualizzare in altezza in ogni momento.
     */
    public static int nTileH = -1;

    /**
     * Numero minimo di tile da visualizzare in larghezza in ogni momento.
     */
    public static int nTileW = -1;

    /**
     * Altezza ottimale di una cella, considerando l'altezza della finestra.
     */
    public static float optimalCellHeight =
            (float) (WindowSettings.WINDOW_HEIGHT - WindowSettings.SIZE_TOP_BAR) / nTileH;

    /**
     * Larghezza ottimale di una cella, considerando la larghezza della finestra.
     */
    public static float optimalCellWidth = (float) WindowSettings.WINDOW_WIDTH / nTileW;

    /**
     * Scale che bisogna applicare a ogni tile per avere il numero di celle in altezza e larghezza desiderato.
     */
    public static int optimalCellSize = Math.round(Math.min(optimalCellHeight, optimalCellWidth));

    /**
     * Offset lungo y usato per centrare il contenuto nello schermo.
     */
    public static int offsetH = Math.max(0,
            (WindowSettings.WINDOW_HEIGHT - WindowSettings.SIZE_TOP_BAR - (nTileH * optimalCellSize)) / 2);

    /**
     * Offset lungo x usato per centrare il contenuto nello schermo.
     */
    public static int offsetW = Math.max(0, (WindowSettings.WINDOW_WIDTH - (nTileW * optimalCellSize)) / 2);

    /**
     * Regolamento della verbosità del logging secondo i livelli specificati da Log.Level.
     */
    public static Log.Level logLevel = INFO;

    public static void updateValue(int newNTileH, int newNTileW) {
        nTileH = newNTileH;
        nTileW = newNTileW;

        optimalCellHeight = (float) (WindowSettings.WINDOW_HEIGHT - WindowSettings.SIZE_TOP_BAR) / nTileH;
        optimalCellWidth = (float) WindowSettings.WINDOW_WIDTH / GameSettings.nTileW;

        optimalCellSize = Math.round(Math.min(optimalCellHeight, optimalCellWidth));

        offsetH = Math.max(0,
                (WindowSettings.WINDOW_HEIGHT - WindowSettings.SIZE_TOP_BAR - Math.round((float) nTileH * optimalCellSize)) / 2);
        offsetW = Math.max(0, (WindowSettings.WINDOW_WIDTH - Math.round((float) nTileW * optimalCellSize)) / 2);
    }
}