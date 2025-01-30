package chevy.service;

/**
 * A renderable object
 */
public interface Renderable {
    /**
     * Render the object
     * @param delta time since last render
     */
    void render(double delta);

    default boolean renderFinished() {return false;}
}
