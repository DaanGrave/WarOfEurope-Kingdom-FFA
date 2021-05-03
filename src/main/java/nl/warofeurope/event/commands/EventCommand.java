package nl.warofeurope.event.commands;

import nl.warofeurope.event.Event;
import nl.warofeurope.event.utils.CommandBase;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.text.DecimalFormat;
import java.util.List;
import java.util.UUID;

import static nl.warofeurope.event.Event.color;

public class EventCommand extends CommandBase {
    private final DecimalFormat decimalFormat;

    public EventCommand(){
        super("event");

        this.decimalFormat = new DecimalFormat("###,##0");
    }

    @Override
    public boolean execute(CommandSender sender, Command cmd, String label, String[] args) {
        Bukkit.getScheduler().runTaskAsynchronously(Event.getInstance(), () -> {
            sender.sendMessage(color(" "));
            sender.sendMessage(color("&b&lEvent Status:"));
//            sender.sendMessage(color(" "));
//            sender.sendMessage(color("&6[Speler Top]"));
//            Event.getInstance().getPlayerDao().getAll().entrySet().stream().sorted((o1, o2) -> o2.getValue() - o1.getValue()).limit(3).forEach(player -> {
//                sender.sendMessage(Bukkit.getOfflinePlayer(UUID.fromString(player.getKey())).getName() + " " + this.decimalFormat.format(player.getValue()));
//            });
            sender.sendMessage(color(" "));
            sender.sendMessage(color("&6[Land Top]"));
            Event.getInstance().calculateLands().entrySet().stream().sorted((o1, o2) -> o2.getValue() - o1.getValue()).limit(5).forEach(land -> {
                sender.sendMessage(land.getKey() + " " + this.decimalFormat.format(land.getValue()));
            });
            sender.sendMessage(color(" "));
            sender.sendMessage(color("&7&oDit Event duurt tot 8 mei 15:45"));
        });
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        return null;
    }
}
