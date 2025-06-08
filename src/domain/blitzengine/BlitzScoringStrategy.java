package src.domain.blitzengine;

import src.domain.cards.Card;
import src.domain.cards.Suit;
import src.domain.cards.Rank;
import src.domain.player.ScoringStrategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlitzScoringStrategy implements ScoringStrategy {
    public int calculateScore(List<Card> cards){
        Map<Suit, Integer> suitScores = new HashMap<>();

        for (Card card : cards) {
            Suit suit = card.getSuit();
            int value = getRankValue(card.getRank());
            suitScores.put(suit, suitScores.getOrDefault(suit,0) + value);
        }

        int maxScore = 0;
        for (int score : suitScores.values()) {
            if (score > maxScore) {
                maxScore = score;
            }
        }
        return maxScore;
    }

    private int getRankValue(Rank rank) {
        return switch (rank) {
            case ONE -> 1;
            case TWO -> 2;
            case THREE -> 3;
            case FOUR -> 4;
            case FIVE -> 5;
            case SIX -> 6;
            case SEVEN -> 7;
            case EIGHT -> 8;
            case NINE -> 9;
            case TEN, JACK, QUEEN, KING -> 10;
            case ACE -> 11;
        };
    }
}
