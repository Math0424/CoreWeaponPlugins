package me.Math0424.SurvivalGuns.Files.Changeable;

import me.Math0424.CoreWeapons.CoreWeaponsAPI;
import me.Math0424.SurvivalGuns.Files.FileLoader;
import me.Math0424.SurvivalGuns.SurvivalGuns;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.logging.Level;

public class BlockConfig {

    public static ArrayList<Material> bulletBreakable = new ArrayList<>();
    public static ArrayList<Material> nonBreakable = new ArrayList<>();

    static {
        nonBreakable.add(Material.ENDER_CHEST);
        nonBreakable.add(Material.BEDROCK);
        nonBreakable.add(Material.OBSIDIAN);
        nonBreakable.add(Material.BARRIER);
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
                    String bool = s.substring(s.indexOf("[") + 1, s.indexOf("]"));
                    if (Boolean.parseBoolean(bool)) {
                        bulletBreakable.add(value);
                    }
                } catch (Exception e) {
                    SurvivalGuns.log(Level.SEVERE, "Unable to load block in the blockConfig with the value '" + s + "'");
                }
            }
        }
    }

}
