package me.Math0424.Withered.Inventory;

import me.Math0424.Withered.Entities.Mech.MechData;
import me.Math0424.Withered.Event.Events.EventAbstract;
import me.Math0424.Withered.Files.Changeable.Lang;
import me.Math0424.CoreWeapons.Util.ItemStackUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class ItemManager {

    private static List<String> globalCompassText = new ArrayList<>();
    private static final HashMap<Player, List<String>> compassText = new HashMap<>();
    private static final HashMap<Player, Location> compassTarget = new HashMap<>();

    private static List<String> globalWatchText = new ArrayList<>();
    private static final HashMap<Player, List<String>> watchText = new HashMap<>();

    public static void updateCompass(Player p, boolean useGlobal) {
        if (MechData.isInMech(p)) {
            return;
        }
        ItemStack newCompass = InventoryManager.getCompass();
        List<String> text;
        if (useGlobal && EventAbstract.isGlobalEventOngoing) {
            text = globalCompassText;
        } else if (compassText.get(p) != null) {
            text = compassText.get(p);
        } else {
            text = Collections.singletonList(Lang.COMPASS.convert(p, 1));
        }
        newCompass = ItemStackUtil.setLore(newCompass, text);
        if (p.getInventory().getItem(45) != null && ItemStackUtil.isSimilarNameType(p.getInventory().getItem(45), InventoryManager.getCompass())) {
            p.getInventory().setItem(45, newCompass);
        }
        p.getInventory().setItem(9, newCompass);
        if (compassTarget.get(p) != null) {
            p.setCompassTarget(compassTarget.get(p));
        }
    }

    public static void setCompassText(Player p, Location loc, List<String> newText, boolean useGlobal) {
        compassText.put(p, newText);
        compassTarget.put(p, loc);
        p.setCompassTarget(loc);
        updateCompass(p, useGlobal);
    }

    public static void setCompassText(Player p, Location loc, List<String> newText) {
        setCompassText(p, loc, newText, true);
    }

    public static void resetCompass(Player p) {
        compassText.remove(p);
        compassTarget.remove(p);
        p.setCompassTarget(p.getWorld().getSpawnLocation());
        updateCompass(p, true);
    }

    public static void setCompassGlobalText(Location loc, List<String> newText) {
        globalCompassText = newText;
        if (EventAbstract.isGlobalEventOngoing) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                setCompassText(p, loc, newText);
            }
        }
    }

    public static void updateWatch(Player p, int i) {
        if (MechData.isInMech(p)) {
            return;
        }
        ItemStack newWatch = InventoryManager.getWatch();
        List<String> text;
        if (!EventAbstract.isInEvent(p)) {
            text = globalWatchText;
        } else if (watchText.get(p) != null) {
            text = watchText.get(p);
        } else {
            text = Collections.singletonList(Lang.WATCH.convert(p, 1));
        }
        newWatch = ItemStackUtil.setLore(newWatch, text);
        ItemMeta meta = newWatch.getItemMeta();
        meta.setCustomModelData(Math.min(1, i - 1));
        newWatch.setItemMeta(meta);
        p.getInventory().setItem(10, newWatch);
    }

    public static void resetWatch(Player p) {
        watchText.remove(p);
        updateWatch(p, 0);
    }

    public static void setWatchText(Player p, int i, List<String> newText) {
        watchText.put(p, newText);
        updateWatch(p, i);
    }

    public static void setGlobalPlayerWatch(int i, List<String> newText) {
        globalWatchText = newText;
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!EventAbstract.isInLocalEvent(p)) {
                updateWatch(p, i);
            }
        }
    }

}
