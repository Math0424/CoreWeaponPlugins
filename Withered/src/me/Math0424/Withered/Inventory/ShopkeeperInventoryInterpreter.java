package me.Math0424.Withered.Inventory;

import me.Math0424.Withered.Files.Changeable.Config;
import me.Math0424.Withered.Files.FileLoader;
import me.Math0424.Withered.Loot.ItemSerializable;
import me.Math0424.Withered.Structures.StructureSerializable;
import me.Math0424.Withered.Util.WitheredUtil;
import me.Math0424.Withered.WitheredAPI.Serializable.*;
import me.Math0424.CoreWeapons.Util.ItemStackUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

public class ShopkeeperInventoryInterpreter {

    private static final HashMap<String, Inventory> shopkeeperInventories = new HashMap<>();

    //[inventoryName]::[slot]::[type]::[name]::[price]::[amount]

    static {
        List<String> lines = FileLoader.shopkeeperConfig.getStringList("items");
        for (String s : lines) {
            String[] args = s.split("::");
            if (args.length == 6) {
                try {
                    int slot = Integer.parseInt(args[1]);
                    int price = Integer.parseInt(args[4]);
                    int amount = Math.min(64, Integer.parseInt(args[5]));
                    switch (args[2].toLowerCase()) {
                        case "gun":
                            putInShopkeeperInv(args[0], GunSerializable.getByName(args[3]).baseClass.getItemStack(), slot, price, 1);
                            break;
                        case "item":
                            putInShopkeeperInv(args[0], ItemSerializable.getByName(args[3]).getItemStack(), slot, price, amount);
                            break;
                        case "armor":
                            putInShopkeeperInv(args[0], ArmorSerializable.getByName(args[3]).baseClass.getItemStack(), slot, price, 1);
                            break;
                        case "ammo":
                            putInShopkeeperInv(args[0], AmmoSerializable.getByName(args[3]).baseClass.getItemStack(), slot, price, amount);
                            break;
                        case "deployable":
                            putInShopkeeperInv(args[0], DeployableSerializable.getByName(args[3]).baseClass.getDeployableItemstack(), slot, price, 1);
                            break;
                        case "structure":
                            putInShopkeeperInv(args[0], StructureSerializable.getByName(args[3]).getItemStack(), slot, price, amount);
                            break;
                        case "attachment":
                            putInShopkeeperInv(args[0], AttachmentSerializable.getByName(args[3]).getItemStack(), slot, price, amount);
                            break;
                        case "grenade":
                            putInShopkeeperInv(args[0], GrenadeSerializable.getByName(args[3]).baseClass.getItemStack(), slot, price, amount);
                            break;
                    }
                    WitheredUtil.debug("Loaded shopkeeper item " + s);
                } catch (Exception e) {
                    e.printStackTrace();
                    WitheredUtil.log(Level.SEVERE, "Failed to parse shopkeeper item " + s);
                }
            } else {
                WitheredUtil.log(Level.SEVERE, "Unable to parse shopkeeper item as format is invalid " + s);
            }
        }
    }

    public static void putInShopkeeperInv(String name, ItemStack item, int slot, int price, int amount) {
        if (shopkeeperInventories.get(name) == null) {
            shopkeeperInventories.put(name, Bukkit.createInventory(null, 54, name));
            for (int i = 0; i < 54; ++i) {
                shopkeeperInventories.get(name).setItem(i, ItemStackUtil.createItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE, " "));
            }
        }
        ItemStack newItem = item.clone();
        ItemMeta meta = newItem.getItemMeta();
        List<String> lore = meta.getLore() == null ? new ArrayList<>() : meta.getLore();
        lore.add(0, ChatColor.UNDERLINE + "" + ChatColor.DARK_GREEN + "" + price + " " + Config.CURRENCYNAME.getStrVal());
        meta.setLore(lore);
        newItem.setItemMeta(meta);
        newItem.setAmount(amount);

        shopkeeperInventories.get(name).setItem(slot, newItem);

    }

    public static Inventory getSafeShopkeeperInventory(String s) {
        return shopkeeperInventories.get(s) == null ? Bukkit.createInventory(null, 54, "InvalidInventory") : shopkeeperInventories.get(s);
    }

    public static HashMap<String, Inventory> getShopkeeperInventories() {
        return shopkeeperInventories;
    }
}
