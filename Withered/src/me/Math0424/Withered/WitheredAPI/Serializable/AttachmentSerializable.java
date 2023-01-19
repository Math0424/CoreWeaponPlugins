package me.Math0424.Withered.WitheredAPI.Serializable;

import me.Math0424.Withered.Loot.LootItem;
import me.Math0424.Withered.Util.WitheredUtil;
import me.Math0424.CoreWeapons.Guns.Attachments.Attachment;
import me.Math0424.CoreWeapons.Guns.Attachments.AttachmentModifier;
import me.Math0424.CoreWeapons.Util.ItemStackUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class AttachmentSerializable implements ConfigurationSerializable {

    private static final ArrayList<AttachmentSerializable> attachments = new ArrayList<>();

    public Attachment baseClass;

    private String classifier;
    private String name;
    private Material material;
    private int modelId;
    private List<String> lore;
    private final HashMap<AttachmentModifier, Double> modifiers = new HashMap<>();

    //not attachment
    private Integer level;
    private Double chanceOfSpawning;

    public AttachmentSerializable(Map<String, Object> map) {
        try {
            String str = (String) map.get("name");
            this.name = str.replaceAll("&", "§");
            this.material = Material.valueOf((String) map.get("material"));
            this.modelId = (Integer) map.get("modelId");
            this.classifier = (String) map.get("classifier");
            this.lore = (ArrayList<String>) map.get("lore");

            this.chanceOfSpawning = (Double) map.get("chanceOfSpawning");
            this.level = (Integer) map.get("level");

            List<String> mods = (ArrayList<String>) map.get("modifiers");
            if (mods != null) {
                for (String f : mods) {
                    String[] s = f.split(":");
                    modifiers.put(AttachmentModifier.valueOf(s[0].toUpperCase()), Double.valueOf(s[1]));
                }
            }

            WitheredUtil.debug("Successfully loaded attachment " + name);
        } catch (Exception e) {
            e.printStackTrace();
            WitheredUtil.log(Level.SEVERE, "Failed to load attachment " + ((String) map.get("name")).replaceAll("&", "§"));
        }
    }

    public static AttachmentSerializable deserialize(Map<String, Object> map) {
        AttachmentSerializable i = new AttachmentSerializable(map);
        attachments.add(i);
        Attachment a = new Attachment(i.name, i.material, i.modelId, i.classifier, i.modifiers);
        i.baseClass = a;
        LootItem.getLootItems().add(new LootItem(ItemStackUtil.setLore(a.getItemStack(), i.lore), 1, i.level, i.chanceOfSpawning));
        return i;
    }

    public Map<String, Object> serialize() {
        return null;
    }

    public static ArrayList<AttachmentSerializable> getAttachments() {
        return attachments;
    }

    public ItemStack getItemStack() {
        return ItemStackUtil.setLore(baseClass.getItemStack(), lore);
    }

    public static AttachmentSerializable getByName(String name) {
        for (AttachmentSerializable x : attachments) {
            if (ChatColor.stripColor(x.name).equalsIgnoreCase(name)) {
                return x;
            }
        }
        return null;
    }

}
