import java.util.*;

public class Deck {

    /***** INSTANCE VARIABLES  *****/
    public Stack<Card> cards; // Stack to hold the cards in the deck;
    private int size; // Number of cards in the deck

    /***** CONSTRUCTORS *****/
    
    public Deck(boolean fillDeck) {
        this.cards = new Stack<>();

        // Initialize the deck with 52 cards (13 ranks and 4 suits)
        if(fillDeck){
            for (int suit = 0; suit < 4; suit++) {
                for (int rank = 1; rank <= 13; rank++) {
                    
                    this.cards.push(new Card(rank, suit));
                    this.size++;
                }
            }
        }
        // Otherwise, the deck remains empty. //
    }

    
    /***** ACCESSOR METHODS *****/

    // getSize(): Return the size of the deck
    public int getSize(){
        return this.size;
    }

    /***** MUTATOR METHODS *****/

    // addCard(Card card): Adds a card to the top of the deck
    public void addCard(Card card){
        cards.push(card);
        this.size++;
    }
    
    // removeCard(): Remove a card from the top of the deck
    public Card removeCard(){
        this.size--;
        return cards.pop();
    }

    /***** DECK METHODS *****/

    // peekTopCard(): Returns the top card in the deck
    public Card peekTopCard() {
        if(getSize() == 0){
            return null; // Return null if the deck is empty
        }
        return cards.peek(); // Return the top card without removing it
    }

    // shuffle(): Shuffle the deck randomly
    public void shuffle() {
        Collections.shuffle(cards);
    }
}