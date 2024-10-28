package chevy.service;

/**
 * Un oggetto da visualizzare
 */
public interface Renderable {
    /**
     * Disegna
     * @param delta tempo trascorso dall'ultimo ridisegno
     */
    void render(double delta);

    default boolean renderFinished() {return false;}
}
