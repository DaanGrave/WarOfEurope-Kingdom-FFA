package nl.warofeurope.event.dao;

import nl.warofeurope.event.Event;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PlayerDao {
    private ConfigurationSection configurationSection;

    public PlayerDao(){
        this.configurationSection = Event.getInstance().getConfig().getConfigurationSection("players");
    }

    public void resetAll(){
        this.configurationSection = Event.getInstance().getConfig().createSection("players");
        Event.getInstance().getConfig().set("players", this.configurationSection);
        Event.getInstance().saveConfig();
    }

    public Map<String, Integer> getAll(){
        Map<String, Integer> players = new HashMap<>();
        Set<String> keys = this.configurationSection.getKeys(false);
        for (String key : keys){
            players.putIfAbsent(key, this.configurationSection.getInt(key));
        }
        return players;
    }

    public void reset(OfflinePlayer player){
        this.setPoints(player, 0);
    }

    public void addPoints(OfflinePlayer player, int points){
        int current = this.getPoints(player);
        int to = current + points;
        this.setPoints(player, to);
    }

    public void setPoints(OfflinePlayer player, int points){
        this.configurationSection.set(player.getUniqueId().toString(), points);
        Event.getInstance().saveConfig();
    }

    public int getPoints(OfflinePlayer player){
        return this.configurationSection.getInt(player.getUniqueId().toString(), 0);
    }
}
