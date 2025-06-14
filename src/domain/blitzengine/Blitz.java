package src.domain.blitzengine;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import src.datasource.Observer;
import src.domain.cards.Card;
import src.domain.cards.Deck;

public class Blitz {
    private Deck deck;
    private DiscardPile discardPile;
    private GameState currentGameState;
    private List<Observer> observers;
    private Move lastMoveMade;
    private int currentPlayerTurn; // You can track whose turn it is, e.g., by player index
    private PlayerID knockerId = null;

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

    public int getDeckSize(){
        return deck.size();
    }

    public boolean deckIsEmpty() {
        boolean isEmpty = deck.isEmpty();
        if (isEmpty) {
            currentGameState = GameState.DECK_EMPTY;
            notifyObservers();
        }
        return isEmpty;
    }

    public Card drawCardFromDeck() {
        Card drawnCard = deck.drawCard();
        return drawnCard;
    }

    public Card drawCardFromDiscardPile() {
        Card drawnCard = discardPile.drawTopCard();
        notifyObservers();
        return drawnCard;
        
    }

    public void discardCard(Card card) {
        discardPile.addCard(card);
    }

    public void knock(PlayerID playerID) {
        currentGameState = GameState.KNOCK_ROUND;
        this.knockerId = playerID;
        lastMoveMade = new Move(
                PlayerTurn.KNOCK,
                null,
                null,
                null,
                new Date()
        );
        notifyObservers();
    }

    public PlayerID getKnockerPlayerID() {
        return knockerId;
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
