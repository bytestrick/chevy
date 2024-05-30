package chevy.settings;

import java.awt.*;

public class WindowSettings {
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


}
