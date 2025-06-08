package src.domain.player;

import src.domain.blitzengine.Blitz;
import src.domain.blitzengine.Move;
import src.domain.cards.Card;
import java.util.List;

public class SimpleAIStrategy implements AIStrategy {
    private Blitz blitz;

    public Move makeMoveDecision(Hand hand) throws NoMoveDecisionException {
        Card topCard = blitz.getTopOfDiscardPile();

        List<Card> currentHand = hand.getCards(); // You’ll need this method in Hand
        Card discardCard = topCard;
        int bestScore = hand.calculateScore();

        for (Card card : currentHand) {
            List<Card> simulated = new ArrayList<>(currentHand);
            simulated.remove(card);
            simulated.add(topCard);
            int simulatedScore = hand.calculateScore(simulated);

            if (simulatedScore > bestScore) {
                bestScore = simulatedScore;
                discardCard = card;
            }
        }

        if (bestScore >= 27) {
            return new Move("Knock");
        }

        hand.remove(discardCard);
        hand.add(discardPile.drawTopCard());
        return new Move("Discard", discardCard);
    }

    @Override
    public void update() {

    }

//    public Card improveHand(Card newCard) {
//        Card discardCard = newCard;
//        int simulateScore = getScore();
//        for(Card card : this.hand){
//            int newScore = simulateHand(newCard, card); // Simualate the hand with every combination of cards of the hand and the newCard
//            if(newScore > simulateScore){ // Determines which card to discard to maximize the hand's score
//                discardCard = card;
//                simulateScore = newScore;
//            }
//        }
//        return discardCard; // Return the card to be discarded
//    }
//
//    // simulateHand(src.domain.cards.Card newCard, src.domain.cards.Card discardCard): Simulates the hand by adding a new card and removing a current card in the hand
//    private int simulateHand(Card newCard, Card discardCard){
//        ArrayList<Card> tempHand = new ArrayList<>();
//        tempHand.addAll(this.hand); // Copy the current hand
//        tempHand.remove(discardCard); // Remove the card to be discarded
//        tempHand.add(newCard); // Add the new card
//        return calculateScore(tempHand); // Calculate the score of the simulated hand
//    }
//
//    // shouldKnock(): Determines if the AI player should knock based on the strength of their hand
//    public boolean shouldKnock() {
//        // The AI player will knock if their score is 27 or higher
//        return getScore() >= 27;
//    }
}
