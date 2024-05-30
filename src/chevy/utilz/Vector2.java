package chevy.utilz;

public class Vector2<T extends Number> {
    T a;
    T b;

    public Vector2(T a, T b) {
        this.a = a;
        this.b = b;
    }

    public void changeFirst(T a) { this.a = a; }

    public void changeSecond(T b) { this.b = b; }

    public void change(Vector2<T> vector2) {
        this.a = vector2.first();
        this.b = vector2.second();
    }

    public T first() { return this.a; }
    public T second() { return this.b; }
}
