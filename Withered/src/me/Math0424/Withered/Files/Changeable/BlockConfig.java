package me.Math0424.Withered.Files.Changeable;

import me.Math0424.Withered.Files.FileLoader;
import me.Math0424.Withered.Util.WitheredUtil;
import me.Math0424.CoreWeapons.CoreWeaponsAPI;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.logging.Level;

public class BlockConfig {

    public static ArrayList<Material> bulletBreakable = new ArrayList<>();
    public static ArrayList<Material> playerBreakable = new ArrayList<>();
    public static ArrayList<Material> roadBlocks = new ArrayList<>();

    public static ArrayList<Material> nonBreakable = new ArrayList<>();

    static {
        nonBreakable.add(Material.ENDER_CHEST);
        nonBreakable.add(Material.BEDROCK);
        nonBreakable.add(Material.OBSIDIAN);
        nonBreakable.add(Material.BARRIER);
        nonBreakable.add(Material.END_GATEWAY);
        nonBreakable.add(Material.END_PORTAL_FRAME);
        nonBreakable.add(Material.END_PORTAL);
        nonBreakable.add(Material.COMMAND_BLOCK);
        nonBreakable.add(Material.REPEATING_COMMAND_BLOCK);
        nonBreakable.add(Material.CHAIN_COMMAND_BLOCK);
        nonBreakable.add(Material.STRUCTURE_BLOCK);
        for (Material m : nonBreakable) {
            CoreWeaponsAPI.getPlugin().addMaterialToUnBreakables(m);
        }
    }

    public static void load() {
        for (Object o : FileLoader.blockConfig.getList("blocks")) {
            if (String.valueOf(o) != null) {
                String s = String.valueOf(o);
                try {
                    Material value = Material.valueOf(s.substring(1, s.indexOf("=")));
                    String[] array = s.substring(s.indexOf("[") + 1, s.indexOf("]")).split(",");
                    if (Boolean.valueOf(array[0].trim())) {
                        roadBlocks.add(value);
                    }
                    if (Boolean.valueOf(array[1].trim())) {
                        playerBreakable.add(value);
                    }
                    if (Boolean.valueOf(array[2].trim())) {
                        bulletBreakable.add(value);
                    }
                } catch (Exception e) {
                    WitheredUtil.log(Level.SEVERE, "Unable to load block in the blockConfig with the value '" + s + "'");
                }
            }
        }
    }

}
