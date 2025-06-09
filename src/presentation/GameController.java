package presentation;

import domain.blitzengine.*;
import domain.player.*;
import domain.stats.*;
import domain.card.*;

import java.util.*;

public class GameController {
    private final GameView gameView;
    private final StatsManager statsManager;
    private final Blitz blitz;

    public GameController() {
        this.statsManager = new StatsManager();
        this.gameView = new GameView();
        this.blitz = new Blitz();
    }

    public StatsManager getStatsManager() {
        return statsManager;
    }

    public void runGameLoop() {
        gameView.displayGameSetup();
    }

    private void handleHumanChoice(int choice, Player player) {

    }
}
