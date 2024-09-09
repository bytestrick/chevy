package chevy.service;

/**
 * Un'entità aggiornabile
 */
public interface Updatable {
    /**
     * <em>Tick</em>
     * @param delta tempo trascorso dall'ultimo tick
     */
    void update(double delta);

    default boolean updateFinished() {return false;}
}