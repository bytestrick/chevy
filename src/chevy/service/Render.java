package chevy.service;

public interface Render {
    void render();

    default boolean isEnd() { return false; }
}
