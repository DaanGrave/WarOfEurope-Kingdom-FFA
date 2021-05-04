package nl.warofeurope.event;

import lombok.Getter;
import net.milkbowl.vault.permission.Permission;
import nl.warofeurope.event.commands.EventCommand;
import nl.warofeurope.event.commands.ResetPlayerProgressCommand;
import nl.warofeurope.event.commands.ToggleMineEventCommand;
import nl.warofeurope.event.dao.PlayerDao;
import nl.warofeurope.event.listeners.BlockBreakListener;
import nl.warofeurope.event.listeners.BlockPlaceListener;
import nl.warofeurope.event.listeners.PistonPushListener;
import nl.warofeurope.event.listeners.XPReceiveListener;
import nl.warofeurope.event.utils.CommandBase;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class Event extends JavaPlugin {
    private @Getter PlayerDao playerDao;
    private @Getter Leaderboard leaderboard;
    private static Permission perms = null;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.playerDao = new PlayerDao();
        setupPermissions();

        this.register(
                new BlockBreakListener(),
                new BlockPlaceListener(),
                new PistonPushListener()
//                new XPReceiveListener()
        );
        this.register(
                new ToggleMineEventCommand(),
                new EventCommand(),
                new ResetPlayerProgressCommand()
        );

        this.leaderboard = new Leaderboard();
    }
    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }

    @Override
    public void onDisable() {
        this.leaderboard.removeLeaderboards();
    }

    public void register(Listener... listeners){
        Arrays.stream(listeners).forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, this));
    }

    public static Event getInstance(){
        return Event.getPlugin(Event.class);
    }

    public void register(CommandBase... commands){
        Arrays.stream(commands).forEach(command -> {
            getCommand(command.getCommand()).setExecutor(command);
            getCommand(command.getCommand()).setTabCompleter(command);
        });
    }

    public static String color(String text){
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public boolean isActive(){
        return this.getConfig().getBoolean("active", false);
    }

    public void setActive(boolean active){
        this.getConfig().set("active", active);
        this.saveConfig();
    }

    public Map<String, Integer> calculateLands(){
        Map<String, Integer> landPoints = new HashMap<>();
        for (String land : getConfig().getStringList("landen")){
            int totalPoints = this.playerDao.getAll().entrySet().stream().filter(i -> {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(i.getKey()));
                return Arrays.asList(perms.getPlayerGroups("world", offlinePlayer)).contains(land.toLowerCase());
            }).mapToInt(Map.Entry::getValue).sum();

            landPoints.putIfAbsent(land, totalPoints);
        }

        return landPoints;
    }
}
