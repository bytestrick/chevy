package chevy.service;

/**
 * An updatable entity
 */
public interface Updatable {
    /**
     * <em>Tick</em>
     * @param delta time since last update
     */
    void update(double delta);

    default boolean updateFinished() {return false;}
}
