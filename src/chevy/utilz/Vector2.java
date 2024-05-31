package chevy.utilz;

public class Vector2<T extends Number> {
    T x;
    T y;

    public Vector2(T x, T y) {
        this.x = x;
        this.y = y;
    }

    public void changeX(T x) { this.x = x; }

    public void changeY(T y) { this.y = y; }

    public void changePosition(Vector2<T> vector2) {
        this.x = vector2.x();
        this.y = vector2.y();
    }

    public T x() { return this.x; }
    public T y() { return this.y; }
}