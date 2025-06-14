package src.presentation;

import java.util.Scanner;
import src.datasource.*;

public class MainWindow{
    private GameController gameController;
    private StatsView statsView;
    private Scanner scanner;

    public MainWindow(){
        this.gameController = new GameController();
        this.statsView =  new StatsView(new CSVFileLoader("path/to/your/file.csv"));
        this.scanner = new Scanner(System.in);

    }

    public void display() {
        System.out.println("=== Welcome to Blitz Card Game ===");
        System.out.println("(1) Start Game");
        System.out.println("(2) View Stats");
        System.out.println("(0) Exit");

        String choice = scanner.nextLine();
        switch (choice) {
            case "1" -> {
                handleStartGame();
                // Exit immediately after game ends
                System.out.println("Thanks for playing!");
            }
            case "2" -> {
                handleViewStats();
                // Optionally exit or prompt again
                System.out.println("Thanks for playing!");
            }
            case "0" -> System.out.println("Thanks for playing!");
            default -> System.out.println("Invalid input. Exiting.");
        }
    }

    private void handleStartGame() {
        gameController.runGameLoop();
    }

    private void handleViewStats() {
        statsView.displayStats();
    }
}