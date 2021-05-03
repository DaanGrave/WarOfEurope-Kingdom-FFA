package nl.warofeurope.event.listeners;

import com.destroystokyo.paper.event.player.PlayerPickupExperienceEvent;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import nl.warofeurope.event.Event;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static nl.warofeurope.event.Event.color;

public class XPReceiveListener implements Listener {
    @EventHandler
    public void onXpReceive(PlayerPickupExperienceEvent event){
        if (Event.getInstance().isActive()){
            Player player = event.getPlayer();
            int experience = event.getExperienceOrb().getExperience();
            Event.getInstance().getPlayerDao().addPoints(player, experience);
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new ComponentBuilder(color("&d+ " + experience + " Punten &7(totaal: " + Event.getInstance().getPlayerDao().getPoints(player) + ")")).create());
        }
    }
}
