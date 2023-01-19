package me.Math0424.Withered.Structures;

import me.Math0424.Withered.Core.Steady;
import me.Math0424.Withered.Entities.Mech.MechData;
import me.Math0424.Withered.Files.Changeable.Lang;
import me.Math0424.Withered.Teams.Squad;
import me.Math0424.CoreWeapons.Util.InventoryUtil;
import me.Math0424.CoreWeapons.Util.ItemStackUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class StructureManager {

    public static ItemStack createStructureItemStack(String name) {
        return ItemStackUtil.createItemStack(Material.ENDER_CHEST, ChatColor.LIGHT_PURPLE + Lang.STRUCTURETOOLS.getValue(0).replace(" ", "") + " : " + name);
    }

    public static String getStructureNameFromItemStack(ItemStack item) {
        if (item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            String[] name = meta.getDisplayName().split(" ");
            return name.length == 3 ? name[2] : null;
        }
        return null;
    }

    public static void blockPlaceEvent(BlockPlaceEvent e) {
        Block b = e.getBlockPlaced();
        Player p = e.getPlayer();
        ItemStack item = e.getItemInHand();
        if (b.getType() == Material.ENDER_CHEST) {
            String name = getStructureNameFromItemStack(item);
            Structure s = new Structure(p, b.getLocation(), name);
            if (!s.getConverter().exists()) {
                p.sendMessage(Lang.STRUCTUREINFO.convert(p, 0));
                e.setCancelled(true);
            } else if (s.getConverter().isFloating()) {
                p.sendMessage(Lang.STRUCTUREINFO.convert(p, 1));
                e.setCancelled(true);
            } else if (s.getConverter().isInGround()) {
                p.sendMessage(Lang.STRUCTUREINFO.convert(p, 2));
                e.setCancelled(true);
            } else if (s.getConverter().isReplacingInvalidBlocks()) {
                p.sendMessage(Lang.STRUCTUREINFO.convert(p, 3));
                e.setCancelled(true);
            } else {
                s.build();
            }
        }
    }

    public static void playerInteractEvent(PlayerInteractEvent e) {
        Block b = e.getClickedBlock();
        Player p = e.getPlayer();
        if (b != null && b.getType() == Material.ENDER_CHEST && !MechData.isInMech(e.getPlayer())) {
            Structure s = Structure.getStructure(b.getLocation());
            if (s != null) {
                ItemStack structure = createStructureItemStack(s.getName());
                if (s.getOwner().equals(p.getName())) {
                    if (InventoryUtil.hasAvailableSlot(p, structure)) {
                        s.destroy();
                        p.getInventory().addItem(structure);
                    } else {
                        p.sendMessage(Lang.STRUCTURETOOLS.convert(p, 1));
                    }
                } else {
                    if (Squad.isInSameSquad(p, s.getOwner())) {
                        if (InventoryUtil.hasAvailableSlot(p, structure)) {
                            s.destroy();
                            p.getInventory().addItem(structure);
                        } else {
                            p.sendMessage(Lang.STRUCTURETOOLS.convert(p, 1));
                        }
                    } else {
                        new Steady() {
                            public void moved(Player p) {
                                p.sendMessage(Lang.STRUCTUREHACKING.convert(p, 2));
                            }

                            public void ticked(int timeRemaining) {
                                if (timeRemaining != 0 && timeRemaining % 20 == 0) {
                                    p.sendMessage(Lang.STRUCTUREHACKING.convert(p).replace("{time}", String.valueOf(timeRemaining / 20)));
                                }
                            }

                            public void complete(Player p) {
                                p.sendMessage(Lang.STRUCTUREHACKING.convert(p, 1));
                                s.setOwner(p.getName());
                            }
                        }.runTimer(p, 20 * 20);
                    }
                }
            }
            e.setCancelled(true);
        }
    }
}
