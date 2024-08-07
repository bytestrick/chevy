package chevy.control;

import chevy.control.collectableController.PowerUpTextVisualizerController;
import chevy.model.chamber.ChamberManager;
import chevy.view.Window;

/**
 * Gestisce l'interazione tra il model e la view.
 * Inizializza il controller della stanza e il listener della tastiera.
 */
public class GameController {
    private final Window window;
    private final KeyboardListener keyboardListener;

    public GameController(Window window) {
        this.window = window;
        this.keyboardListener = new KeyboardListener(window);

        // collega al chamberView la stanza (con le entità ordinate in base al layer) da disegnare
        this.window.getGamePanel().getChamberView().setDrawOrder(ChamberManager.getInstance().getCurrentChamber().getDrawOrderChamber());

        // inizializzazione del controller della stanza
        PowerUpTextVisualizerController powerUpTextVisualizerController = new PowerUpTextVisualizerController(
                this.window.getGamePanel().getPowerUpTextVisualizerController()
        );
        ChamberController chamberController = new ChamberController(ChamberManager.getInstance().getCurrentChamber(), powerUpTextVisualizerController);
        keyboardListener.setChamber(chamberController);
    }
}
