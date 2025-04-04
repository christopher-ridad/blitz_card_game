import java.util.*;

public class AIPlayer extends Player {
    public AIPlayer(String name) {
        super(name);
    }

    /***** AIPlayer METHODS *****/

    // improveHand(Card newCard): Determines which card to discard to improve the hand of the AI player
    public Card improveHand(Card newCard) {
        Card discardCard = newCard;
        int simulateScore = getScore();
        for(Card card : this.hand){
            int newScore = simulateHand(newCard, card); // Simualate the hand with every combination of cards of the hand and the newCard
            if(newScore > simulateScore){ // Determines which card to discard to maximize the hand's score
                discardCard = card;
                simulateScore = newScore;
            }
        }
        return discardCard; // Return the card to be discarded
    }

    // simulateHand(Card newCard, Card discardCard): Simulates the hand by adding a new card and removing a current card in the hand
    private int simulateHand(Card newCard, Card discardCard){
        ArrayList<Card> tempHand = new ArrayList<>();
        tempHand.addAll(this.hand); // Copy the current hand
        tempHand.remove(discardCard); // Remove the card to be discarded
        tempHand.add(newCard); // Add the new card
        return calculateScore(tempHand); // Calculate the score of the simulated hand
    }

    // shouldKnock(): Determines if the AI player should knock based on the strength of their hand
    public boolean shouldKnock() {
        // The AI player will knock if their score is 27 or higher
        return getScore() >= 27;
    }
}
