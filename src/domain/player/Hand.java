package src.domain.player;

import src.domain.cards.Card;
import java.util.List;

public class Hand {
    private final List<Card> cards;
    private int score;
    private final ScoringStrategy scoringStrategy;

    public Hand(List<Card> cards, ScoringStrategy scoringStrategy){
        this.cards = cards;
        this.scoringStrategy = scoringStrategy;
    }

    void addCard(Card playingCard){
        this.cards.add(playingCard);
        calculateScore();
    }

    void removeCard(Card playingCard){
        this.cards.remove(playingCard);
        calculateScore();
    }

    private void calculateScore(){
        this.score = scoringStrategy.calculateScore(this.cards);
    }

    public List<Card> getCards(){
        return this.cards;
    }

    public int getScore(){
        return this.score;
    }

    public int size(){
        return this.cards.size();
    }
}