package src.datasource;

public interface Loader {
    String next();
    boolean hasNext();
}