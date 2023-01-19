package me.Math0424.Withered.Gameplay.VillagerManagers;

import me.Math0424.Withered.Files.Changeable.Lang;
import me.Math0424.CoreWeapons.Core.Container;
import me.Math0424.CoreWeapons.Guns.Attachments.Attachment;
import me.Math0424.CoreWeapons.Guns.Gun.Gun;
import me.Math0424.CoreWeapons.Util.ItemStackUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class GunSmithManager {

    private final static int gunSlot = 11, slot1 = 13, slot2 = 14, slot3 = 15;
    private final static String name = "GunAttachmentMenu";

    public static Inventory createInventory(Player p) {
        Inventory inv = Bukkit.createInventory(null, 27, name);
        for (int i = 0; i < inv.getSize(); i++) {
            inv.setItem(i, ItemStackUtil.createItemStack(Material.GRAY_STAINED_GLASS_PANE, " "));
        }

        inv.setItem(gunSlot, new ItemStack(Material.AIR));

        inv.setItem(slot1, ItemStackUtil.createItemStack(Material.BLACK_STAINED_GLASS_PANE, Lang.GUNSMITHINV.convert(p)));
        inv.setItem(slot2, ItemStackUtil.createItemStack(Material.BLACK_STAINED_GLASS_PANE, Lang.GUNSMITHINV.convert(p)));
        inv.setItem(slot3, ItemStackUtil.createItemStack(Material.BLACK_STAINED_GLASS_PANE, Lang.GUNSMITHINV.convert(p)));

        return inv;
    }

    public static void InventoryCloseEvent(InventoryCloseEvent e) {
        if (e.getView().getTitle().equals(name)) {
            Inventory inv = e.getInventory();
            if (inv.getItem(gunSlot) != null) {
                Gun gunItem = Container.getContainerItem(Gun.class, inv.getItem(gunSlot));
                if (gunItem != null) {
                    gunItem.clearAttachments();
                    gunItem.addAttachments(getAttachment(inv.getItem(slot1), inv.getItem(slot2), inv.getItem(slot3)));
                    e.getPlayer().getWorld().dropItem(e.getPlayer().getLocation(), inv.getItem(gunSlot));
                }
            }
        }
    }

    public static void inventoryClickEvent(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getClickedInventory() != null && e.getView().getTitle().equals(name)) {
            if (e.getClick() == ClickType.SHIFT_LEFT || e.getClick() == ClickType.MIDDLE) {
                e.setCancelled(true);
                return;
            }
            if (e.getClickedInventory().getType() != InventoryType.PLAYER) {

                if (e.getSlot() != gunSlot && e.getSlot() != slot1 && e.getSlot() != slot2 && e.getSlot() != slot3) {
                    e.setCancelled(true);
                    return;
                }

                Inventory inv = e.getInventory();
                ItemStack current = e.getCurrentItem();
                ItemStack cursor = e.getCursor();

                if (current == null || current.getType() == Material.AIR) {
                    //placed an item
                    Gun gunItem = Container.getContainerItem(Gun.class, cursor);
                    if (e.getSlot() == gunSlot && gunItem != null) {
                        inv.setItem(slot1, ItemStackUtil.createItemStack(Material.AIR));
                        inv.setItem(slot2, ItemStackUtil.createItemStack(Material.AIR));
                        inv.setItem(slot3, ItemStackUtil.createItemStack(Material.AIR));

                        List<Attachment> attachments = gunItem.getAttachments();
                        if (attachments.size() >= 1) {
                            inv.setItem(slot1, attachments.get(0).getItemStack());
                        }
                        if (attachments.size() >= 2) {
                            inv.setItem(slot2, attachments.get(1).getItemStack());
                        }
                        if (attachments.size() >= 3) {
                            inv.setItem(slot3, attachments.get(2).getItemStack());
                        }

                        p.playSound(p.getLocation(), Sound.ENTITY_HORSE_ARMOR, 1, 1);

                    } else if ((e.getSlot() == slot1 || e.getSlot() == slot2 || e.getSlot() == slot3) && cursor.hasItemMeta() && cursor.getAmount() <= 1) {
                        Gun newGunItem = Container.getContainerItem(Gun.class, inv.getItem(gunSlot));
                        Attachment attachment = Container.getContainerItem(Attachment.class, cursor);
                        if (newGunItem != null && attachment != null) {
                            if (!newGunItem.isConflictingAttachment(attachment) && !newGunItem.getIllegalAttachments().contains(attachment.getClassifier())) {
                                newGunItem.clearAttachments();
                                newGunItem.addAttachments(getAttachment(inv.getItem(slot1), inv.getItem(slot2), inv.getItem(slot3), cursor));
                            } else {
                                e.setCancelled(true);
                            }
                        } else {
                            e.setCancelled(true);
                        }
                    } else {
                        e.setCancelled(true);
                    }
                } else if (current.getType() != Material.AIR && cursor.getType() == Material.AIR) {
                    //took an item
                    if (e.getSlot() == gunSlot) {
                        Gun gunItem = Container.getContainerItem(Gun.class, current);
                        if (gunItem != null) {
                            gunItem.clearAttachments();
                            gunItem.addAttachments(getAttachment(inv.getItem(slot1), inv.getItem(slot2), inv.getItem(slot3)));

                            p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_USE, 1, 1);

                            inv.setItem(slot1, ItemStackUtil.createItemStack(Material.BLACK_STAINED_GLASS_PANE, Lang.GUNSMITHINV.convert(p)));
                            inv.setItem(slot2, ItemStackUtil.createItemStack(Material.BLACK_STAINED_GLASS_PANE, Lang.GUNSMITHINV.convert(p)));
                            inv.setItem(slot3, ItemStackUtil.createItemStack(Material.BLACK_STAINED_GLASS_PANE, Lang.GUNSMITHINV.convert(p)));
                        }
                    } else {
                        Gun gunItem = Container.getContainerItem(Gun.class, inv.getItem(gunSlot));
                        Attachment attachment = Container.getContainerItem(Attachment.class, current);
                        if (gunItem != null && attachment != null) {
                            gunItem.removeAttachment(attachment);
                        } else {
                            e.setCancelled(true);
                        }
                    }
                } else {
                    e.setCancelled(true);
                }

            }
        }
    }

    private static ArrayList<Attachment> getAttachment(ItemStack... attachment) {
        ArrayList<Attachment> attachments = new ArrayList<>();
        for (ItemStack item : attachment) {
            if (item != null && item.hasItemMeta()) {
                Attachment attachment1 = Container.getContainerItem(Attachment.class, item);
                if (attachment1 != null) {
                    attachments.add(attachment1);
                }
            }
        }
        return attachments;
    }


}
