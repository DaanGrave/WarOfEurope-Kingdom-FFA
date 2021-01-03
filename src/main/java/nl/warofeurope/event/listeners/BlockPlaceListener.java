package nl.warofeurope.event.listeners;

import nl.warofeurope.event.Event;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class BlockPlaceListener implements Listener {
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){
        Block block = event.getBlock();
        block.setMetadata("event_deny", new FixedMetadataValue(Event.getInstance(), true));
    }
}
