package nl.warofeurope.event.commands;

import nl.warofeurope.event.Event;
import nl.warofeurope.event.utils.CommandBase;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

import static nl.warofeurope.event.Event.color;

public class ToggleMineEventCommand extends CommandBase {
    public ToggleMineEventCommand(){
        super("togglemineevent", "op");
    }

    @Override
    public boolean execute(CommandSender sender, Command cmd, String label, String[] args) {
        boolean active = Event.getInstance().isActive();
        if (active) {
            sender.sendMessage(color("&cEvent is gestopt"));
        } else {
            sender.sendMessage(color("&aEvent is gestart"));
        }
        Event.getInstance().setActive(!active);
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        return null;
    }
}
