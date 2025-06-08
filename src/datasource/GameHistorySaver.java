package src.datasource;

public class GameHistorySaver implements Saver, Observer {
    private String filePath;

    public GameHistorySaver(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void append(String data) {
        // Append game history data
    }

    @Override
    public void update() {
        // React to observed changes
    }
}
