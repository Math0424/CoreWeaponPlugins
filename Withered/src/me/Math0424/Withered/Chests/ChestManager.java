package me.Math0424.Withered.Chests;

import me.Math0424.Withered.Commands.CommandHelper;
import me.Math0424.Withered.Files.Changeable.Config;
import me.Math0424.Withered.Files.FileSaver;
import me.Math0424.Withered.Loot.LootItem;
import me.Math0424.Withered.Withered;
import me.Math0424.CoreWeapons.Util.MyUtil;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collections;
import java.util.HashMap;

public class ChestManager {

    public static HashMap<Player, Integer> placeLevel = new HashMap<>();

    static {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!LevelChest.chests.isEmpty()) {
                    Collections.shuffle(LootItem.lootItems);
                    LevelChest c = LevelChest.chests.get(MyUtil.random(LevelChest.chests.size()));
                    if (MyUtil.isChunkLoaded(c.getLocation()) && !c.getLocation().subtract(0, 1, 0).getBlock().isEmpty()) {
                        c.populate();
                    }
                }
            }
        }.runTaskTimer(Withered.getPlugin(), Config.CHESTPOPULATIONRATE.getIntVal(), Config.CHESTPOPULATIONRATE.getIntVal());
        repopulateAllChests();
    }

    public static void playerInteractEvent(PlayerInteractEvent e) {
        Block b = e.getClickedBlock();
        if (b != null && b.getType() == Material.CHEST) {
            LevelChest c = LevelChest.getLevelChest(b.getLocation());
            if (e.getAction() == Action.LEFT_CLICK_BLOCK && canUseChest(e.getPlayer())) {
                LevelChest.removeChest(c.getLocation());
                e.getPlayer().sendMessage(ChatColor.RED + "Removed level chest lvl " + c.getLevel());
                FileSaver.saveChests();
            } else if (c != null && Config.CHESTPOP.getBoolVal() && !c.getForceNonPop()) {
                b.breakNaturally();
                e.setCancelled(true);
            }
        }
    }

    public static void blockPlaceEvent(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        if (canUseChest(p) && e.getBlockPlaced().getType() == Material.CHEST) {
            if (LevelChest.getLevelChest(e.getBlock().getLocation()) != null) {
                p.sendMessage(ChatColor.RED + "Chest already placed here!");
                return;
            }
            LevelChest l;
            if (placeLevel.containsKey(p)) {
                l = new LevelChest(e.getBlock().getLocation(), placeLevel.get(p));
            } else {
                l = new LevelChest(e.getBlock().getLocation(), e.getItemInHand().getAmount());
            }
            l.register();
            p.sendMessage(ChatColor.GREEN + "Placed chest level " + l.getLevel());
            p.sendMessage("Type /witheredsetup chestlevel {level}");
            FileSaver.saveChests();
        }
    }

    private static boolean canUseChest(Player p) {
        return (CommandHelper.hasPermission(p, "withered.commands.setup") || p.isOp()) && p.getGameMode() == GameMode.CREATIVE && p.isSneaking();
    }


    public static void repopulateAllChests() {
        if (!LevelChest.chests.isEmpty()) {
            for (LevelChest c : LevelChest.chests) {
                c.populate();
            }
        }
    }

}
