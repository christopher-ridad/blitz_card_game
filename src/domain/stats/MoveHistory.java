package src.domain.stats;

import src.domain.blitzengine.Move;
import src.domain.player.Player;
import datasource.Loader;
import datasource.Saver;
import src.domain.blitzengine.Blitz;

import java.util.*;

class MoveHistory extends Stats {
    private List<Move> moves;

    public MoveHistory() {
        super("MoveHistory");
        this.moves = new ArrayList<>();
    }

    @Override
    public void update(Move move) {
        recordMove(move);
    }

    public void recordMove(Move move) {
        moves.add(move);
    }

    public List<String> loadMoves() {
        List<String> moveDescriptions = new ArrayList<>();
        for (Move move : moves) {
            moveDescriptions.add(move.toString());
        }
        return moveDescriptions;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Move History:\n");
        for (Move move : moves) {
            sb.append(move.toString()).append("\n");
        }
        return sb.toString();
    }
}