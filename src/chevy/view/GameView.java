package chevy.view;

public class GameView {
    private final Window window;

    // TODO: mostra FPS a schermo in modo condizionale

    public GameView() {
        window = new Window(true);
    }

    public Window getWindow() {
        return window;
    }
}