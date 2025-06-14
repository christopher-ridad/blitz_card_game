package src.domain.blitzengine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import src.domain.cards.Card;
import src.domain.cards.Rank;
import src.domain.cards.Suit;
import src.domain.player.ScoringStrategy;

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

    public boolean checkInstantWin(List<Card> cards) {
        Map<Suit, SuitFlags> suitFlagsMap = new HashMap<>();

        for (Card card : cards) {
            Suit suit = card.getSuit();
            SuitFlags flags = suitFlagsMap.getOrDefault(suit, new SuitFlags());

            Rank rank = card.getRank();
            if (rank == Rank.TEN) {
                flags.hasTen = true;
            } else if (rank == Rank.JACK || rank == Rank.QUEEN || rank == Rank.KING) {
                flags.hasFace = true;
            } else if (rank == Rank.ACE) {
                flags.hasAce = true;
            }

            suitFlagsMap.put(suit, flags);
        }

        for (SuitFlags flags : suitFlagsMap.values()) {
            if (flags.hasTen && flags.hasFace && flags.hasAce) {
                return true;
            }
        }
        return false;
    }

    private int getRankValue(Rank rank) {
        return switch (rank) {
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

    private static class SuitFlags {
        boolean hasTen = false;
        boolean hasFace = false;
        boolean hasAce = false;
    }
}
