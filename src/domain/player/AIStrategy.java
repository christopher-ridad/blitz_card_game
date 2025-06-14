package src.domain.player;

import src.datasource.Observer;
import src.domain.blitzengine.Move;
import src.domain.blitzengine.PlayerTurn;
import src.domain.cards.Card;

import java.util.List;

public interface AIStrategy extends Observer {
    public PlayerTurn makeMoveDecision(Hand hand);
    public Card chooseBestCardToDiscard(List<Card> hand, Card incomingCard);
}
