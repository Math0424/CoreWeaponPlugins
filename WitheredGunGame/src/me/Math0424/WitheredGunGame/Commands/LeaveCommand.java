package me.Math0424.WitheredGunGame.Commands;

import me.Math0424.WitheredGunGame.Arenas.Arena;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaveCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String arg2, String[] arg3) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			Arena a = Arena.getArena(p);
			if (a != null) {
				a.removePlayer(p, false);
			} else {
				p.sendMessage(ChatColor.RED + "You are not in a game");
			}
		}
		return false;
	}
	
	
	
	
	
}
