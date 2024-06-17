package chevy.settings;

public class GameSettings {
    public static final int FPS = 60;
    public static final int FRAME_TARGET_TIME = 1000 / FPS; // quanto deve durare un frame per avere gli FPS desiderati


    public static int nTileH = 16;
    public static int nTileW = 16;
    public static float SCALE_H = (float) (WindowSettings.WINDOW_HEIGHT - WindowSettings.SIZE_TOP_BAR) / nTileH;
    public static float SCALE_W = (float) WindowSettings.WINDOW_WIDTH / nTileW;
    public static int SCALE = (int) (Math.min(SCALE_H, SCALE_W)); // scale che bisogna applicare ad ogni tile per avere il numero di celle in altezza e larghezza desiderato
}
