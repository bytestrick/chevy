package chevy.service;

public interface Render {
    void render(double delta);

    default boolean renderFinished() {return false;}
}