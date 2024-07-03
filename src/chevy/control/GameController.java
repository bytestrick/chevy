package chevy.control;

import chevy.model.chamber.ChamberManager;
import chevy.view.GameView;

/**
 * Gestisce l'interazione tra il model e la view.
 * Inizializza il controller della stanza e il listener della tastiera.
 */
public class GameController {
    /**
     * Riferimento alla vista di gioco.
     */
    private final GameView gameView;
    /**
     * Riferimento al listener della tastiera.
     */
    private final KeyboardListener keyboardListener;

    /**
     * @param gameView  riferimento alla vista di gioco
     */
    public GameController(GameView gameView) {
        this.gameView = gameView;
        this.keyboardListener = new KeyboardListener(gameView);

        // collega al chamberView la stanza (con le entità ordinate in base al layer) da disegnare
        this.gameView.getWindow().getGamePanel().getChamberView().setDrawOrder(ChamberManager.getInstance().getCurrentChamber().getDrawOrderChamber());

        // inizializzazione del controller della stanza
        ChamberController chamberController = new ChamberController(ChamberManager.getInstance().getCurrentChamber());
        keyboardListener.setChamber(chamberController);
    }
}