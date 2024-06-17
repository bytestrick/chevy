package chevy.service;

public interface Render {
    void render();

    default boolean renderIsEnd() { return false; }
}
