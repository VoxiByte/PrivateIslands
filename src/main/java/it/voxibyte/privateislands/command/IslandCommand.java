package it.voxibyte.privateislands.command;

import it.voxibyte.privateislands.menu.IslandMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class IslandCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage("Questo è un comando per soli utenti");
            return true;
        }

        Player player = (Player) commandSender;
        IslandMenu.open(player);
        return true;
    }
}
