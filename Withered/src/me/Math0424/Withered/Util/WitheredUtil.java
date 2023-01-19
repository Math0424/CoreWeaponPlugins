package me.Math0424.Withered.Util;

import me.Math0424.Withered.Files.Changeable.Config;
import me.Math0424.Withered.Files.FileLoader;
import me.Math0424.CoreWeapons.Util.MyUtil;
import net.minecraft.server.v1_16_R3.Item;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.logging.Level;

public class WitheredUtil {

    public static boolean isInSpawn(Location loc) {
        return loc.getWorld().getSpawnLocation().distance(loc) < Config.SPAWNPROTECTDISTANCE.getIntVal();
    }

    public static void log(Level l, String message) {
        Bukkit.getServer().getLogger().log(l, ChatColor.YELLOW + "[Withered] " + ChatColor.RESET + message);
    }

    public static void debug(String message) {
        if (FileLoader.config != null && Config.DEBUGMODE.getBoolVal()) {
            Bukkit.getServer().getLogger().log(Level.INFO, ChatColor.GOLD + "[Withered-Debug] " + ChatColor.RESET + message);
        }
    }

    public static void setMaxStackSize(ItemStack itemStack, int amount) {
        Item item = CraftItemStack.asNMSCopy(itemStack).getItem();
        try {
            Field f = Item.class.getDeclaredField("maxStackSize");
            f.setAccessible(true);
            f.setInt(item, amount);
            WitheredUtil.debug("Set max stack size of item " + item.getName() + " to " + amount);
        } catch (Exception e) {
            WitheredUtil.log(Level.SEVERE, "Error while adjusting stack size of item " + item.getName());
            e.printStackTrace();
        }
    }


    public static Location getSafeSpawn(Location loc) {
        return getSafeSpawn(loc.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }

    public static Location getSafeSpawn(World w, int x, int minHeight, int z) {
        ArrayList<Location> possible = new ArrayList<Location>();
        for (int y = minHeight; y < 254; y++) {
            Location spawnOption = new Location(w, x + .5, y, z + .5);
            if (isSpawnable(spawnOption)) {
                possible.add(spawnOption);
            }
        }
        if (possible.size() == 0) {
            return new Location(w, x, w.getHighestBlockYAt(x, z), z);
        } else if (possible.size() == 1) {
            return possible.get(0);
        }
        return possible.get(MyUtil.random(possible.size() - 1));
    }

    public static boolean isSpawnable(Location loc) {
        Block bottom = loc.clone().subtract(0, 1, 0).getBlock();
        if (bottom.getType() != Material.AIR && bottom.getType() != Material.WATER && bottom.getType() != Material.LAVA) {
            return loc.getBlock().getType() == Material.AIR && loc.clone().add(0, 1, 0).getBlock().getType() == Material.AIR;
        }
        return false;
    }

    public static boolean willSuffocate(Location loc) {
        return loc != null && loc.getBlock().getType().isOccluding() && loc.clone().add(0, 1, 0).getBlock().getType().isOccluding();
    }

}
