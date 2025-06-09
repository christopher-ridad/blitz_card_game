package src.datasource;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CSVFileSaver implements Saver {
    private final String filePath;

    public CSVFileSaver(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void append(String data) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(data);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Failed to write to file: " + filePath);
            e.printStackTrace();
        }
    }
}