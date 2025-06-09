package presentation;

import domain.blitzengine.*;
import domain.player.*;
import domain.stats.*;
import domain.card.*;

import java.util.*;

public class GameController {
    private final GameView gameView;
    private final StatsManager statsManager;
    private final Blitz blitz;
    private Map<PlayerID, Player> players;
    private int currentTurn;


    public GameController() {
        this.statsManager = new StatsManager();
        this.gameView = new GameView();
        this.blitz = new Blitz();
        this.players = new LinkedHashMap<>();
        this.currentTurn = 0;
    }

    private void setup() {
        players = new LinkedHashMap<>();
        PlayerID[] allIds = PlayerID.values();

        Player human = new Player(allIds[0], new Hand(new ArrayList<>(), new BlitzScoringStrategy()));
        players.put(allIds[0], human);

        int numBots = gameView.promptNumBots();  // Implement prompt method in GameView
        for (int i = 0; i < numBots; i++) {
            PlayerID botId = allIds[i + 1];
            String difficulty = gameView.promptBotDifficulty(botId); // returns "easy" or "hard"

            AIStrategy aiStrategy = switch (difficulty.toLowerCase()) {
                case "easy" -> new SimpleAIStrategy(blitz);
                case "hard" -> new ProbabilisticAIStrategy(blitz);
                default -> new SimpleAIStrategy(blitz);
            };

            Player bot = new Player(botId, new Hand(new ArrayList<>(), new BlitzScoringStrategy()), aiStrategy);
            players.put(botId, bot);
        }

        //blitz.setupPlayers(new ArrayList<>(players.values()));  // You’ll need to implement this in Blitz
        currentTurn = 0;
    }

    public void runGameLoop() {
        gameView.displayGameSetup();

        setup();  // Setup players and Blitz

    }


    private void switchPlayerTurn() {
        PlayerID[] playerOrder = players.keySet().toArray(new PlayerID[0]);
        currentTurn = (currentTurn + 1) % playerOrder.length;
    }

    private void handleHumanChoice(int choice, Player player) {
        switch (choice) {
            case 1 -> PlayerTurn.DRAW_CARD_FROM_DISCARD_PILE;
            case 2 -> PlayerTurn.DRAW_CARD_FROM_DECK;
            case 3 -> PlayerTurn.KNOCK;
            default -> gameView.displayInvalidChoice();
        }
    }

}
