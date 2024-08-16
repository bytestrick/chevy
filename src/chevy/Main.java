package chevy;

import chevy.control.GameController;
import chevy.service.GameLoop;
import chevy.utils.Log;
import chevy.view.Window;

public class Main {
    public static void main(String[] args) {
        Window window = new Window(true);
        new GameController(window);
        new GameLoop();
        Log.info("Avvio");
    }
}