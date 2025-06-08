package src.datasource;

public class CSVFileLoader implements Loader {
    private String fileName;

    public CSVFileLoader(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void next() {
        // Load next line from CSV
    }

    @Override
    public boolean hasNext() {
        // Check if more lines exist
        return false;
    }
}
