package chevy.control;

import chevy.model.chamber.ChamberManager;
import chevy.view.Window;
import chevy.view.hud.HUD;

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
        HUD hud = this.window.getGamePanel().getHud();
        ChamberController chamberController = new ChamberController(ChamberManager.getInstance().getCurrentChamber(), hud);
        keyboardListener.setChamber(chamberController);

        hud.getPlayerInfo().setPlayer(chamberController.getChamber().getPlayer());
    }
}
