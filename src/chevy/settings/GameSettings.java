package chevy.settings;

public class GameSettings {
    public static final int FPS = 60;
    public static final int FRAME_TARGET_TIME = 1000 / FPS; // quanto deve durare un frame per avere gli FPS desiderati


    public static final int SIZE_TILE = 16;
    public static int nTileH = 0; // numero minimo di tile in altezza da visualizzare (verranno visualizzate sempre)
    public static int nTileW = 0; // numero minimo di tile in larghezza da visualizzare (verranno visualizzate sempre)
    public static float optimalCellSizeH = (float) (WindowSettings.WINDOW_HEIGHT - WindowSettings.SIZE_TOP_BAR) / nTileH; // calcola la dimensione ottimale di una cella in altezza, tenendo conto dell'altezza della finestra.
    public static float optimalCellSizeW = (float) WindowSettings.WINDOW_WIDTH / nTileW; // calcola la dimensione ottimale di una cella in larghezza, tenendo conto della larghezza della finestra.
    public static int optimalCellSize = Math.round(Math.min(optimalCellSizeH, optimalCellSizeW)); // scale che bisogna applicare a ogni tile per avere il numero di celle in altezza e larghezza desiderato
    public static int offsetH = Math.max(0, Math.round((float) (WindowSettings.WINDOW_HEIGHT - WindowSettings.SIZE_TOP_BAR - nTileH * optimalCellSize) / 2)); // offset usato per mantenere centrato il contenuto di segnato a schermo, su y
    public static int offsetW = Math.max(0, Math.round((float) (WindowSettings.WINDOW_WIDTH - nTileW * optimalCellSize) / 2)); // offset usato per mantenere centrato il contenuto di segnato a schermo, su x

    public static void updateValue(int newNTileH, int newNTileW) {
        nTileH = newNTileH;
        nTileW = newNTileW;

        optimalCellSizeH = (float) (WindowSettings.WINDOW_HEIGHT - WindowSettings.SIZE_TOP_BAR) / nTileH;
        optimalCellSizeW = (float) WindowSettings.WINDOW_WIDTH / GameSettings.nTileW;

        optimalCellSize = Math.round(Math.min(optimalCellSizeH, optimalCellSizeW));

        offsetH = Math.max(0, Math.round((float) (WindowSettings.WINDOW_HEIGHT - WindowSettings.SIZE_TOP_BAR - nTileH * optimalCellSize) / 2));
        offsetW = Math.max(0, Math.round((float) (WindowSettings.WINDOW_WIDTH - nTileW * optimalCellSize) / 2));
    }
}
