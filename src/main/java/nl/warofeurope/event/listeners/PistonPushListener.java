package nl.warofeurope.event.listeners;

import nl.warofeurope.event.Event;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class PistonPushListener implements Listener {
    @EventHandler
    public void onPush(BlockPistonRetractEvent event){
        for (Block block : event.getBlocks()){
            block.setMetadata("event_deny", new FixedMetadataValue(Event.getInstance(), true));
        }
    }
}
