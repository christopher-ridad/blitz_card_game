package src.domain.player;

import src.domain.blitzengine.PlayerID;
import src.domain.blitzengine.PlayerTurn;
import src.domain.cards.Card;


public class Player {
    private final boolean isBot;
    // ??? private List<Stats> allStats;
    private final Hand hand;
    private final AIStrategy aiStrategy;
    private final PlayerID playerId;

    public Player(PlayerID playerId, Hand hand) {
        this.playerId = playerId;
        this.hand = hand;
        this.aiStrategy = null;
        this.isBot = false;
    }

    public Player(PlayerID playerId, Hand hand, AIStrategy aiStrategy) {
        this.playerId = playerId;
        this.hand = hand;
        this.aiStrategy = aiStrategy;
        this.isBot = true;
    }

    public PlayerID getPlayerId() {
        return playerId;
    }

    public PlayerTurn makeMoveDecision() throws NoMoveDecisionException {
        if (aiStrategy == null) {
            throw new NoMoveDecisionException("AI strategy not set for this player.");
        }
        return aiStrategy.makeMoveDecision(hand);
    }

    public Card chooseBestCardToDiscard(Card incomingCard) throws NoMoveDecisionException {
        if (aiStrategy == null) {
            throw new NoMoveDecisionException("AI strategy not set for this player.");
        }
        return aiStrategy.chooseBestCardToDiscard(hand.getCards(), incomingCard);
    }

    public boolean isBot(){
        return this.isBot;
    }

    public Hand getHand(){
        return this.hand;
    }
    
}