package chevy.service;

public interface Render {
    void render();

    default int getRenderPriority() {
        return 0;
    }
}
