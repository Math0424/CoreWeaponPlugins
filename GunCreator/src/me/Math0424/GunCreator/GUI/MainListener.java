package me.Math0424.GunCreator.GUI;

import me.Math0424.CoreWeapons.Armor.Armor;
import me.Math0424.CoreWeapons.Core.Container;
import me.Math0424.CoreWeapons.Core.SerializableItem;
import me.Math0424.CoreWeapons.Grenades.Grenade.Grenade;
import me.Math0424.CoreWeapons.Guns.Ammo.Ammo;
import me.Math0424.CoreWeapons.Guns.Gun.Gun;
import me.Math0424.CoreWeapons.Util.MyUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public class MainListener implements Listener {

    //make drag events into click events for sake of inventory management
    @EventHandler
    public void inventoryDragEvent(InventoryDragEvent e) {
        for (Integer i : e.getRawSlots()) {
            e.getView().setCursor(e.getOldCursor());
            InventoryClickEvent ev = new InventoryClickEvent(e.getView(), InventoryType.SlotType.CONTAINER, i, ClickType.LEFT, InventoryAction.DROP_ONE_SLOT);
            inventoryClickEvent(ev);
            if (ev.isCancelled()) {
                e.setCancelled(true);
            } else {
                e.getView().setCursor(null);
            }
        }
    }

    @EventHandler
    public void inventoryClickEvent(InventoryClickEvent e) {
        MyGUI.inventoryClickedEvent(e);
    }


    @EventHandler
    private void playerInteractEvent(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (e.getHand() != null && e.getHand().equals(EquipmentSlot.HAND)) {
            ItemStack item = player.getItemInHand();
            if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
                List<String> lore = item.getItemMeta().getLore();
                if (lore.size() == 2) {
                    SerializableItem cont;
                    switch (lore.get(0)) {
                        case "JSONGun":
                            cont = new Gun();
                            break;
                        case "JSONGrenade":
                            cont = new Grenade();
                            break;
                        case "JSONArmor":
                            cont = new Armor();
                            break;
                        case "JSONAmmo":
                            cont = new Ammo();
                            break;
                        default:
                            return;
                    }
                    try {
                        Map<String, Object> map = MyUtil.deserializeMap(lore.get(1));
                        cont.deSerialize(map);
                        Container.applyToItem(player.getItemInHand(), cont);
                    } catch (Exception ignored) {
                        ignored.printStackTrace();
                        player.getItemInHand().setAmount(0);
                        player.sendMessage(ChatColor.RED + "Unable to recover item!");
                    }
                }
            }
        }

    }

}
