package me.Math0424.SurvivalGuns.Gameplay;

import me.Math0424.CoreWeapons.Core.Container;
import me.Math0424.CoreWeapons.Guns.Attachments.Attachment;
import me.Math0424.CoreWeapons.Guns.Gun.Gun;
import me.Math0424.CoreWeapons.Util.ItemStackUtil;
import me.Math0424.SurvivalGuns.CoreWeapons.AttachmentSerializable;
import me.Math0424.SurvivalGuns.SurvivalGuns;
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
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class GunSmithingGUI {

    private final static int gunSlot = 11, slot1 = 13, slot2 = 14, slot3 = 15;
    private final static String name = "GunAttachmentMenu";

    public static Inventory createInventory(Player p) {
        Inventory inv = Bukkit.createInventory(null, 27, name);
        for (int i = 0; i < inv.getSize(); i++) {
            inv.setItem(i, ItemStackUtil.createItemStack(Material.GRAY_STAINED_GLASS_PANE, " "));
        }

        inv.setItem(gunSlot, new ItemStack(Material.AIR));

        inv.setItem(slot1, ItemStackUtil.createItemStack(Material.BLACK_STAINED_GLASS_PANE));
        inv.setItem(slot2, ItemStackUtil.createItemStack(Material.BLACK_STAINED_GLASS_PANE));
        inv.setItem(slot3, ItemStackUtil.createItemStack(Material.BLACK_STAINED_GLASS_PANE));

        return inv;
    }

    public static void InventoryCloseEvent(InventoryCloseEvent e) {
        if (e.getView().getTitle().equals(name)) {
            Inventory inv = e.getInventory();
            if (inv.getItem(gunSlot) != null) {
                Container<Gun> cont = Container.getContainerItem(Gun.class, inv.getItem(gunSlot));
                if (cont != null) {
                    cont.getObject().clearAttachments();
                    cont.getObject().addAttachment(getAttachment(inv.getItem(slot1), inv.getItem(slot2), inv.getItem(slot3)));
                    cont.updateItemMapping();
                    e.getPlayer().getWorld().dropItem(e.getPlayer().getLocation(), cont.getItemStack());
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
                    Container<Gun> cout = Container.getContainerItem(Gun.class, cursor);
                    if (e.getSlot() == gunSlot && cout != null) {
                        inv.setItem(slot1, ItemStackUtil.createItemStack(Material.AIR));
                        inv.setItem(slot2, ItemStackUtil.createItemStack(Material.AIR));
                        inv.setItem(slot3, ItemStackUtil.createItemStack(Material.AIR));

                        List<Attachment> attachments = cout.getObject().getAttachments();
                        if (attachments.size() >= 1) {
                            AttachmentSerializable att = AttachmentSerializable.getByName(attachments.get(0).getName());
                            if (att != null)
                                inv.setItem(slot1, att.getItem());
                        }
                        if (attachments.size() >= 2) {
                            AttachmentSerializable att = AttachmentSerializable.getByName(attachments.get(1).getName());
                            if (att != null)
                                inv.setItem(slot2, att.getItem());
                        }
                        if (attachments.size() >= 3) {
                            AttachmentSerializable att = AttachmentSerializable.getByName(attachments.get(2).getName());
                            if (att != null)
                                inv.setItem(slot3, att.getItem());
                        }

                        p.playSound(p.getLocation(), Sound.ENTITY_HORSE_ARMOR, 1, 1);

                    } else if ((e.getSlot() == slot1 || e.getSlot() == slot2 || e.getSlot() == slot3) && cursor.hasItemMeta() && cursor.getAmount() <= 1) {
                        Container<Gun> gCont = Container.getContainerItem(Gun.class, inv.getItem(gunSlot));
                        Container<Attachment> aCont = Container.getContainerItem(Attachment.class, cursor);
                        if (gCont != null && aCont != null) {
                            Gun g = gCont.getObject();
                            if (g.canAddAttachment(aCont.getObject())) {
                                g.clearAttachments();
                                g.addAttachment(getAttachment(inv.getItem(slot1), inv.getItem(slot2), inv.getItem(slot3), cursor));
                                aCont.updateItemMapping();
                            } else {
                                e.setCancelled(true);
                            }
                        } else {
                            e.setCancelled(true);
                        }
                    } else {
                        e.setCancelled(true);
                    }
                } else if(current.getType() != Material.AIR && cursor.getType() == Material.AIR) {
                    //took an item
                    if (e.getSlot() == gunSlot) {
                        Container<Gun> cont = Container.getContainerItem(Gun.class, current);
                        if (cont != null) {
                            cont.getObject().clearAttachments();
                            cont.getObject().addAttachment(getAttachment(inv.getItem(slot1), inv.getItem(slot2), inv.getItem(slot3)));
                            cont.updateItemMapping();

                            p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_USE, 1, 1);

                            inv.setItem(slot1, ItemStackUtil.createItemStack(Material.BLACK_STAINED_GLASS_PANE));
                            inv.setItem(slot2, ItemStackUtil.createItemStack(Material.BLACK_STAINED_GLASS_PANE));
                            inv.setItem(slot3, ItemStackUtil.createItemStack(Material.BLACK_STAINED_GLASS_PANE));
                        }
                    } else {
                        Container<Gun> gunItem = Container.getContainerItem(Gun.class, inv.getItem(gunSlot));
                        Container<Attachment> attachment = Container.getContainerItem(Attachment.class, current);
                        if (gunItem != null && attachment != null) {
                            gunItem.getObject().removeAttachment(attachment.getObject());
                        } else {
                            e.setCancelled(true);
                        }
                    }
                } else {
                    e.setCancelled(true);
                }
            }

            new BukkitRunnable() {
                public void run() {
                    p.updateInventory();
                }
            }.runTaskLater(SurvivalGuns.getPlugin(), 1);

        }
    }

    private static ArrayList<Attachment> getAttachment(ItemStack... attachment) {
        ArrayList<Attachment> attachments = new ArrayList<>();
        for (ItemStack item : attachment) {
            if (item != null && item.hasItemMeta()) {
                Container<Attachment> attachment1 = Container.getContainerItem(Attachment.class, item);
                if (attachment1 != null) {
                    attachments.add(attachment1.getObject());
                }
            }
        }
        return attachments;
    }

}
