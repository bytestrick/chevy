package chevy.view;

import chevy.service.RenderManager;

public class GameView {
    private final Window window;

    // TODO: mostra FPS a schermo in modo condizionale

    public GameView() {
        System.out.println("Starting");

        window = new Window(true);
    }

    public Window getWindow() {
        return window;
    }
}