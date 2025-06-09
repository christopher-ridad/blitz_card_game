package presentation;

import domain.blitzengine.*;
import domain.player.*;
import domain.stats.*;
import domain.card.*;
import datasource.*;

import java.util.*;

public class GameController {
    private final GameView gameView;
    private final StatsManager statsManager;
    private final Blitz blitz;
    private Map<PlayerID, Player> players;
    private List<PlayerID> playerOrder;
    private int currentTurn;


    public GameController() {
        this.gameView = new GameView();

        // Initialize deck and discard pile for Blitz
        Deck deck = new Deck();
        deck.shuffle();
        DiscardPile discardPile = new DiscardPile();
        
        // Initialize observers list
        List<Observer> observers = new ArrayList<>();
        
        // Create Blitz instance
        this.blitz = new Blitz(deck, discardPile, observers);
        
        // Initialize StatsManager with proper dependencies
        Loader loader = new CSVFileLoader("game_stats.csv");
        Saver saver = new CSVFileSaver("game_stats.csv");
        this.statsManager = new StatsManager(blitz, loader, saver);

        
        this.players = new LinkedHashMap<>();
        this.playerOrder = new ArrayList<>();
        this.currentTurn = 0;
    }

    private void setup() {
        players.clear();
        playerOrder.clear();
        PlayerID[] allIds = PlayerID.values();

        // Create human player
        Player human = new Player(allIds[0], new Hand(new ArrayList<>(), new BlitzScoringStrategy()));
        players.put(allIds[0], human);
        playerOrder.add(allIds[0]);

        // Get bot configuration from user
        String[] botConfig = gameView.promptNumBotsAndDifficulty();
        int numBots = Integer.parseInt(botConfig[0]);
        String difficulty = botConfig[1];

        // Create bot players
        for (int i = 0; i < numBots; i++) {
            PlayerID botId = allIds[i + 1];
            
            AIStrategy aiStrategy = switch (difficulty.toLowerCase()) {
                case "easy" -> new SimpleAIStrategy(blitz);
                case "hard" -> new ProbabilisticAIStrategy(blitz);
                default -> new SimpleAIStrategy(blitz);
            };

            Player bot = new Player(botId, new Hand(new ArrayList<>(), new BlitzScoringStrategy()), aiStrategy);
            players.put(botId, bot);
            playerOrder.add(botId);
        }
        
        dealInitialCards();
        Collections.shuffle(playerOrder);
        currentTurn = 0;
    }

    private void dealInitialCards() {
        for (int cardCount = 0; cardCount < 3; cardCount++) {
            for (Player player : players.values()) {
                Card card = blitz.drawCardFromDeck();
                if (card != null) {
                    player.getHand().addCard(card);
                }
            }
        }
        Card initialDiscard = blitz.drawCardFromDeck();
        if (initialDiscard != null) {
            blitz.getDiscardPile().addCard(initialDiscard);
        }
    }

    public void runGameLoop() {
        gameView.displayGameSetup();
        setup();

        while (!isGameOver()) {
            PlayerID currentPlayerId = playerOrder.get(currentTurn);
            Player currentPlayer = players.get(currentPlayerId);
            
            gameView.displayTurnInfo(currentPlayer, blitz.getDeck().size(), 
                                   blitz.getDiscardPile().peekTopCard());

            try {
                if (currentPlayer.isBot()) {
                    handleBotTurn(currentPlayer);
                } else {
                    handleHumanTurn(currentPlayer);
                }
            } catch (Exception e) {
                System.err.println("Error during player turn: " + e.getMessage());
                e.printStackTrace();
            }

            detectAndHandleInstantWin();
            
            if (!isGameOver()) {
                switchPlayerTurn();
                gameView.displayDelay();
            }
        }

        Player winner = determineWinner();
        gameView.displayEndScreen(winner, new ArrayList<>(players.values()));
        
        statsManager.saveAllStats();
    }

        private void handleHumanTurn(Player player) {
        boolean canKnock = canPlayerKnock(player);
        int choice = gameView.promptPlayerChoice(canKnock);
        
        switch (choice) {
            case 1 -> handleDrawFromDeck(player);
            case 2 -> handleDrawFromDiscardPile(player);
            case 3 -> {
                if (canKnock) {
                    handleKnock(player);
                } else {
                    System.out.println("You cannot knock yet!");
                    handleHumanTurn(player);
                }
            }
            default -> {
                System.out.println("Invalid choice. Please try again.");
                handleHumanTurn(player);
            }
        }
    }

