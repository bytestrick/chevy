package chevy.control;

import chevy.Sound;
import chevy.model.chamber.ChamberManager;
import chevy.view.Window;
import chevy.view.chamber.ChamberView;
import chevy.view.hud.HUDView;

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

        // inizializzazione del controller della stanza
        HUDView hudView = this.window.getGamePanel().getHud();
        ChamberController chamberController = new ChamberController(this.window.getGamePanel().getChamberView(),
                ChamberManager.getInstance().getCurrentChamber(), hudView);
        keyboardListener.setChamber(chamberController);

        hudView.getPlayerInfo().setPlayer(chamberController.getChamber().getPlayer());
        Sound.getInstance().startMusic();
    }
}
