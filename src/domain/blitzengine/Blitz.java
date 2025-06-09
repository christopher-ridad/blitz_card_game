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

    public void drawCardFromDeck() {
        if (deck.isEmpty()) {
            currentGameState = GameState.DECK_EMPTY;
            notifyObservers();
            return;
        }

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

        //
//    OUTER:
//            while(true) {
//        System.out.println("====================");
//        System.out.println("Card Drawn!");
//        System.out.println("====================");
//        System.out.println("You drew: **" + drawnCard + "**");
//        System.out.println("Do you want to keep this card? (y/n)");
//
//        String choice = scan.next();
//
//        switch (choice) {
//            case "y" -> {
//                // Determine which card the player wants to discard from their hand
//                System.out.println("====================");
//                System.out.println("Discard a Card ");
//                System.out.println("====================");
//                System.out.println("Which card do you want to discard?");
//                System.out.println("(1) " + player.hand.get(0));
//                System.out.println("(2) " + player.hand.get(1));
//                System.out.println("(3) " + player.hand.get(2));
//                System.out.println("====================");
//                int discardChoice = Integer.parseInt(scan.next());
//                Card discardedCard = player.hand.get(discardChoice - 1);
//                // Add drawn card to player's hand and remove the discarded card
//                player.removeCard(discardedCard);
//                System.out.println("====================");
//                System.out.println("You discarded the " + discardedCard + ".");
//                System.out.println("====================");
//                player.addCard(drawnCard);
//                // Add discarded card to the discard pile
//                discardPile.addCard(discardedCard);
//                // Show new hand and score
//                displayNewHand(player);
//                break OUTER;
//            }
//            case "n" -> {
//                // Add the drawn card to the discard pile
//                System.out.println("====================");
//                System.out.println("You discarded the " + drawnCard + ".");
//                System.out.println("====================");
//                discardPile.addCard(drawnCard);
//                break OUTER;
//            }
//            default -> {
//                System.out.println("====================");
//                System.out.println("INVALID CHOICE. TRY AGAIN.");
//                System.out.println("====================");
//            }
//        }
//    }
//}
}