    private void handleBotTurn(Player bot) {
        try {
            Move move = bot.makeMoveDecision();
            executeMove(move);
            System.out.println(bot.getPlayerID() + " made their move.");
        } catch (NoMoveDecisionException e) {
            System.err.println("Bot failed to make a decision: " + e.getMessage());
            handleDrawFromDeck(bot);
        }
    }

    private void handleDrawFromDeck(Player player) {
        Card drawnCard = blitz.drawCardFromDeck();
        if (drawnCard != null) {
            if (player.isBot()) {
                player.getHand().addCard(drawnCard);
                Card toDiscard = selectCardToDiscard(player);
                player.getHand().removeCard(toDiscard);
                blitz.getDiscardPile().addCard(toDiscard);
            } else {

                boolean keepCard = gameView.confirmCardKeep(drawnCard);
                if (keepCard) {
                    player.getHand().addCard(drawnCard);
                    int discardIndex = gameView.promptDiscardChoice(player.getHand().getCards());
                    Card toDiscard = player.getHand().getCards().get(discardIndex);
                    player.getHand().removeCard(toDiscard);
                    blitz.getDiscardPile().addCard(toDiscard);
                } else {
                    blitz.getDiscardPile().addCard(drawnCard);
                }
            }
            
            Move move = new Move(PlayerTurn.DRAW_CARD_FROM_DECK, player, drawnCard, 
                               blitz.getDiscardPile().peekTopCard(), new Date());
            blitz.setLastMoveMade(move);
            blitz.notifyObservers();
        }
    }

    private void handleDrawFromDiscardPile(Player player) {
        Card drawnCard = blitz.getDiscardPile().drawTopCard();
        if (drawnCard != null) {
            player.getHand().addCard(drawnCard);
            
            if (player.isBot()) {
                Card toDiscard = selectCardToDiscard(player);
                player.getHand().removeCard(toDiscard);
                blitz.getDiscardPile().addCard(toDiscard);
            } else {
                int discardIndex = gameView.promptDiscardChoice(player.getHand().getCards());
                Card toDiscard = player.getHand().getCards().get(discardIndex);
                player.getHand().removeCard(toDiscard);
                blitz.getDiscardPile().addCard(toDiscard);
            }
            
            Move move = new Move(PlayerTurn.DRAW_CARD_FROM_DISCARD_PILE, player, drawnCard, 
                               blitz.getDiscardPile().peekTopCard(), new Date());
            blitz.setLastMoveMade(move);
            blitz.notifyObservers();
        }
    }

    private void handleKnock(Player player) {
        blitz.knock();
        System.out.println(player.getPlayerID() + " has knocked! Final round begins.");
        
        Move move = new Move(PlayerTurn.KNOCK, player, null, null, new Date());
        blitz.setLastMoveMade(move);
        blitz.notifyObservers();
    }

    private Card selectCardToDiscard(Player player) {
        List<Card> cards = player.getHand().getCards();
        Card worstCard = cards.get(0);

        for (Card card : cards) {
            if (card.getRank().ordinal() < worstCard.getRank().ordinal()) {
                worstCard = card;
            }
        }
        
        return worstCard;
    }

    private void executeMove(Move move) {
        switch (move.getPlayerTurn()) {
            case DRAW_CARD_FROM_DECK -> handleDrawFromDeck(move.getPlayer());
            case DRAW_CARD_FROM_DISCARD_PILE -> handleDrawFromDiscardPile(move.getPlayer());
            case KNOCK -> handleKnock(move.getPlayer());
        }
    }

    private void switchPlayerTurn() {
        currentTurn = (currentTurn + 1) % playerOrder.size();
    }

    private boolean canPlayerKnock(Player player) {
        return blitz.getCurrentGameState() == GameState.REGULAR_ROUND;
    }

    private boolean isGameOver() {
        GameState state = blitz.getCurrentGameState();
        return state == GameState.PLAYER_REGULAR_WIN || 
               state == GameState.MULTIPLE_PLAYERS_WIN ||
               state == GameState.PLAYER_INSTANT_WIN ||
               state == GameState.DECK_EMPTY;
    }

    private void detectAndHandleInstantWin() {
        for (Player player : players.values()) {
            if (player.getHand().getScoringStrategy().checkInstantWin(player.getHand().getCards())) {
                blitz.setCurrentGameState(GameState.PLAYER_INSTANT_WIN);
                System.out.println(player.getPlayerID() + " achieved an instant win!");
                return;
            }
        }
    }

    private Player determineWinner() {
        Player winner = null;
        int highestScore = -1;
        
        for (Player player : players.values()) {
            int score = player.getHand().getScore();
            if (score > highestScore) {
                highestScore = score;
                winner = player;
            }
        }
        
        return winner;
    }
}
