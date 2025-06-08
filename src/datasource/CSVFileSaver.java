package src.datasource;

public class CSVFileSaver implements Saver {
    private String filePath;

    public CSVFileSaver(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void append(String data) {
        // Append data to CSV file
    }
}

