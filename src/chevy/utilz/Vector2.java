package chevy.utilz;

public class Vector2<T extends Number> {
    public T first;
    public T second;

    public Vector2(T first, T second) {
        this.first = first;
        this.second = second;
    }

    public void change(Vector2<T> vector2) {
        this.first = vector2.first;
        this.second = vector2.second;
    }

    @Override
    public String toString() {
        return "Vector2(" + first + ", " + second + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vector2<?> vector2 = (Vector2<?>) o;

        if (!first.equals(vector2.first)) return false;
        return second.equals(vector2.second);
    }

    @Override
    public int hashCode() {
        int result = first.hashCode();
        result = 31 * result + second.hashCode();
        return result;
    }
}
