package me.Math0424.Withered.Listeners;

import me.Math0424.Withered.Chat.ChatManager;
import me.Math0424.Withered.Chests.ChestManager;
import me.Math0424.Withered.Core.PlayerData;
import me.Math0424.Withered.Currency.CurrencyManager;
import me.Math0424.Withered.Entities.Mech.MechData;
import me.Math0424.Withered.Entities.MobHandler;
import me.Math0424.Withered.Event.EventManager;
import me.Math0424.Withered.Files.Changeable.BlockConfig;
import me.Math0424.Withered.Files.Changeable.Config;
import me.Math0424.Withered.Files.FileLoader;
import me.Math0424.Withered.Gameplay.CombatLogger;
import me.Math0424.Withered.Gameplay.VillagerManagers.BankerManager;
import me.Math0424.Withered.Gameplay.VillagerManagers.GunSmithManager;
import me.Math0424.Withered.Gameplay.VillagerManagers.ShopkeeperManager;
import me.Math0424.Withered.Inventory.InventoryManager;
import me.Math0424.Withered.Signs.SignListener;
import me.Math0424.Withered.Structures.StructureManager;
import me.Math0424.Withered.Teams.ScoreboardManager;
import me.Math0424.Withered.Teams.Squad;
import me.Math0424.Withered.Util.WitheredUtil;
import me.Math0424.Withered.Withered;
import me.Math0424.Withered.Worlds.WorldManager;
import me.Math0424.CoreWeapons.Core.Container;
import me.Math0424.CoreWeapons.DamageHandler.DamageExplainer;
import me.Math0424.CoreWeapons.DamageHandler.DamageUtil;
import me.Math0424.CoreWeapons.Guns.Gun.Gun;
import me.Math0424.CoreWeapons.Util.InventoryUtil;
import me.Math0424.CoreWeapons.Util.ItemStackUtil;
import me.Math0424.CoreWeapons.CoreWeaponsAPI;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class MainListener implements Listener {

    @EventHandler
    public void creatureSpawnEvent(CreatureSpawnEvent e) {
        Entity ent = e.getEntity();
        if (Config.ZOMBIESMODE.getBoolVal()) {
            if (ent.getType().getClass().isAssignableFrom(EntityZombie.class)) {
                e.getEntity().getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(25);
                e.getEntity().setHealth(25);
                e.getEntity().getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.3);
                e.getEntity().getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(1.0);
                e.getEntity().getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(2.0);
            } else if (ent.getType().getClass().isAssignableFrom(EntityMonster.class)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void asyncPlayerPreLoginEvent(AsyncPlayerPreLoginEvent e) {
        if (!CoreWeaponsAPI.getResourcePackLoader().isReady()) {
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_FULL, ChatColor.RED + "Please wait before rejoining!");
        }
    }

    @EventHandler
    public void playerJoinEvent(PlayerJoinEvent e) {
        PlayerData data = PlayerData.getPlayerData(e.getPlayer());
        if (FileLoader.resetLevel != data.getResetLevel()) {
            e.getPlayer().teleport(e.getPlayer().getWorld().getSpawnLocation());
            WitheredUtil.debug("Reset level different for player " + e.getPlayer().getName() + " sent to spawn");
            if (!Config.KEEPPLAYERINVENTORYONRESET.getBoolVal()) {
                data.resetInventory();
            }
        } else {
            if (!WitheredUtil.willSuffocate(data.getLocation())) {
                e.getPlayer().teleport(data.getLocation());
            } else {
                e.getPlayer().teleport(WitheredUtil.getSafeSpawn(data.getLocation().clone().subtract(0, 2, 0)));
            }
        }

        data.setResetLevel(Math.max(0, FileLoader.resetLevel));
        InventoryManager.setInventory(e.getPlayer(), data.getInventory());

        ScoreboardManager.setMainTeam(e.getPlayer());

        InventoryManager.updatePlayerInventory(e.getPlayer());
        e.getPlayer().undiscoverRecipes(Withered.getPlugin().craftable);

        if (e.getPlayer().getWorld().getBlockAt(0, 0, 0).getType() != Material.EMERALD_BLOCK) {
            e.getPlayer().sendMessage(ChatColor.RED +   "You are not using the default world and this world has not been registered!");
            e.getPlayer().sendMessage(ChatColor.GREEN + "To use the default world use the 'witheredsetup world overridewithdefault' command");
            e.getPlayer().sendMessage(ChatColor.RED +   "or register this world with 'witheredsetup world register' command");
        } else {
            if (e.getPlayer().getWorld().getBlockAt(0, 1, 0).getType() != WorldManager.currentMapMaterial) {
                if (e.getPlayer().isOp()) {
                    e.getPlayer().sendMessage(ChatColor.RED + "Map version outdated, your still on " + ChatColor.YELLOW + e.getPlayer().getWorld().getBlockAt(0, 1, 0).getType().toString().toLowerCase());
                    e.getPlayer().sendMessage(ChatColor.RED + "Current map version is " + ChatColor.YELLOW + WorldManager.currentMapMaterial.toString().toLowerCase());
                    e.getPlayer().sendMessage(ChatColor.GREEN + "Override world with new one using the 'witheredsetup world overridewithdefault' command");
                }
            }
        }
    }

    @EventHandler
    public void playerQuitEvent(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        PlayerData data = PlayerData.getPlayerData(p);
        if (p.isInsideVehicle()) {
            p.leaveVehicle();
        }
        if (MechData.getInMech().containsKey(e.getPlayer())) {
            MechData.getInMech().get(e.getPlayer()).removePlayer();
        }
        if (CombatLogger.isInCombat(p)) {
            DamageUtil.setInstantDamage(1000.0, p, p, null, DamageExplainer.COMBATLOG);
        }

        data.updatePlayerInvLocation();

        Squad.removeFromAllSquads(p);
        EventManager.OnPlayerLeave(p);
    }

    @EventHandler
    public void playerChangedWorldEvent(PlayerChangedWorldEvent e) {
        Player p = e.getPlayer();
        if (p.isInsideVehicle()) {
            p.leaveVehicle();
        }
        if (MechData.getInMech().containsKey(e.getPlayer())) {
            MechData.getInMech().get(e.getPlayer()).removePlayer();
        }
        if (CombatLogger.isInCombat(p)) {
            DamageUtil.setInstantDamage(1000.0, p, p, null, DamageExplainer.COMBATLOG);
        }
        EventManager.OnPlayerLeave(p);
        PlayerData.getPlayerData(p).updatePlayerInvLocation();
    }

    @EventHandler
    public void playerInteractEvent(PlayerInteractEvent e) {
        ChestManager.playerInteractEvent(e);
        StructureManager.playerInteractEvent(e);
        SignListener.playerInteractEvent(e);
        if (e.getClickedBlock() != null && Tag.BEDS.isTagged(e.getClickedBlock().getType())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void playerRespawnEvent(PlayerRespawnEvent e) {
        InventoryManager.updatePlayerInventory(e.getPlayer());
        ScoreboardManager.updateHelmet(e.getPlayer());
    }

    @EventHandler
    public void entityPickupItemEvent(EntityPickupItemEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();

            if (MechData.getInMech().containsKey(p)) {
                e.setCancelled(true);
                return;
            }

            if (p.getGameMode() != GameMode.CREATIVE && !InventoryUtil.canHaveGun(e.getItem().getItemStack(), p, Config.MAXPRIMARYGUNS.getIntVal(), Config.MAXSECONDARYGUNS.getIntVal())) {
                e.setCancelled(true);
            }

            if (CurrencyManager.pickupCurrency(p, e.getItem())) {
                e.setCancelled(true);
            }

            for (Entity ent : p.getWorld().getNearbyEntities(p.getLocation(), 3, 3, 3)) {
                if (ent instanceof Item && ent != e.getItem() && !((CraftEntity) ent).getHandle().dead) {
                    if (CurrencyManager.pickupCurrency(p, (Item) ent)) {
                        ((CraftEntity) ent).getHandle().dead = true;
                    }
                }
            }

        }
    }

    @EventHandler
    public void InventoryCloseEvent(InventoryCloseEvent e) {
        GunSmithManager.InventoryCloseEvent(e);
    }

    @EventHandler
    public void InventoryOpenEvent(InventoryOpenEvent e) {
        if (e.getInventory().getType() != InventoryType.PLAYER && e.getInventory().getType() != InventoryType.CHEST) {
            e.setCancelled(true);
        }
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
        if (e.getClickedInventory() != null &&
                (e.getClickedInventory().getType() == InventoryType.PLAYER ||
                        e.getClickedInventory().getType() == InventoryType.CREATIVE ||
                        e.getClickedInventory().getType() == InventoryType.CHEST)
        ) {
            if (e.getClickedInventory().getType() == InventoryType.PLAYER) {

                if (e.getClick() == ClickType.SHIFT_LEFT || e.getClick() == ClickType.SHIFT_RIGHT) {
                    if (p.getOpenInventory().getTopInventory().getType() != InventoryType.CRAFTING
                            && p.getOpenInventory().getTopInventory().getType() != InventoryType.PLAYER
                            && p.getOpenInventory().getTopInventory().getType() != InventoryType.CHEST) {
                        e.setCancelled(true);
                    }
                }

                if (InventoryManager.getActionables().get(e.getSlot()) != null) {
                    InventoryManager.getActionables().get(e.getSlot()).run(e);
                }
                if (InventoryManager.getUnTouchables().get(e.getSlot()) != null) {
                    e.setCancelled(true);
                }

            }
            if (p.getGameMode() != GameMode.CREATIVE && e.getClickedInventory() != e.getWhoClicked().getInventory()
                    && !InventoryUtil.canHaveGun(e.getCurrentItem(), (Player)p, Config.MAXPRIMARYGUNS.getIntVal(), Config.MAXSECONDARYGUNS.getIntVal())) {
                e.setCancelled(true);
                ((Player) p).updateInventory();
            }

            BankerManager.inventoryClickEvent(e);
            GunSmithManager.inventoryClickEvent(e);
            ShopkeeperManager.inventoryClickEvent(e);

            new BukkitRunnable() {
                public void run() {
                    ((Player) e.getWhoClicked()).updateInventory();
                }
            }.runTaskLater(Withered.getPlugin(), 1);

        } else if (e.getClickedInventory() != null) {
            e.setCancelled(true);
        }

    }

    @EventHandler
    public void playerSwapHandItemsEvent(PlayerSwapHandItemsEvent e) {
        if (ItemStackUtil.isSimilarNameType(e.getMainHandItem(), InventoryManager.getCompass())) {
            e.setCancelled(true);
            e.getPlayer().getInventory().setItem(40, ItemStackUtil.createItemStack(Material.AIR));
        }
    }

    @EventHandler
    public void asyncPlayerChatEvent(AsyncPlayerChatEvent e) {
        ChatManager.asyncPlayerChatEvent(e);
    }

    @EventHandler
    public void playerEnterBedEvent(PlayerBedEnterEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void blockBreakEvent(BlockBreakEvent e) {
        Player p = e.getPlayer();
        if (p.getGameMode() != GameMode.CREATIVE && (!BlockConfig.playerBreakable.contains(e.getBlock().getType()) || WitheredUtil.isInSpawn(p.getLocation()))) {
            e.setCancelled(true);
        }

        ItemStack item = p.getInventory().getItemInMainHand();
        if (item.hasItemMeta() && Container.getContainerItem(Gun.class, item) != null) {
            e.setCancelled(true);
        }

        SignListener.blockBreakEvent(e);

    }

    @EventHandler
    public void foodLevelChangeEvent(FoodLevelChangeEvent e) {
        if (WitheredUtil.isInSpawn(e.getEntity().getLocation()) || MechData.getInMech().containsKey(e.getEntity())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void entityDamageByEntityEvent(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            Player attacker = (Player) e.getDamager();
            if (e.getEntity() instanceof Player) {
                if (!attacker.isOp() && WitheredUtil.isInSpawn(e.getEntity().getLocation())) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void signChangeEvent(SignChangeEvent e) {
        SignListener.signChangeEvent(e);
    }

    @EventHandler
    public void blockPlaceEvent(BlockPlaceEvent e) {
        if (!WitheredUtil.isInSpawn(e.getPlayer().getLocation()) && e.getPlayer().getGameMode() != GameMode.CREATIVE) {
            if (e.getBlock().getType() != Material.ENDER_CHEST) {
                if (!BlockConfig.playerBreakable.contains(e.getBlock().getType())) {
                    e.setCancelled(true);
                }
            } else {
                StructureManager.blockPlaceEvent(e);
            }
        } else {
            if (e.getPlayer().getGameMode() == GameMode.CREATIVE && e.getBlock().getType() == Material.ENDER_CHEST) {
                StructureManager.blockPlaceEvent(e);
            } else {
                ChestManager.blockPlaceEvent(e);
            }
        }
    }

    @EventHandler
    public void chunkLoadEvent(ChunkLoadEvent e) {
        MobHandler.chunkLoadEvent(e);
    }

    @EventHandler
    public void playerArmorStandManipulateEvent(PlayerArmorStandManipulateEvent e) {
        if (!e.getPlayer().isOp() && WitheredUtil.isInSpawn(e.getPlayer().getLocation())) {
            e.setCancelled(true);
        }
    }


}
