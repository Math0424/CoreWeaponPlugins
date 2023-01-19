package me.Math0424.SurvivalGuns.CoreWeapons;

import me.Math0424.CoreWeapons.Core.Container;
import me.Math0424.CoreWeapons.Grenades.Grenade.Grenade;
import me.Math0424.CoreWeapons.Grenades.Grenade.GrenadeType;
import me.Math0424.CoreWeapons.Util.ItemStackUtil;
import me.Math0424.SurvivalGuns.CoreWeapons.Crafting.Craftable;
import me.Math0424.SurvivalGuns.SurvivalGuns;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;

public class GrenadeSerializable extends Craftable implements ConfigurationSerializable {

    private static final ArrayList<GrenadeSerializable> grenades = new ArrayList<>();

    private int modelId;
    private Grenade baseClass;

    public GrenadeSerializable(Map<String, Object> map) {
        try {
            this.name = ChatColor.translateAlternateColorCodes('&', (String) map.get("name"));
            this.modelId = (int) map.get("modelId");

            shape = (ArrayList<String>) map.get("shape");
            ingredients = (Map<Character, String>) map.get("ingredients");

            //Fixes
            map.put("grenadeType", GrenadeType.valueOf((String) map.get("grenadeType")));

            map.put("throwPitch", ((Double)map.get("throwPitch")).floatValue());
            map.put("throwMultiplier", ((Double)map.get("throwMultiplier")).floatValue());
            map.put("explosiveYield", ((Double)map.get("explosiveYield")).floatValue());
            //End

            baseClass = new Grenade();
            baseClass.deSerialize(map);

            loadRecipe();
            grenades.add(this);
        } catch (Exception e) {
            e.printStackTrace();
            SurvivalGuns.log(Level.SEVERE, "Failed to load grenade " + ((String) map.get("name")).replaceAll("&", "§"));
        }
    }

    public static GrenadeSerializable deserialize(Map<String, Object> map) {
        return new GrenadeSerializable(map);
    }

    public Map<String, Object> serialize() {
        return null;
    }

    public static ArrayList<GrenadeSerializable> getRegistered() {
        return grenades;
    }

    @Override
    public ItemStack getItem() {
        return Container.applyToItem(ItemStackUtil.createItemStack(Material.IRON_NUGGET, name, 1, modelId), baseClass);
    }

}
