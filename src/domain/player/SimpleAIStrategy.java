package src.domain.player;

import src.domain.blitzengine.Blitz;
import src.domain.blitzengine.Move;
import src.domain.cards.Card;
import src.domain.blitzengine.PlayerTurn;
import src.domain.blitzengine.BlitzScoringStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class SimpleAIStrategy implements AIStrategy {
    private final Blitz blitz;
    private Move latestMove;

    public SimpleAIStrategy(Blitz blitz) {
        this.blitz = blitz;
        this.latestMove = null;
        blitz.addObserver(this);
    }

    public Move makeMoveDecision(Hand hand){
        Card topCard = latestMove.getCardDiscarded();

        List<Card> currentHand = new ArrayList<>(hand.getCards()); // You’ll need this method in Hand
        Card discardCandidate = topCard;
        int bestScore = hand.getScore();

        for (Card card : currentHand) {
            List<Card> simulated = new ArrayList<>(currentHand);
            simulated.remove(card);
            simulated.add(topCard);
            int simulatedScore =  new BlitzScoringStrategy().calculateScore(simulated)

            if (simulatedScore > bestScore) {
                bestScore = simulatedScore;
                discardCandidate = card;
            }
        }

        if (bestScore >= 27) {
            blitz.knock();
            return new Move(PlayerTurn.KNOCK, null, null, null, new Date());
        }

        blitz.drawCardFromDiscardPile();
        hand.removeCard(discardCandidate);
        hand.addCard(topCard);
        return new Move(PlayerTurn.DRAW_CARD_FROM_DISCARD_PILE, null, topCard, discardCandidate, new Date());
    }

    @Override
    public void update() {
        this.latestMove = blitz.getLastMoveMade();
    }
}
