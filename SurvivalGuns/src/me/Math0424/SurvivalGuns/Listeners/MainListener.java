package me.Math0424.SurvivalGuns.Listeners;

import me.Math0424.CoreWeapons.Core.Container;
import me.Math0424.CoreWeapons.DamageHandler.DamageExplainer;
import me.Math0424.CoreWeapons.DamageHandler.DamageUtil;
import me.Math0424.CoreWeapons.Events.BulletEvents.BulletDestroyBlockEvent;
import me.Math0424.CoreWeapons.Events.GunEvents.GunFireEvent;
import me.Math0424.CoreWeapons.Guns.Ammo.Ammo;
import me.Math0424.CoreWeapons.Guns.Bullets.BulletType;
import me.Math0424.CoreWeapons.Guns.Bullets.Entity.AcidBullet;
import me.Math0424.CoreWeapons.Guns.Gun.Gun;
import me.Math0424.CoreWeapons.Managers.GunManager;
import me.Math0424.CoreWeapons.Util.InventoryUtil;
import me.Math0424.CoreWeapons.Util.MyUtil;
import me.Math0424.SurvivalGuns.CoreWeapons.Crafting.Craftable;
import me.Math0424.SurvivalGuns.Files.Changeable.BlockConfig;
import me.Math0424.SurvivalGuns.Files.Changeable.Config;
import me.Math0424.SurvivalGuns.Gameplay.GunSmithingGUI;
import me.Math0424.SurvivalGuns.SurvivalGuns;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class MainListener implements Listener {

    @EventHandler
    public void playerJoinEvent(PlayerJoinEvent e) {
        if (Config.GIVERECIPIES.getBoolVal()) {
            e.getPlayer().discoverRecipes(Craftable.getCraftables().keySet());
        }
    }

    //BULLETS
    @EventHandler
    public void BulletDestroyBlockEvent(BulletDestroyBlockEvent e) {
        if (!Config.BLOCKDAMAGE.getBoolVal()) {
            e.setCancelled(true);
            return;
        }
        if (e.getGun().getBulletType() == BulletType.NUKE) {
            if (!Config.ENABLENUKES.getBoolVal()) {
                e.setCancelled(true);
            }
            return;
        }
        if (!BlockConfig.bulletBreakable.contains(e.getBlock().getType())) {
            if (e.getBullet() instanceof AcidBullet) {
                AcidBullet a = (AcidBullet) e.getBullet();
                if (BlockConfig.nonBreakable.contains(e.getBlock().getType()) || a.getIteration() >= 2) {
                    e.setCancelled(true);
                }
            } else {
                e.setCancelled(true);
            }
        }

    }


    @EventHandler
    public void InventoryCloseEvent(InventoryCloseEvent e) {
        GunSmithingGUI.InventoryCloseEvent(e);
    }

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
        HumanEntity p = e.getWhoClicked();
        if (e.getClickedInventory() != null) {
            GunSmithingGUI.inventoryClickEvent(e);

            if (p.getGameMode() != GameMode.CREATIVE && e.getClickedInventory() != e.getWhoClicked().getInventory()
                    && !InventoryUtil.canHaveGun(e.getCurrentItem(), (Player)p, Config.MAXPRIMARYGUNS.getIntVal(), Config.MAXSECONDARYGUNS.getIntVal())) {
                e.setCancelled(true);
                ((Player) p).updateInventory();
            }

            if (e.getClick() == ClickType.SHIFT_LEFT || e.getClick() == ClickType.SHIFT_RIGHT) {
                if (e.getCurrentItem() != null && (e.getInventory().getType() == InventoryType.ENCHANTING || e.getInventory().getType() == InventoryType.ANVIL)) {
                    if (Container.getContainerItem(Gun.class, e.getCurrentItem()) != null) {
                        e.setCancelled(true);
                    }
                }
            }

            if (e.getCursor() != null && (e.getClickedInventory().getType() == InventoryType.ENCHANTING || e.getClickedInventory().getType() == InventoryType.ANVIL)) {
                if (Container.getContainerItem(Gun.class, e.getCurrentItem()) != null) {
                    e.setCancelled(true);
                }
            }

        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void gunFireEvent(GunFireEvent e) {
        if (!e.isCancelled()) {
            e.getGun().getObject().setQuality(e.getGun().getObject().getQuality() * .9999f);
        }
    }

    @EventHandler
    public void entityPickupItemEvent(EntityPickupItemEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (p.getGameMode() != GameMode.CREATIVE && !InventoryUtil.canHaveGun(e.getItem().getItemStack(), p, Config.MAXPRIMARYGUNS.getIntVal(), Config.MAXSECONDARYGUNS.getIntVal())) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void prepareItemCraftEvent(PrepareItemCraftEvent e) {
        if (e.getInventory().getRecipe() != null && e.getInventory().getRecipe().getResult().getType() == Material.IRON_INGOT) {
            for (ItemStack i : e.getInventory().getMatrix()) {
                if (i != null && i.hasItemMeta() && i.getItemMeta().hasDisplayName()) {
                    e.getInventory().setResult(new ItemStack(Material.AIR));
                }
            }
        }
    }

    @EventHandler
    public void breakBlockEvent(BlockBreakEvent e) {
        if (e.getPlayer().getItemInUse() != null) {
            Container<Ammo> am = Container.getContainerItem(Ammo.class, e.getPlayer().getItemInUse());
            if (am != null) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void playerInteractEvent(PlayerInteractEvent e) {
        if (e.getClickedBlock() != null) {
            if (e.getClickedBlock().getType() == Material.FLETCHING_TABLE
                    && e.getAction() == Action.RIGHT_CLICK_BLOCK  && !e.getPlayer().isSneaking()) {
                e.getPlayer().openInventory(GunSmithingGUI.createInventory(e.getPlayer()));
                e.setCancelled(true);
            } else if (Tag.SIGNS.isTagged(e.getClickedBlock().getType())) {
                GunManager.addToDisableFire(e.getPlayer(), 3);
            }
        }

    }

    //PHYSICS
    @EventHandler
    public void blockExplodeEvent(EntityExplodeEvent e) {
        if (!Config.BLOCKPHYSICS.getBoolVal()) {
            return;
        }

        ArrayList<Location> blocksList = new ArrayList<>();
        for (Block b : e.blockList()) {
            if (MyUtil.random(2) == 1) {
                toFallingBlock(b, 2);
            }
            blocksList.add(b.getLocation());
        }

        int y = e.getEntity().getLocation().getBlockY();
        //generate the tower above
        new BukkitRunnable() {
            int current = y;
            int air = 15;
            int stone = 0;

            @Override
            public void run() {
                Location check = new Location(e.getEntity().getLocation().getWorld(), e.getEntity().getLocation().getBlockX(), current + 5, e.getEntity().getLocation().getBlockZ());
                Material mat = check.getBlock().getType();
                if (mat != Material.AIR) {
                    air = 15;
                }
                if (mat == Material.STONE
                        || mat == Material.ANDESITE
                        || mat == Material.DIORITE
                        || mat == Material.GRANITE
                        || mat == Material.DIRT) {
                    stone++;
                }

                ArrayList<Location> remove = new ArrayList<>();
                for (int i = 0; i < blocksList.size(); i++) {
                    Location l = blocksList.get(i);
                    Location loc = new Location(l.getWorld(), l.getX() - .5, current, l.getZ() - .5);
                    Block b = loc.getBlock();
                    if (BlockConfig.nonBreakable.contains(b.getType())) {
                        remove.add(l);
                    } else {
                        toFallingBlock(b, 10);
                    }
                }
                blocksList.removeAll(remove);
                current++;
                air--;
                if (current > 255 || air < 0 || stone > 5) {
                    cancel();
                }
            }
        }.runTaskTimer(SurvivalGuns.getPlugin(), 0, 1);
    }

    @EventHandler
    public void entityChangeBlockEvent(EntityChangeBlockEvent e) {
        if (e.getEntity() instanceof FallingBlock) {
            if (!Config.BLOCKPHYSICS.getBoolVal()) {
                return;
            }
            e.getEntity().getLocation().getWorld().playSound(e.getEntity().getLocation(), Sound.BLOCK_WOOD_PLACE, 1, 1);
            for (Entity ent : e.getEntity().getNearbyEntities(1, 1, 1)) {
                if (ent instanceof LivingEntity) {
                    if (ent instanceof Player) {
                        Player p = (Player) ent;
                        DamageUtil.setDamage((double) 5, p, e.getEntity(), DamageExplainer.BLOCKCRUSH, false);
                    } else {
                        ((LivingEntity) ent).damage(10);
                    }
                }
            }
        }
    }

    private void toFallingBlock(Block b, int dampener) {
        if (b.isLiquid()) {
            b.setType(Material.AIR);
            return;
        }
        if (!BlockConfig.nonBreakable.contains(b.getType()) && !b.isEmpty()) {
            float x = (float) ((Math.random() * ((1.0 - -1.0) + 1)) + -1.0) / dampener;
            float y = (float) ((Math.random() * ((1.0 - -1.0) + 1)) + -1.0) / dampener;
            float z = (float) ((Math.random() * ((1.0 - -1.0) + 1)) + -1.0) / dampener;
            FallingBlock fallingBlock = b.getWorld().spawnFallingBlock(b.getLocation(), b.getType().createBlockData());
            fallingBlock.setDropItem(false);
            b.setType(Material.AIR);
            fallingBlock.setVelocity(new Vector(x, y, z));
        }
    }

}
