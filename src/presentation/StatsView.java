package src.presentation;

import src.datasource.Loader;

public class StatsView {
    private final Loader loader;

    public StatsView(Loader loader) {
        this.loader = loader;
    }

    public void displayStats() {
        System.out.println("=== Player Stats / Game History ===");
        while (loader.hasNext()) {
            loader.next();
        }
        System.out.println("===================================");
    }
}
