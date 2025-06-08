package src.domain.blitzengine;

import src.domain.cards.Card;
import java.util.Stack;

public class DiscardPile {
    private final Stack<Card> cards;

    public DiscardPile(){
        this.cards = new Stack<Card>();
    }

    public void addCard(Card card) {
        cards.push(card);
    }

    public Card peekTopCard() {
        if (!cards.isEmpty()) {
            return cards.peek();
        }
        return null;
    }

    public Card drawTopCard() {
        if (!cards.isEmpty()) {
            return cards.pop();
        }
        return null;
    }
}