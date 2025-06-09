package src.domain.player;

import src.domain.blitzengine.Move;
import src.domain.blitzengine.PlayerID;

//            +makeMoveDecision(): Move
//            +isBot(): boolean
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

    public Move makeMoveDecision() throws NoMoveDecisionException {
        if (aiStrategy == null) {
            throw new NoMoveDecisionException("AI strategy not set for this player.");
        }
        return this.aiStrategy.makeMoveDecision(this.hand);
    }

    public boolean isBot(){
        return this.isBot;
    }

    public hand getHand(){
        return this.hand;
    }


//    // setAutoWin(): Sets autoWin to true if their hand matches the automatic win condition
//    private void setAutoWin(){
//        Set<Integer> suits = new HashSet<>();
//        Set<Integer> ranks = new HashSet<>();
//
//        for (Card card : this.hand) {
//            suits.add(card.getSuit());
//            ranks.add(card.getRank());
//        }
//
//        if(suits.size() == 1 && ranks.contains(1) && ranks.contains(10) &&
//               (ranks.contains(11) || ranks.contains(12) || ranks.contains(13))){
//            this.autoWin = true;
//        }
//    }

}