package src.presentation;

import src.domain.blitzengine.*;
import src.domain.player.*;
import src.domain.stats.*;
import src.domain.cards.*;
import src.datasource.Observer;

import java.util.*;

public class GameController {
    private final GameView gameView;
    private final StatsManager statsManager;
    private final Blitz blitz;
    private final List<Player> players;
    private boolean gameRunning;
    private int currentPlayerIndex;


    public GameController() {
        this.gameView = new GameView();
        this.statsManager = new StatsManager(blitz, new Loader(), new Saver());

        Deck deck = new Deck();
        deck.shuffle();

        DiscardPile discardPile = new DiscardPile();

        Card firstDiscardCard = deck.drawCard();
        discardPile.addCard(firstDiscardCard);

        List<Observer> observers = new ArrayList<>();

        this.blitz = new Blitz(deck, discardPile, observers);

        this.players = new ArrayList<>();
        this.gameRunning = true;
    }

    public void runGameLoop() {
        gameView.displayGameSetup();
        setupPlayers();

        while (gameRunning) {
            GameState state = blitz.getCurrentGameState();

            switch (state) {
                case REGULAR_ROUND -> handleRegularRound();
                case KNOCK_ROUND -> handleKnockRound();
                case DECK_EMPTY -> handleDeckEmpty();
                case PLAYER_INSTANT_WIN, PLAYER_REGULAR_WIN, MULTIPLE_PLAYERS_WIN -> {
                    endGame(state);
                    gameRunning = false;
                }
                default -> System.out.println("Unknown game state: " + state);
            }
        }
    }

    private void setupPlayers() {
        int playerCount = -1;

        while (playerCount < 3 || playerCount > 6) {
            playerCount = gameView.promptNumberOfPlayers();
            if (playerCount < 3 || playerCount > 6) {
                System.out.println("Please enter a number between 3 and 6.");
            }
        }

        PlayerID[] ids = PlayerID.values();

        for (int i = 0; i < playerCount; i++) {
            Hand hand = new Hand(new ArrayList<>(), new BlitzScoringStrategy()); // COME BACK TO THIS

            // Deal 3 cards to this player
            for (int j = 0; j < 3; j++) {
                Card card = blitz.drawCardFromDeck();
                hand.addCard(card);
            }

            Player player;
            if (i == 0) {
                // First player is human
                player = new Player(ids[i], hand);
            } else {
                // All others are bots
                player = new Player(ids[i], hand, new SimpleAIStrategy(blitz));
            }
            players.add(player); // LOOK AT THIS AGAIN!!!!!!!!!!!!!!!!!!!
            gameView.displayInitialHand(player);
        }
    }

    private void switchTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }


    private void handleRegularRound() {
        Player currentPlayer = players.get(currentPlayerIndex);

        if (currentPlayer.isBot()) {
            PlayerTurn move = currentPlayer.makeMoveDecision();
            applyMove(currentPlayer, move);
        } else {
            int choice = gameView.promptHumanMove(currentPlayer);
            handleHumanChoice(choice, currentPlayer);
        }
    }

    private void handleKnockRound() {
        Player currentPlayer = players.get(currentPlayerIndex);
        PlayerID knockerId = blitz.getKnockerPlayerID(); // THIS METHOD DOES NOT EXIST, MIGHT NEED IT

        if (currentPlayer.isBot()) {
            if (currentPlayer.getPlayerId() == knockerId) {
                // Knocker's turn: end game now
                endGame(blitz.getCurrentGameState());
                gameRunning = false;
                return;
            } else {
                // Let AI pick move from deck or discard
                PlayerTurn move = currentPlayer.makeMoveDecision();
                if (move == PlayerTurn.KNOCK) {
                    // Bots can't knock here, force a valid move
                    move = PlayerTurn.DRAW_CARD_FROM_DECK;
                }
                applyMove(currentPlayer, move);
            }
        } else {
            if (currentPlayer.getPlayerId() == knockerId) {
                // Human knocker's turn, end game
                gameView.displayMessage("It's your turn and you knocked earlier. The game will now end.");
                endGame(blitz.getCurrentGameState());
                gameRunning = false;
                return;
            } else {
                // Human non-knocker turn: prompt to draw from deck or discard, then discard card, no knock allowed
                int choice = -1;
                while (choice != 1 && choice != 2) {
                    choice = gameView.promptHumanMove(currentPlayer);
                    if (choice != 1 && choice != 2) {
                        gameView.displayMessage("Invalid choice. You can only draw from the deck (1) or discard pile (2) in the knock round.");
                    }
                }
                handleHumanChoice(choice, currentPlayer);
            }
        }
        switchTurn();
    }

    private PlayerTurn convertChoiceToPlayerTurn(int choice) {
        return switch (choice) {
            case 1 -> PlayerTurn.DRAW_CARD_FROM_DECK;
            case 2 -> PlayerTurn.DRAW_CARD_FROM_DISCARD_PILE;
            case 3 -> PlayerTurn.KNOCK;
            default -> throw new IllegalArgumentException("Invalid choice"); // should never happen now
        };
    }

    private void handleHumanChoice(int choice, Player player) {
        PlayerTurn move = convertChoiceToPlayerTurn(choice);
        applyMove(player, move);
    }

    private void applyMove(Player player, PlayerTurn move) {
        switch (move) {
            case DRAW_CARD_FROM_DECK -> {
                Card drawnCard = blitz.drawCardFromDeck();
                player.getHand().addCard(drawnCard);
                gameView.displayMessage(player.getPlayerId() + " drew a card from the deck.");

                // Prompt discard
                Card discard = gameView.promptDiscardCard(player);
                player.getHand().removeCard(discard);
                blitz.discardCard(discard);
                gameView.displayMessage(player.getPlayerId() + " discarded " + discard);
            }
            case DRAW_CARD_FROM_DISCARD_PILE -> {
                Card drawnCard = blitz.drawCardFromDiscardPile();
                player.getHand().addCard(drawnCard);
                gameView.displayMessage(player.getPlayerId() + " drew a card from the discard pile.");

                // Prompt discard
                Card discard = gameView.promptDiscardCard(player);
                player.getHand().removeCard(discard);
                blitz.discardCard(discard);
                gameView.displayMessage(player.getPlayerId() + " discarded " + discard);
            }
            case KNOCK -> {
                blitz.knock(player.getPlayerId());
                gameView.displayMessage(player.getPlayerId() + " knocked.");
            }
            default -> {
                gameView.displayMessage("Invalid move.");
                return;
            }
        }

        blitz.updateTurn();
        blitz.notifyObservers();

        switchTurn();
    }

    public StatsManager getStatsManager() {
        return statsManager;
    }

}
    
