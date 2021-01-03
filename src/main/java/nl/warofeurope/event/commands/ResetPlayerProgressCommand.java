package nl.warofeurope.event.commands;

import nl.warofeurope.event.Event;
import nl.warofeurope.event.utils.CommandBase;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

import static nl.warofeurope.event.Event.color;

public class ResetPlayerProgressCommand extends CommandBase {
    public ResetPlayerProgressCommand(){
        super("resetplayerprogress", "op");
    }

    @Override
    public boolean execute(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length != 1){
            sender.sendMessage(color("&cGebruik: /resetplayerprogress <player>"));
        } else {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
            Event.getInstance().getPlayerDao().reset(offlinePlayer);
            sender.sendMessage(color("&cSpeler gereset"));
        }
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        return null;
    }
}
