package src.domain.player;

import src.datasource.Observer;
import src.domain.blitzengine.Move;

public interface AIStrategy extends Observer {
    public Move makeMoveDecision(Hand hand);
}
