package chevy.service;

public interface Update {
    void update(double delta);

    default boolean updateIsEnd() { return false; }
}
