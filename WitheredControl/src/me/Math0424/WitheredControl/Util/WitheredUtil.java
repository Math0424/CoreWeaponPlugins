package me.Math0424.WitheredControl.Util;

import me.Math0424.WitheredControl.Config;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Random;

public class WitheredUtil {
	
	private static Random rand = new Random();
	
	public static int random(int i) {
		return rand.nextInt(i);
	}
	
	public static int randomPosNeg(int num) {
		return random(num + 1 + num) - num; 
	}
	
	public static boolean isSameLocation(Location loc1, Location loc2) {
		if (loc1.getX() == loc2.getX() && loc1.getY() == loc2.getY() && loc1.getZ() == loc2.getZ()) {
			return true;
		}
		return false;
	}
	
	public static boolean isSameBlockLocation(Location loc1, Location loc2) {
		if (loc1.getBlockX() == loc2.getBlockX() && loc1.getBlockY() == loc2.getBlockY() && loc1.getBlockZ() == loc2.getBlockZ()) {
			return true;
		}
		return false;
	}

	public static ItemStack createItemStack(String name, Material material) {
		
		ItemStack item = new ItemStack(material);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(name);
		item.setItemMeta(itemMeta);
		
		return item;
	}
	
	public static void info(String s) {
		Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "[WitheredControl] " + s);
	}
	
	public static void debug(String s) {
		if (Config.DEBUG.getBoolVal()) {
			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.WHITE + "[WitheredControl-Debug] " + s);
		}
	}

}
