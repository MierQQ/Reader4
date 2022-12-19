package nsu.mier.reader.repository;

public interface Callback<T> {
    public void Invoke(T data);
}
