package chevy.control;

import chevy.model.GameModel;
import chevy.view.GameView;

/**
 * La classe GameController gestisce l'interazione tra il model e la view.
 * Questa classe si occupa di inizializzare il controller della stanza e il listener della tastiera.
 */
public class GameController {
    /**
     * Riferimento al modello di gioco.
     */
    private final GameModel gameModel;
    /**
     * Riferimento alla vista di gioco.
     */
    private final GameView gameView;
    /**
     * Riferimento al listener della tastiera.
     */
    private final KeyboardListener keyboardListener;

    /**
     * Inizializza il controller del gioco con i riferimenti al modello di gioco e alla vista di gioco.
     * Crea il listener della tastiera e inizializza il controller della stanza.
     * @param gameModel riferimento al modello di gioco
     * @param gameView riferimento alla vista di gioco
     */
    public GameController(GameModel gameModel, GameView gameView) {
        this.gameModel = gameModel;
        this.gameView = gameView;
        this.keyboardListener = new KeyboardListener(gameView);

        // collega al chamberView la stanza (con le entità ordinate in base al layer) da disegnare
        this.gameView.getWindow().getGamePanel().getChamberView().setDrawOrderChamber(gameModel.getCurrentChamber().getDrawOrderChamber());

        // inizializzazione del controller della stanza
        ChamberController chamberController = new ChamberController(gameModel.getCurrentChamber());
        keyboardListener.setChamber(chamberController);
    }
}
