import src.domain.stats;

import src.domain.blitzengine.Move;
import src.domain.player.Player;
import datasource.Loader;
import datasource.Saver;
import src.domain.blitzengine.Blitz;

import java.util.*;

public abstract class Stats {
    protected String type;

    public Stats(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public abstract void update(Move move);
}
