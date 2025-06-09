package presentation;

import domain.player.Player;
import java.util.Scanner;

public class MainWindow{
    private GameController gameController;
    private StatsView statsWindow;

    public void display() {
        printGameDescription();

        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter your name:");
        String playerName = scanner.nextLine();

        gameController = new GameController(playerName);  // Initialize controller with the player name
        statsWindow = new StatsView(gameController.getStatsManager().getLoader());

        while (true) {
            System.out.println("""
            ===========================================
            What would you like to do?
            1. Start Game
            2. View Stats
            3. Exit
            ===========================================
            """);

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> handleStartGame();
                case "2" -> handleViewStats();
                case "3" -> {
                    System.out.println("Thanks for playing Blitz!");
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void handleStartGame() {
        gameController.setup();
        gameController.runGameLoop();
    }

    private void handleViewStats() {
        statsWindow.displayStats();
    }

    public static void printGameDescription() {
        System.out.printf("""
        ===========================================
                      Blitz Card Game
        ===========================================
        Objective:
        Achieve the highest total value of cards of the same suit. The highest possible score is 31.

        How to Play:
        Each player is dealt 3 cards in their hand. On your turn, you can:
        - Draw from the deck
        - Take the top card from the discard pile
        - Knock

        If a player knocks, all others get one more turn. 
        The game ends if the deck is empty.
        A player instantly wins with 10 + face + Ace of the same suit.

        Card Values:
        - Number cards: face value
        - Face cards: 10 points
        - Aces: 11 points

        Scoring:
        Highest sum of same-suit cards in hand.

        ===========================================
        """);
    }
}