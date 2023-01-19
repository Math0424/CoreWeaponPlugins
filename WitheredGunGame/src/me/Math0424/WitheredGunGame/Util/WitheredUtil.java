package me.Math0424.WitheredGunGame.Util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.logging.Level;

public class WitheredUtil {

	public static void log(Level l, String message) {
		Bukkit.getServer().getLogger().log(l, ChatColor.YELLOW + "[Withered-GunGame] " + ChatColor.RESET + message);
	}

}
