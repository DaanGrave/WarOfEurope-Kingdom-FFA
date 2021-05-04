package nl.warofeurope.event.listeners;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import nl.warofeurope.event.Event;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static nl.warofeurope.event.Event.color;

public class BlockBreakListener implements Listener {
    private final Map<Material, Integer> pointBlocks;

    public BlockBreakListener(){
        this.pointBlocks = new HashMap<>();
        ConfigurationSection configurationSection = Event.getInstance().getConfig().getConfigurationSection("points");

        Set<String> keys = configurationSection.getKeys(false);
        for (String key : keys){
            try {
                this.pointBlocks.putIfAbsent(Material.valueOf(key), configurationSection.getInt(key));
            } catch (Exception ignored){
                System.out.println("Block niet gevonden: " + key);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event){
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Material material = block.getType();

        if (this.pointBlocks.containsKey(material) && !block.hasMetadata("event_deny") && Event.getInstance().isActive()){
            int points = pointBlocks.get(material);
            Event.getInstance().getPlayerDao().addPoints(player, points);
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new ComponentBuilder(color("&d+ " + points + " Punten &7(totaal: " + Event.getInstance().getPlayerDao().getPoints(player) + ")")).create());
        }
    }
}
