package src.presentation;

import src.domain.player.Player;
import src.datasource.*;

import java.util.Scanner;

public class MainWindow{
    private GameController gameController;
    private StatsView statsView;
    private Scanner scanner;

    public MainWindow(){
        this.gameController = new GameController();
        this.statsView =  new StatsView(new CSVFileLoader());
        this.scanner = new Scanner (System.in);

    }

    public void display(){
        System.out.println("=== Welcome to Blitz Card Game ===");
        while (true) {
            System.out.println("(1) Start Game");
            System.out.println("(2) View Stats");
            System.out.println("(0) Exit");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> handleStartGame();
                case "2" -> handleViewStats();
                case "0" -> {
                    System.out.println("Thanks for playing!");
                    return;
                }
                default -> System.out.println("Invalid input. Try again.");
            }
        }
    }

    private void handleStartGame() {
        // System.out.print("Enter number of bots (1–3): ");
        // int numBots = Integer.parseInt(scanner.nextLine());

        // System.out.print("Select difficulty (easy/hard): ");
        // String difficulty = scanner.nextLine().trim().toLowerCase();

        // gameController.runGameLoop(numBots, difficulty);
        gameController.runGameLoop();
    }

    private void handleViewStats() {
        statsView.displayStats();
    }
}