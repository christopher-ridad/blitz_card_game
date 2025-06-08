package src.domain.player;

import src.domain.blitzengine.Move;

//            +makeMoveDecision(): Move
//            +isBot(): boolean
public class Player {
    private final boolean isBot;
    // ??? private List<Stats> allStats;
    private final Hand hand;
    private final AIStrategy aiStrategy;

    public Player(Hand hand) {
        isBot = false;
        this.hand = hand;
        this.aiStrategy = null;
    }

    public Player(Hand hand, AIStrategy aiStrategy) {
        isBot = true;
        this.hand = hand;
        this.aiStrategy = aiStrategy;
    }

    public Move makeMoveDecision() throws NoMoveDecisionException {
        if (aiStrategy == null) {
            throw new NoMoveDecisionException("AI strategy not set for this player.");
        }
        return this.aiStrategy.makeMoveDecision();
    }

    public boolean isBot(){
        return this.isBot;
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