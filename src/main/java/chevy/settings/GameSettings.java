package chevy.settings;

import chevy.utils.Log;

import static chevy.utils.Log.Level.INFO;

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
    public static int nTileH = 0;

    /**
     * Numero minimo di tile da visualizzare in larghezza in ogni momento.
     */
    public static int nTileW = 0;

    /**
     * Altezza ottimale di una cella, considerando l'altezza della finestra.
     */
    public static float optimalCellSizeH =
            (float) (WindowSettings.WINDOW_HEIGHT - WindowSettings.SIZE_TOP_BAR) / nTileH;

    /**
     * Larghezza ottimale di una cella, considerando la larghezza della finestra.
     */
    public static float optimalCellSizeW = (float) WindowSettings.WINDOW_WIDTH / nTileW;

    /**
     * Scale che bisogna applicare a ogni tile per avere il numero di celle in altezza e larghezza desiderato.
     */
    public static int optimalCellSize = Math.round(Math.min(optimalCellSizeH, optimalCellSizeW));

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

        optimalCellSizeH = (float) (WindowSettings.WINDOW_HEIGHT - WindowSettings.SIZE_TOP_BAR) / nTileH;
        optimalCellSizeW = (float) WindowSettings.WINDOW_WIDTH / GameSettings.nTileW;

        optimalCellSize = Math.round(Math.min(optimalCellSizeH, optimalCellSizeW));

        offsetH = Math.max(0,
                Math.round((float) (WindowSettings.WINDOW_HEIGHT - WindowSettings.SIZE_TOP_BAR - nTileH * optimalCellSize) / 2));
        offsetW = Math.max(0, Math.round((float) (WindowSettings.WINDOW_WIDTH - nTileW * optimalCellSize) / 2));
    }
}
