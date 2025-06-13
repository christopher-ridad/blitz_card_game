package src.domain.player;

import src.domain.blitzengine.Blitz;
import src.domain.cards.Card;
import src.domain.blitzengine.PlayerTurn;
import src.domain.blitzengine.BlitzScoringStrategy;

import java.util.ArrayList;
import java.util.List;

public class SimpleAIStrategy implements AIStrategy {
    private final Blitz blitz;

    public SimpleAIStrategy(Blitz blitz) {
        this.blitz = blitz;
        blitz.addObserver(this);
    }

    public PlayerTurn makeMoveDecision(Hand hand) {
        List<Card> currentHand = new ArrayList<>(hand.getCards());
        int currentScore = hand.getScore();

        if (currentScore >= 27) {
            return PlayerTurn.KNOCK;
        }

        Card discardTop = blitz.seeTopCardOfDiscardPile();
        if(improveHand(currentHand, discardTop)){
            return PlayerTurn.DRAW_CARD_FROM_DISCARD_PILE;
        }

        return PlayerTurn.DRAW_CARD_FROM_DECK;
    }

    private boolean improveHand(List<Card> hand, Card incomingCard) {
        BlitzScoringStrategy scoringStrategy = new BlitzScoringStrategy();
        int originalScore = scoringStrategy.calculateScore(hand);

        for (Card card : hand) {
            List<Card> simulatedHand = new ArrayList<>(hand);
            simulatedHand.remove(card);
            simulatedHand.add(incomingCard);

            int simulatedScore = scoringStrategy.calculateScore(simulatedHand);
            if (simulatedScore > originalScore) {
                return true;
            }
        }
        return false;
    }

    public Card chooseBestCardToDiscard(List<Card> hand, Card incomingCard) {
        Card bestCardToDiscard = null;
        int bestScore = 0;
        BlitzScoringStrategy scoringStrategy = new BlitzScoringStrategy();

        for (Card card : hand) {
            List<Card> simulatedHand = new ArrayList<>(hand);
            simulatedHand.remove(card);
            simulatedHand.add(incomingCard);

            int simulatedScore = scoringStrategy.calculateScore(simulatedHand);
            if (simulatedScore > bestScore) {
                bestScore = simulatedScore;
                bestCardToDiscard = card;
            }
        }
        return bestCardToDiscard;
    }

    @Override
    public void update() {
        return;
    }
}
