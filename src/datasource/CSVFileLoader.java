package src.datasource;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

public class CSVFileLoader implements Loader {
    private final String fileName;
    private BufferedReader reader;
    private Queue<String> linesBuffer;

    public CSVFileLoader(String fileName) {
        this.fileName = fileName;
        this.linesBuffer = new LinkedList<>();
        initializeReader();
    }

    private void initializeReader() {
        try {
            reader = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = reader.readLine()) != null) {
                linesBuffer.offer(line);
            }
        } catch (IOException e) {
            System.err.println("Failed to open file: " + fileName);
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean hasNext() {
        return !linesBuffer.isEmpty();
    }

    @Override
    public String next() {
        return linesBuffer.poll();
    }
}