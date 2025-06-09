package presentation;

import domain.blitzengine.*;
import domain.player.*;
import domain.stats.*;

import java.util.*;


public class GameController {
    private GameState gameState;
    private Blitz blitz;
    private StatsManager statsManager;
    private Map<Integer, Player> players = new HashMap<>();
    private int currentTurn;

    public GameController() {
        setup();
    }

    private void setup() {
        Deck deck = new Deck();
        DiscardPile discardPile = new DiscardPile();
        List<Observer> observers = new ArrayList<>();
        blitz = new Blitz(deck, discardPile, observers);

        // Add dummy players
        players.put(1, new Player());  // human
        players.put(2, new Player(new SimpleAIStrategy(blitz))); // bot

        statsManager = new StatsManager(blitz, new datasource.CSVFileLoader("stats.csv"),
                                               new datasource.CSVFileSaver("stats.csv"));
        observers.add(statsManager);
    }

    public void runGameLoop() {
        GameView view = new GameView();
        view.displayGameSetup();

        while (!gameEnded()) {
            // Example: simulate player turns here
            switchPlayerTurn();
            System.out.println("Player " + currentTurn + "'s turn...");
            // Implement detectAndHandle... methods to simulate moves
        }

        view.displayEndScreen();
    }

    private boolean gameEnded() {
        // Stub logic
        return false;
    }

    private void switchPlayerTurn() {
        currentTurn = (currentTurn % players.size()) + 1;
    }

    // Stub methods
    private void detectAndHandleDrawCardFromDeck() {}
    private void detectAndHandleDrawCardFromDiscardPile() {}
    private void detectAndHandleKnock() {}
    private void detectAndHandleInstantWin() {}
    private void detectAndHandleStatsViewing() {}
}