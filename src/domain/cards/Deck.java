package src.domain.cards;

import java.util.Stack;
import java.util.Collections;

public class Deck {

    private final Stack<Card> cards;

    public Deck() {
        this.cards = new Stack<>();

        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                this.cards.add(new Card(rank, suit));
            }
        }
    }

    public void shuffle() {
        Collections.shuffle(this.cards);
    }

    public Card drawCard(){
        return this.cards.pop();
    }

    public boolean isEmpty(){
        return this.cards.isEmpty();
    }

    public int size(){
        return this.cards.size();
    }
}