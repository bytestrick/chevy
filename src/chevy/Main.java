package chevy;

import chevy.control.ChamberController;
import chevy.utils.Log;
import chevy.view.Window;

public class Main {
    public static void main(String[] args) {
        Window.initLookAndFeel();
        new ChamberController(new Window(true));
        Log.info("Chevy: AVVIO");
    }
}