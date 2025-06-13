package src.domain.blitzengine;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import src.domain.cards.Deck;
import src.domain.cards.Card;
import src.datasource.Observer;

public class Blitz {
    private Deck deck;
    private DiscardPile discardPile;
    private GameState currentGameState;
    private List<Observer> observers;
    private Move lastMoveMade;
    private int currentPlayerTurn; // You can track whose turn it is, e.g., by player index

    public Blitz(Deck deck, DiscardPile discardPile, List<Observer> observers) {
        this.deck = deck;
        this.discardPile = discardPile;
        this.observers = (observers != null) ? observers : new ArrayList<>();
        this.currentGameState = GameState.REGULAR_ROUND;
        this.lastMoveMade = null;
        this.currentPlayerTurn = 0;  // Starting player index (can be adjusted)
    }

    public Card seeTopCardOfDiscardPile() {
        return discardPile.peekTopCard();
    }

    public void deckIsEmpty() {
        if (deck.isEmpty()) {
            currentGameState = GameState.DECK_EMPTY;
            notifyObservers();
        }
        return isEmpty;
    }

    public void drawCardFromDeck() {


        Card drawnCard = deck.drawCard();
        lastMoveMade = new Move(
                PlayerTurn.DRAW_CARD_FROM_DECK,
                null,  // Set the player accordingly outside this class or extend method
                drawnCard,
                null,
                new Date()
        );

        // Add drawn card to player's hand logic happens outside Blitz class
        updateTurn();
        notifyObservers();
    }

    public void drawCardFromDiscardPile() {
        Card drawnCard = discardPile.drawTopCard();
        lastMoveMade = new Move(
                PlayerTurn.DRAW_CARD_FROM_DISCARD_PILE,
                null,
                drawnCard,
                null,
                new Date()
        );
        updateTurn();
        notifyObservers();
    }

    public void knock() {
        currentGameState = GameState.KNOCK_ROUND;
        lastMoveMade = new Move(
                PlayerTurn.KNOCK,
                null,
                null,
                null,
                new Date()
        );
        notifyObservers();
    }

    public GameState getCurrentGameState() {
        return currentGameState;
    }

    public void updateTurn() {
        // Increment player turn (example for 2 players, customize as needed)
        currentPlayerTurn = (currentPlayerTurn + 1) % 2;
        // Optionally update game state here, e.g., check for win conditions
    }

    public void addObserver(Observer observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        for (Observer obs : observers) {
            obs.update();
        }
    }

    public Move getLastMoveMade() {
        return lastMoveMade;
    }
}
