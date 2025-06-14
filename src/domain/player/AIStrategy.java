package src.domain.player;

import java.util.List;
import src.datasource.Observer;
import src.domain.blitzengine.PlayerTurn;
import src.domain.cards.Card;

public interface AIStrategy extends Observer {
    public PlayerTurn makeMoveDecision(Hand hand);
    public Card chooseBestCardToDiscard(List<Card> hand, Card incomingCard);
}
