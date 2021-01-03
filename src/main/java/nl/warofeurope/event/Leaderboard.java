package nl.warofeurope.event;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

import static nl.warofeurope.event.Event.color;

public class Leaderboard {
    private final @Getter List<ArmorStand> playerLeaderboard;
    private final @Getter List<ArmorStand> landLeaderboard;
    private final double TO_REMOVE = .35;
    private final DecimalFormat decimalFormat;

    public Leaderboard(){
        this.playerLeaderboard = new ArrayList<>();
        this.landLeaderboard = new ArrayList<>();
        this.decimalFormat = new DecimalFormat("###,##0");
        this.renderLeaderboards();

        Bukkit.getScheduler().runTaskTimerAsynchronously(Event.getInstance(), this::reloadLeaderboards, 0L, 600L);
    }

    public void renderLeaderboards(){
        this.playerLeaderboard();
        this.landLeaderboard();
    }

    private void playerLeaderboard(){
        ConfigurationSection playerSpawnLocation = Event.getInstance().getConfig().getConfigurationSection("leaderboards.player");
        Location SPAWN_LOCATION = new Location(
                Bukkit.getWorld(playerSpawnLocation.getString("world")),
                playerSpawnLocation.getDouble("x"),
                playerSpawnLocation.getDouble("y"),
                playerSpawnLocation.getDouble("z")
        ).add(0, 2.25, 0);

        for (int i = 0; i < 12; i++){
            SPAWN_LOCATION = SPAWN_LOCATION.add(0, -TO_REMOVE, 0);
            ArmorStand armorStand = SPAWN_LOCATION.getWorld().spawn(SPAWN_LOCATION, ArmorStand.class);
            this.setArmorStandThings(armorStand, "&b#" + (i + 1) + " &7Niemand");
            this.playerLeaderboard.add(armorStand);
        }
    }

    private void landLeaderboard(){
        ConfigurationSection playerSpawnLocation = Event.getInstance().getConfig().getConfigurationSection("leaderboards.land");
        Location SPAWN_LOCATION = new Location(
                Bukkit.getWorld(playerSpawnLocation.getString("world")),
                playerSpawnLocation.getDouble("x"),
                playerSpawnLocation.getDouble("y"),
                playerSpawnLocation.getDouble("z")
        ).add(0, 2.25, 0);

        for (int i = 0; i < 12; i++){
            SPAWN_LOCATION = SPAWN_LOCATION.add(0, -TO_REMOVE, 0);
            ArmorStand armorStand = SPAWN_LOCATION.getWorld().spawn(SPAWN_LOCATION, ArmorStand.class);
            this.setArmorStandThings(armorStand, "&b#" + (i + 1) + " &7Geen Land");
            this.landLeaderboard.add(armorStand);
        }
    }

    private void setArmorStandThings(ArmorStand armorStand, String name){
        armorStand.setGravity(false);
        armorStand.setVisible(false);
        armorStand.setCustomNameVisible(true);
        armorStand.setCustomName(color(name));
    }

    public void reloadLeaderboards(){
        this.reloadPlayerLeaderboard();
        this.reloadLandLeaderboard();
    }

    private void reloadLandLeaderboard(){
        List<Map.Entry<String, Integer>> landEntries = Event.getInstance().calculateLands().entrySet().stream().sorted((o1, o2) -> o2.getValue() - o1.getValue()).limit(12).collect(Collectors.toList());

        int armorstandIndex = 0;
        for (ArmorStand armorStand : this.landLeaderboard){
            armorstandIndex++;
            try {
                Map.Entry<String, Integer> entry = landEntries.get(armorstandIndex - 1);
                armorStand.setCustomName(color("&b#" + armorstandIndex + " &7" + entry.getKey() + ": &f&l" + this.decimalFormat.format(entry.getValue()) + " &fPunten"));
            } catch (IndexOutOfBoundsException e){
                armorStand.setCustomName(color("&b#" + armorstandIndex + " &7Geen Land"));
            }
        }
    }

    private void reloadPlayerLeaderboard(){
        List<Map.Entry<String, Integer>> playerEntries = Event.getInstance().getPlayerDao().getAll().entrySet().stream().filter(i -> {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(i.getKey());
            return !offlinePlayer.isBanned();
        }).sorted((o1, o2) -> o2.getValue() - o1.getValue()).limit(12).collect(Collectors.toList());
        int armorstandIndex = 0;
        for (ArmorStand armorStand : this.playerLeaderboard){
            armorstandIndex++;
            try {
                Map.Entry<String, Integer> entry = playerEntries.get(armorstandIndex - 1);
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(entry.getKey()));
                armorStand.setCustomName(color("&b#" + armorstandIndex + " &7" + offlinePlayer.getName() + ": &f&l" + this.decimalFormat.format(entry.getValue()) + " &fPunten"));
            } catch (IndexOutOfBoundsException e){
                armorStand.setCustomName(color("&b#" + armorstandIndex + " &7Niemand"));
            }
        }
    }

    public void removeLeaderboards(){
        this.playerLeaderboard.forEach(Entity::remove);
        this.landLeaderboard.forEach(Entity::remove);
    }
}
