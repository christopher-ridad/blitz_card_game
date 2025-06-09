package src.domain.stats;

import src.domain.blitzengine.Move;

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
