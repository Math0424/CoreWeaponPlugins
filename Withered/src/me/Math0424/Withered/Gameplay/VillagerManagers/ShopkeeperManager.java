package me.Math0424.Withered.Gameplay.VillagerManagers;

import me.Math0424.Withered.Core.PlayerData;
import me.Math0424.Withered.Currency.CurrencyManager;
import me.Math0424.Withered.Files.Changeable.Config;
import me.Math0424.Withered.Inventory.ShopkeeperInventoryInterpreter;
import me.Math0424.Withered.Loot.ItemSerializable;
import me.Math0424.Withered.Structures.StructureSerializable;
import me.Math0424.Withered.WitheredAPI.Serializable.*;
import me.Math0424.CoreWeapons.Guns.Gun.Gun;
import me.Math0424.CoreWeapons.Guns.QualityEnum;
import me.Math0424.CoreWeapons.Util.InventoryUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ShopkeeperManager {

    public static void inventoryClickEvent(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getClickedInventory() != null && e.getClickedInventory().getType() != InventoryType.PLAYER && ShopkeeperInventoryInterpreter.getShopkeeperInventories().containsKey(e.getView().getTitle())) {
            ItemStack item = e.getCurrentItem();
            e.setCancelled(true);
            if (item != null && item.getType() != Material.AIR && item.hasItemMeta()) {
                PlayerData data = PlayerData.getPlayerData(p);
                int price = getShopPrice(item);
                if (price == -1 || data.getCash() < price) {
                    return;
                }

                String name = ChatColor.stripColor(item.getItemMeta().getDisplayName());

                GunSerializable gu = GunSerializable.getByName(name);
                GrenadeSerializable gr = GrenadeSerializable.getByName(name);
                ItemSerializable i = ItemSerializable.getByName(name);
                ArmorSerializable ar = ArmorSerializable.getByName(name);
                AmmoSerializable am = AmmoSerializable.getByName(name);
                AttachmentSerializable at = AttachmentSerializable.getByName(name);
                DeployableSerializable d = DeployableSerializable.getByName(name);
                StructureSerializable s = StructureSerializable.getByName(name.split(" : ").length == 2 ? name.split(" : ")[1] : "");

                if (gu != null && InventoryUtil.canHaveGun(item, p, Config.MAXPRIMARYGUNS.getIntVal(), Config.MAXSECONDARYGUNS.getIntVal())) {
                    e.setCursor(Gun.getGunItemStack(gu.baseClass, QualityEnum.NEW));
                } else if (i != null) {
                    e.setCursor(i.getItemStack());
                } else if (ar != null) {
                    e.setCursor(ar.baseClass.getItemStack());
                } else if (am != null) {
                    e.setCursor(am.baseClass.getItemStack());
                } else if (d != null) {
                    e.setCursor(d.baseClass.getDeployableItemstack());
                } else if (gr != null) {
                    e.setCursor(gr.baseClass.getItemStack());
                } else if (at != null) {
                    e.setCursor(at.baseClass.getItemStack());
                } else if (s != null) {
                    e.setCursor(s.getItemStack());
                } else {
                    return;
                }

                e.getCursor().setAmount(item.getAmount());
                data.subtractFromCash(price);
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_YES, 1, 1);
                CurrencyManager.updateCurrency(p);

            } else {
                e.setCancelled(true);
            }
        }
    }

    private static int getShopPrice(ItemStack item) {
        if (item != null) {
            try {
                List<String> lore = item.getItemMeta().getLore();
                String[] price = lore.get(0).split(" ");
                return Integer.parseInt(ChatColor.stripColor(price[0]));
            } catch (Exception e) {
                return -1;
            }
        }
        return -1;
    }

}
