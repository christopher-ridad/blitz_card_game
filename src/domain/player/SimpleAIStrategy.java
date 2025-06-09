import src.domain.blitzengine.Blitz;
import src.domain.blitzengine.Move;
import src.domain.cards.Card;
import src.domain.blitzengine.PlayerTurn;
import src.domain.blitzengine.BlitzScoringStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class SimpleAIStrategy implements AIStrategy {
    private Blitz blitz;

    public Move makeMoveDecision(Hand hand) throws NoMoveDecisionException {
        Card topCard = blitz.getLastMoveMade().getCardDiscarded();

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
        Card drawnCard = topCard;
        hand.removeCard(discardCandidate);
        hand.addCard(drawnCard);
        return new Move(PlayerTurn.DRAW_CARD_FROM_DISCARD_PILE, null, drawnCard, discardCandidate, new Date());
    }

    @Override
    public void update() {

    }
}
