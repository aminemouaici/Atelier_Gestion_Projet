package API;

public interface Operator<T> {
    void init(String query);
    T next();
    void close();
}
