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

    public void runGameLoop(int numBots, String difficulty) {
        gameView.displayGameSetup();

        blitz.setup();
        while (!blitz.isGameOver()) {
            Player currentPlayer = blitz.getCurrentPlayer();
            gameView.displayTurnInfo(currentPlayer, blitz.getDeckSize(), blitz.getTopDiscardCard());

            if (!currentPlayer.isAI()) {
                int choice = gameView.promptPlayerChoice(!blitz.hasKnocked());
                handleHumanChoice(choice, currentPlayer);
            } else {
                blitz.processAITurn((AIPlayer) currentPlayer);
                gameView.displayDelay();
            }

            blitz.advanceTurn();
        }

        Player winner = blitz.determineWinner();
        gameView.displayEndScreen(winner, blitz.getAllPlayers());
        statsManager.recordGameResult(winner);
    }

    private void handleHumanChoice(int choice, Player player) {
        switch (choice) {
            case 1 -> blitz.drawFromDeck(player, gameView);
            case 2 -> blitz.drawFromDiscardPile(player, gameView);
            case 3 -> blitz.knock(player);
        }
    }
}
