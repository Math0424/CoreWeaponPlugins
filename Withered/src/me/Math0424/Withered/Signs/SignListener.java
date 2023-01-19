package me.Math0424.Withered.Signs;

import me.Math0424.Withered.Files.Changeable.Config;
import me.Math0424.Withered.Files.FileSaver;
import me.Math0424.Withered.Loot.LootItem;
import me.Math0424.Withered.Util.WitheredUtil;
import me.Math0424.Withered.Withered;
import me.Math0424.Withered.WitheredAPI.Serializable.GunSerializable;
import me.Math0424.CoreWeapons.Guns.Gun.Gun;
import me.Math0424.CoreWeapons.Guns.QualityEnum;
import me.Math0424.CoreWeapons.Util.MyUtil;
import org.bukkit.*;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.logging.Level;

public class SignListener {


    static {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (SignData s : SignData.signData) {
                    try {
                        Sign sign = (Sign) s.getSignLoc().getBlock().getState();
                        if (sign == null) {
                            s.getSignLoc().getBlock().setType(Material.OAK_SIGN);
                            sign = (Sign) s.getSignLoc().getBlock().getState();
                        }
                        sign.setLine(0, ChatColor.BLUE + "[Withered]");
                        sign.setLine(1, ChatColor.GREEN + s.getTownName());
                        int count = 0;
                        for (Entity e : s.getSpawnLocation().getWorld().getNearbyEntities(s.getSpawnLocation(), s.getSpread(), s.getSpread(), s.getSpread())) {
                            if (e instanceof Player) {
                                count++;
                            }
                        }
                        sign.setLine(2, ChatColor.GREEN + "Players");
                        sign.setLine(3, ChatColor.BLACK + String.valueOf(count));

                        sign.update();
                    } catch (Exception e) {
                        try {
                            WitheredUtil.log(Level.SEVERE, "Error while finding sign at " + s.getSignLoc().getX() + " " + s.getSignLoc().getY() + " " + s.getSignLoc().getZ());
                            WitheredUtil.log(Level.SEVERE, "Place a sign at that location to fix this error");
                            WitheredUtil.log(Level.SEVERE, "If you want to break it shift left click after placing sign");
                        } catch (Exception f) {
                            WitheredUtil.log(Level.SEVERE, "Error while loading error report for a sign...    lol");
                            f.printStackTrace();
                        }
                    }

                }
            }
        }.runTaskTimer(Withered.getPlugin(), 20 * 5, 20 * 5);
    }

    public static void signChangeEvent(SignChangeEvent e) {
        String[] line = e.getLines();
        if (e.getPlayer().isOp() && line[0].equalsIgnoreCase("[Withered]")) {
            if (line.length == 4) {

                // [Withered]
                // {cityName}
                // {x, minHeight, z}
                // spread

                try {
                    String[] coords = line[2].split(" ");

                    int x = Integer.valueOf(coords[0]);
                    int minHeight = Integer.valueOf(coords[1]);
                    int z = Integer.valueOf(coords[2]);
                    int spread = Integer.valueOf(line[3]);

                    new SignData(e.getBlock().getLocation(), new Location(e.getBlock().getWorld(), x, minHeight, z), spread, line[1]);
                    e.getPlayer().sendMessage(ChatColor.GREEN + "Created sign!");

                    FileSaver.saveSignData();
                } catch (Exception ex) {
                    e.getPlayer().sendMessage(ChatColor.RED + "Sign format invalid!");
                    e.getPlayer().sendMessage(ChatColor.RED + "[] = required  {} = input");
                    e.getPlayer().sendMessage(ChatColor.RED + "[Withered]");
                    e.getPlayer().sendMessage(ChatColor.RED + "{locationName}");
                    e.getPlayer().sendMessage(ChatColor.RED + "{x minHeight z}");
                    e.getPlayer().sendMessage(ChatColor.RED + "{spread}");
                }
            }
        }
    }


    public static void playerInteractEvent(PlayerInteractEvent e) {
        if (e.getClickedBlock() != null && Tag.SIGNS.getValues().contains(e.getClickedBlock().getType())) {
            Location loc = e.getClickedBlock().getLocation();
            for (SignData s : SignData.signData) {
                if (MyUtil.isSameBlockLocation(s.getSignLoc(), loc)) {

                    for (int i = 0; i < 10; i++) {

                        Integer x1 = (int) (s.getSpawnLocation().getX() + MyUtil.randomPosNeg(s.getSpread()));
                        Integer z1 = (int) (s.getSpawnLocation().getZ() + MyUtil.randomPosNeg(s.getSpread()));
                        Location spawn = WitheredUtil.getSafeSpawn(loc.getWorld(), x1, s.getSpawnLocation().getBlockY(), z1);

                        if (spawn != null) {

                            e.getPlayer().getInventory().setHeldItemSlot(4);

                            e.getPlayer().teleport(spawn);
                            e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_GHAST_SCREAM, 1, 1);

                            displayTown(e.getPlayer(), s.getTownName());

                            Gun spawnGun = GunSerializable.getByName(Config.DEFAULTSPAWNGUN.getStrVal()).baseClass;
                            if (e.getPlayer().getInventory().getItem(0) == null && spawnGun != null) {
                                ItemStack gun = Gun.getGunItemStack(spawnGun, QualityEnum.NEW);
                                if (gun != null) {
                                    e.getPlayer().getInventory().setItem(0, gun);
                                }
                            }
                            if (e.getPlayer().getInventory().getItem(8) == null) {
                                ItemStack item = LootItem.getByName(Config.DEFAULTSPAWNITEM.getStrVal()).getItem();
                                if (item != null) {
                                    item.setAmount(LootItem.getByName(Config.DEFAULTSPAWNITEM.getStrVal()).getMaxAmount());
                                    e.getPlayer().getInventory().setItem(8, item);
                                }
                            }
                            return;
                        }
                    }
                    e.getPlayer().sendMessage(ChatColor.RED + "Could not find suitable spawn location, try again!");
                }
            }
        }
    }


    public static void blockBreakEvent(BlockBreakEvent e) {
        Player p = e.getPlayer();
        if (p.isOp() && p.getGameMode() == GameMode.CREATIVE && p.isSneaking()) {
            for (SignData s : SignData.signData) {
                if (MyUtil.isSameBlockLocation(s.getSignLoc(), e.getBlock().getLocation())) {
                    SignData.signData.remove(s);
                    p.sendMessage(ChatColor.GREEN + "Removed sign...");
                    FileSaver.saveSignData();
                    return;
                }
            }
        }
    }

    private static void displayTown(Player p, String name) {
        new BukkitRunnable() {
            final int total = name.length();
            int current = 0;
            String currentString = "";

            @Override
            public void run() {
                currentString = currentString + name.charAt(current);
                p.playSound(p.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 10, 1);
                p.sendMessage("");
                p.sendMessage("");
                p.sendMessage("");
                p.sendMessage("");
                p.sendMessage("");
                p.sendMessage("                         " + ChatColor.GRAY + currentString);
                p.sendMessage("");
                p.sendMessage("");
                p.sendMessage("");
                p.sendMessage("");
                current++;
                if (total <= current) {
                    cancel();
                    return;
                }
            }
        }.runTaskTimer(Withered.getPlugin(), 40, 6);
    }

}
