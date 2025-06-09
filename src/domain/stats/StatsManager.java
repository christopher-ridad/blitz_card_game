package src.domain.stats;

import src.domain.blitzengine.Move;
import src.domain.player.Player;
import datasource.Loader;
import datasource.Saver;
import src.domain.blitzengine.Blitz;

import java.util.*;

public class StatsManager implements src.datasource.Observer {
    private Map<Player, List<Stats>> playerSpecificStats;
    private Blitz blitz;
    private Loader loader;
    private Saver saver;

    public StatsManager(Blitz blitz, Loader loader, Saver saver) {
        this.blitz = blitz;
        this.loader = loader;
        this.saver = saver;
        this.playerSpecificStats = new HashMap<>();
    }

    public void registerPlayer(Player player) {
        List<Stats> statsList = new ArrayList<>();
        statsList.add(new MoveHistory());
        playerSpecificStats.put(player, statsList);
    }

    @Override
    public void update() {
        Move move = blitz.getLastMoveMade();
        if (move != null && playerSpecificStats.containsKey(move.getPlayer())) {
            for (Stats stat : playerSpecificStats.get(move.getPlayer())) {
                stat.update(move);
            }
        }
    }

    public void saveAllStats() {
        for (Map.Entry<Player, List<Stats>> entry : playerSpecificStats.entrySet()) {
            for (Stats stat : entry.getValue()) {
                saver.append(stat.toString());
            }
        }
    }

    public List<String> loadPlayerStats(Player player) {
        List<String> loaded = new ArrayList<>();
        while (loader.hasNext()) {
            loaded.add(loader.next().toString());
        }
        return loaded;
    }
} 
