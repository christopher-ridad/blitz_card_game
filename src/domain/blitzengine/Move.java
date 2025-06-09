package src.domain.blitzengine;

import src.domain.cards.Card;
import src.domain.player.Player;

public class Move {
    private PlayerTurn playerTurn;
    private Player player;
    private Card cardDrawn;
    private Card cardDiscarded;
    //private Date timeStamp;

    public Move(PlayerTurn playerTurn, Player player, Card cardDrawn, Card cardDiscarded){
        this.playerTurn = playerTurn;
        this.player = player;
        this.cardDrawn = cardDrawn;
        this.cardDiscarded = cardDiscarded;
    }

    public PlayerTurn getPlayerTurn(){
        return this.playerTurn;
    }

    public Player getPlayer(){
        return this.player;
    }

    public Card getCardDrawn(){
        return this.cardDrawn;
    }

    public Card getCardDiscarded(){
        return this.cardDiscarded;
    }
}
