package src.domain.player;

import java.util.List;
import src.domain.cards.Card;

public interface ScoringStrategy {
    public int calculateScore(List<Card> cards);
    public boolean checkInstantWin(List<Card> cards); // New method
}
