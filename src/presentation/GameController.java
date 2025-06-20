package src.presentation;

import java.util.*;
import src.datasource.CSVFileLoader;
import src.datasource.CSVFileSaver;
import src.datasource.Observer;
import src.domain.blitzengine.*;
import src.domain.cards.*;
import src.domain.player.*;
import src.domain.stats.*;

public class GameController {
    private final GameView gameView;
    private final StatsManager statsManager;
    private final Blitz blitz;
    private final List<Player> players;
    private boolean gameRunning;
    private int currentPlayerIndex;


    public GameController() {
        this.gameView = new GameView();

        Deck deck = new Deck();
        deck.shuffle();

        DiscardPile discardPile = new DiscardPile();

        Card firstDiscardCard = deck.drawCard();
        discardPile.addCard(firstDiscardCard);

        List<Observer> observers = new ArrayList<>();

        this.blitz = new Blitz(deck, discardPile, observers);

        this.statsManager = new StatsManager(blitz, new CSVFileLoader("stats.csv"), new CSVFileSaver("stats.csv"));

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
                gameView.displayInvalidNumberOfPlayers();
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
            //gameView.displayInitialHand(player);

            if (hasInstantWinCombo(hand)) {
                gameView.displayMessage(player.getPlayerId() + " has an INSTANT WIN with 10 + Face Card + Ace of same suit!");
                gameView.displayDelay();
                endGame(GameState.PLAYER_INSTANT_WIN);
                gameRunning = false;
                return;
            }
        }
    }

    private boolean hasInstantWinCombo(Hand hand) {
        boolean hasTen = false;
        boolean hasFace = false; // JACK, QUEEN, KING
        boolean hasAce = false;
        Suit commonSuit = null;

        for (Card card : hand.getCards()) {
            Rank rank = card.getRank();
            Suit suit = card.getSuit();

            // Set the reference suit (first card's suit)
            if (commonSuit == null) {
                commonSuit = suit;
            }

            // If any card's suit doesn't match, return false
            if (suit != commonSuit) {
                return false;
            }

            // Check for required ranks
            switch (rank) {
                case TEN -> hasTen = true;
                case JACK, QUEEN, KING -> hasFace = true;
                case ACE -> hasAce = true;
            }
        }

        return hasTen && hasFace && hasAce;
    }


    private void switchTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }


    private void handleRegularRound() {
        Player currentPlayer = players.get(currentPlayerIndex);
        displayTurnInfoForCurrentPlayer(currentPlayer);
        PlayerTurn move; 

        if (currentPlayer.isBot()) {
            try {
                move = currentPlayer.makeMoveDecision(); 
            }
            catch (NoMoveDecisionException e) {
                e.printStackTrace();
                return; 
            }
            applyMove(currentPlayer, move);
        }
        else {
            int choice = gameView.promptHumanMove(currentPlayer);
            handleHumanChoice(choice, currentPlayer);
        }
        switchTurn();
    }

    private void handleKnockRound() {
        Player currentPlayer = players.get(currentPlayerIndex);
        displayTurnInfoForCurrentPlayer(currentPlayer);
        PlayerID knockerId = blitz.getKnockerPlayerID(); // THIS METHOD DOES NOT EXIST, MIGHT NEED IT

        if (currentPlayer.isBot()) {
            if (currentPlayer.getPlayerId() == knockerId) {
                // Knocker's turn: end game now
                endGame(blitz.getCurrentGameState());
                gameRunning = false;
                return;
            } else {
                PlayerTurn move = null;
                try{
                    move = currentPlayer.makeMoveDecision();
                } 
                catch (NoMoveDecisionException e) {
                    e.printStackTrace();
                    return;
                }

                if (move == PlayerTurn.KNOCK) {
                    // Bots can't knock here, force a valid move
                    move = PlayerTurn.DRAW_CARD_FROM_DECK;
                }
                applyMove(currentPlayer, move);
            }
        } else {
            if (currentPlayer.getPlayerId() == knockerId) {
                // Human knocker's turn, end game
                gameView.displayKnockerTurnMessage();
                endGame(blitz.getCurrentGameState());
                gameRunning = false;
                return;
            } else {
                // Human non-knocker turn: prompt to draw from deck or discard, then discard card, no knock allowed
                int choice = -1;
                while (choice != 1 && choice != 2) {
                    choice = gameView.promptHumanMove(currentPlayer);
                    if (choice != 1 && choice != 2) {
                        gameView.displayInvalidKnockRoundChoice();
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
            case DRAW_CARD_FROM_DECK -> {detectAndHandleDrawCardFromDeck(player);}

            case DRAW_CARD_FROM_DISCARD_PILE -> {detectAndHandleDrawCardFromDiscardPile(player);}

            case KNOCK -> {detectAndHandleKnock(player);}

            default -> {
                gameView.displayInvalidMove();
                gameView.displayDelay();
            }
        }
    }

    private void handleDeckEmpty() {
        gameView.displayDeckEmpty();
        gameView.displayDelay();
        endGame(GameState.DECK_EMPTY);
        gameRunning = false;
    }

    private void endGame(GameState endState) {
        Player winner = determineWinner();
        gameView.displayEndScreen(winner, players);
        statsManager.recordGameResult(players, endState);
    }

    private void detectAndHandleDrawCardFromDeck(Player player) {
        Card drawnCard = blitz.drawCardFromDeck();
        player.getHand().addCard(drawnCard);
        gameView.displayPlayerDrewCard(player.getPlayerId(), drawnCard, "deck");
        gameView.displayDelay();

        Card discard;
        if (player.isBot()) {
            try {
                discard = player.chooseBestCardToDiscard(drawnCard);
            } catch (NoMoveDecisionException e) {
                e.printStackTrace();
                return;
            }
        } else {
            discard = gameView.promptDiscardCard(player);
        }

        player.getHand().removeCard(discard);
        blitz.discardCard(discard);
        gameView.displayPlayerDiscardedCard(player.getPlayerId(), discard);
        gameView.displayDelay();
    }

    private void detectAndHandleDrawCardFromDiscardPile(Player player) {
        Card drawnCard = blitz.drawCardFromDiscardPile();
        player.getHand().addCard(drawnCard);
        gameView.displayPlayerDrewCard(player.getPlayerId(), drawnCard, "discard");
        gameView.displayDelay();

        Card discard;
        if (player.isBot()) {
            try {
                discard = player.chooseBestCardToDiscard(drawnCard);
            } catch (NoMoveDecisionException e) {
                e.printStackTrace();
                return;
            }
        } else {
            discard = gameView.promptDiscardCard(player);
        }

        player.getHand().removeCard(discard);
        blitz.discardCard(discard);
        gameView.displayPlayerDiscardedCard(player.getPlayerId(), discard);
        gameView.displayDelay();
    }
    private void detectAndHandleKnock(Player player) {
        blitz.knock(player.getPlayerId());
        gameView.displayPlayerKnocked(player.getPlayerId());
        gameView.displayDelay();
    }

    private void displayTurnInfoForCurrentPlayer(Player currentPlayer) {
        int deckSize = blitz.getDeckSize();
        Card topDiscard = blitz.seeTopCardOfDiscardPile();
        gameView.displayTurnInfo(currentPlayer, deckSize, topDiscard);
    }

    private Player determineWinner() {
        Player winner = null;
        int highestScore = Integer.MIN_VALUE;

        for (Player player : players) {
            int score = player.getHand().getScore();
            if (score > highestScore) {
                highestScore = score;
                winner = player;
            }
        }
        return winner;
    }
    
    public StatsManager getStatsManager() {
        return statsManager;
    }
}
    
