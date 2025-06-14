package src.datasource;

public class GameHistoryLoader implements Loader {
    private int gameID;

    public GameHistoryLoader(int gameID) {
        this.gameID = gameID;
    }

    @Override
    public String next() {
        // Load next history record
        return "";
    }

    @Override
    public boolean hasNext() {
        // Check if more history records exist
        return false;
    }
}
