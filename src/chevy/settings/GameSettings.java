package chevy.settings;

public class GameSettings {
    public static final int FPS = 60;
    public static final int FRAME_TARGET_TIME = 1000 / FPS; // quanto deve durare un frame per avere gli FPS desiderati


    public static final int SIZE_TILE = 16;
    public static int nTileH = 0; // numero minimo di tile in altezza da visualizzare (verranno visualizzate sempre)
    public static int nTileW = 0; // numero minimo di tile in larghezza da visualizzare (verranno visualizzate sempre)
    public static float scaleH = (float) (WindowSettings.WINDOW_HEIGHT - WindowSettings.SIZE_TOP_BAR) / nTileH;
    public static float scaleW = (float) WindowSettings.WINDOW_WIDTH / nTileW;
    public static int scale = Math.round(Math.min(scaleH, scaleW)); // scale che bisogna applicare a ogni tile per avere il numero di celle in altezza e larghezza desiderato
    public static int offsetH = Math.max(0, (WindowSettings.WINDOW_HEIGHT - WindowSettings.SIZE_TOP_BAR - (nTileH * scale)) / 2);
    public static int offsetW = Math.max(0, (WindowSettings.WINDOW_WIDTH - (nTileW * scale)) / 2);

    public static void updateValue(int newNTileH, int newNTileW) {
        nTileH = newNTileH;
        nTileW = newNTileW;

        scaleH = (float) (WindowSettings.WINDOW_HEIGHT - WindowSettings.SIZE_TOP_BAR) / nTileH;
        scaleW = (float) WindowSettings.WINDOW_WIDTH / GameSettings.nTileW;

        scale = Math.round(Math.min(scaleH, scaleW));

        offsetH = Math.max(0, (WindowSettings.WINDOW_HEIGHT - WindowSettings.SIZE_TOP_BAR - Math.round((float) nTileH * scale)) / 2);
        offsetW = Math.max(0, (WindowSettings.WINDOW_WIDTH - Math.round((float) nTileW * scale)) / 2);
    }
}
