package me.Math0424.SurvivalGuns.CoreWeapons;

import me.Math0424.CoreWeapons.Core.Container;
import me.Math0424.CoreWeapons.Guns.Attachments.Attachment;
import me.Math0424.CoreWeapons.Util.ItemStackUtil;
import me.Math0424.SurvivalGuns.CoreWeapons.Crafting.Craftable;
import me.Math0424.SurvivalGuns.SurvivalGuns;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class AttachmentSerializable extends Craftable implements ConfigurationSerializable {

    private static final ArrayList<AttachmentSerializable> attachments = new ArrayList<>();

    private int modelId;
    private Attachment baseClass;

    private List<String> lore;

    public AttachmentSerializable(Map<String, Object> map) {
        try {
            this.name = ChatColor.translateAlternateColorCodes('&', (String) map.get("name"));
            this.modelId = (int) map.get("modelID");

            this.lore = (ArrayList<String>) map.get("lore");

            shape = (ArrayList<String>) map.get("shape");
            ingredients = (Map<Character, String>) map.get("ingredients");

            baseClass = new Attachment();
            baseClass.deSerialize(map);

            loadRecipe();
            attachments.add(this);
        } catch (Exception e) {
            e.printStackTrace();
            SurvivalGuns.log(Level.SEVERE, "Failed to load attachment " + ((String) map.get("name")).replaceAll("&", "§"));
        }
    }

    public static AttachmentSerializable deserialize(Map<String, Object> map) {
        return new AttachmentSerializable(map);
    }

    public Map<String, Object> serialize() {
        return null;
    }

    public static AttachmentSerializable getByName(String name) {
        for(AttachmentSerializable s : attachments) {
            if (ChatColor.stripColor(s.baseClass.getName()).equals(ChatColor.stripColor(name))) {
                return s;
            }
        }
        return null;
    }

    public static ArrayList<AttachmentSerializable> getRegistered() {
        return attachments;
    }

    @Override
    public ItemStack getItem() {
        return Container.applyToItem(ItemStackUtil.createItemStack(Material.IRON_NUGGET, name, lore, 1, modelId), baseClass);
    }

}
