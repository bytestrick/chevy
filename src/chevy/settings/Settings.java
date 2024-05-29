package chevy.settings;

import chevy.view.Window;

import java.awt.*;

public class Settings {
    public static int SIZE_TOP_BAR = 0; // il valore viene impostato non appena la finestra di gioco si apre

    private static final int MAX_WINDOW_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;
    private static final int MAX_WINDOW_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
    private static final float WINDOW_SCALE = 0.8f; // da 0 a 1


//    Questo approccio mantiene l'aspectratio deciso in precedenza.
    private static final int ASPECT_RATIO_W = 4;
    private static final int ASPECT_RATIO_H = 3;
    private static final int MAX_WINDOW_SIZE = Math.round(Math.min(MAX_WINDOW_WIDTH, MAX_WINDOW_HEIGHT) * WINDOW_SCALE);
    public static final int WINDOW_HEIGHT = MAX_WINDOW_WIDTH >= MAX_WINDOW_HEIGHT ?
            MAX_WINDOW_SIZE :
            MAX_WINDOW_SIZE * ASPECT_RATIO_H / ASPECT_RATIO_W;
    public static final int WINDOW_WIDTH = MAX_WINDOW_WIDTH <= MAX_WINDOW_HEIGHT ?
            MAX_WINDOW_SIZE :
            MAX_WINDOW_SIZE * ASPECT_RATIO_W / ASPECT_RATIO_H;

//    Questo approccio mantiene l'aspect-ratio dello schermo
//    public static final int WINDOW_HEIGHT = Math.round(MAX_WINDOW_EIGHT * WINDOW_SCALE);
//    public static final int WINDOW_WIDTH = Math.round(MAX_WINDOW_WIDTH * WINDOW_SCALE);

    public static final int TILE_SIZE = 16;
    private static int N_TILE_PER_HEIGHT = 4;
    private static int N_TILE_PER_WIDTH = 4;
    private static final int SCALE_H = (WINDOW_HEIGHT - SIZE_TOP_BAR) * N_TILE_PER_HEIGHT / TILE_SIZE;
    private static final int SCALE_W = WINDOW_WIDTH * N_TILE_PER_WIDTH / TILE_SIZE;
    public static final int TILE_SCALE = Math.min(SCALE_H, SCALE_W); // scale che bisogna applicare ad ogni tile per avere il numero di celle in altezza e larghezza desiderato


    public static void setNCellsPerHeight(int n) {
        N_TILE_PER_HEIGHT = n;
    }

    public static void setNCellsPerWidth(int n) {
        N_TILE_PER_WIDTH = n;
    }
}
