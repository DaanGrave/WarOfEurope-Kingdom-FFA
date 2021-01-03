package nl.warofeurope.event.utils;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;

import static nl.warofeurope.event.Event.color;

public abstract class CommandBase implements CommandExecutor, TabCompleter {
    private final @Getter String command;
    private @Getter String permission;
    private @Getter @Setter String noPermission;

    public CommandBase(String command) {
        this.command = command;
    }

    public CommandBase(String command, String permission) {
        this.command = command;
        this.permission = "warofeurope.mineevent." + permission;
    }

    public CommandBase(String command, boolean op){
        this.command = command;
        if (op){
            this.permission = "op";
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (this.getPermission() != null){
            if (this.getPermission().equalsIgnoreCase("op")){
                if (!sender.isOp()){
                    sender.sendMessage(color(getNoPermission()));
                    return true;
                }
            } else {
                if (!sender.hasPermission(this.permission)){
                    sender.sendMessage(color(getNoPermission()));
                    return true;
                }
            }
        }

        return this.execute(sender, cmd, label, args);
    }

    public abstract boolean execute(CommandSender sender, Command cmd, String label, String[] args);

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (this.getPermission() != null){
            if (this.getPermission().equalsIgnoreCase("op")){
                if (!sender.isOp()){
                    return null;
                }
            } else {
                if (!sender.hasPermission(this.getPermission())){
                    return null;
                }
            }
        }
        return tabComplete(sender, cmd, label, args);
    }

    public abstract List<String> tabComplete(CommandSender sender, Command cmd, String label, String[] args);
}
