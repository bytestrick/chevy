package chevy.service;

public interface Update {
    void update(double delta);

    default boolean isEnd() { return false; }
}
