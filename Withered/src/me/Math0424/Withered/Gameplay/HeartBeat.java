package me.Math0424.Withered.Gameplay;

import me.Math0424.Withered.Entities.MobHandler;
import me.Math0424.Withered.Event.Events.EventAbstract;
import me.Math0424.Withered.Files.Changeable.Config;
import me.Math0424.Withered.Listeners.MechListeners;
import me.Math0424.Withered.Util.WitheredUtil;
import me.Math0424.Withered.Withered;
import me.Math0424.CoreWeapons.DamageHandler.DamageExplainer;
import me.Math0424.CoreWeapons.DamageHandler.DamageUtil;
import me.Math0424.CoreWeapons.Data.PlayerData;
import me.Math0424.CoreWeapons.Util.MyUtil;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.logging.Level;

public class HeartBeat {

    public HeartBeat() {
        new BukkitRunnable() {
            long ticks = 0;

            @Override
            public void run() {
                ticks++;
                if (ticks % 2 == 0) {
                    MechListeners.mechSuitTick();
                    MobHandler.loadedThisTick.clear();
                }

                for (EventAbstract e : EventAbstract.ongoingEvents) {
                    e.tick();
                }

                for (Player p : Bukkit.getOnlinePlayers()) {

                    if (ticks % 20 == 0 && Config.DEATHBYWATER.getBoolVal() && (p.getLocation().getBlock().isLiquid() || (p.getLocation().getBlock().getBlockData() instanceof Waterlogged
                            && ((Waterlogged) p.getLocation().getBlock().getBlockData()).isWaterlogged()))
                            && (p.getVehicle() == null || !(p.getVehicle().getType() == EntityType.BOAT))) {
                        DamageUtil.setDamage(4.0, p, null, null, DamageExplainer.WATER);
                    }

                    if (p.getInventory().contains(Material.ENDER_CHEST) || p.getInventory().getItemInOffHand().getType() == Material.ENDER_CHEST) {
                        p.addPotionEffect((new PotionEffect(PotionEffectType.SLOW, 10, 2, false, false, false)));
                    }

                    if (ticks % (20 * 60) == 0) {
                        me.Math0424.Withered.Core.PlayerData.getPlayerData(p).updatePlayerInvLocation();
                    }

                    if (ticks % 10 == 0 && p.hasPotionEffect(PotionEffectType.GLOWING)) {
                        for (Player p1 : p.getWorld().getPlayers()) {
                            double dist = p.getLocation().distance(p1.getLocation());
                            if (p != p1 && dist <= 200) {
                                MyUtil.drawColoredLine(p.getLocation(), p1.getLocation(), Color.WHITE, 1, dist / 200, p1);
                            }
                        }
                    }
                }

                if (ticks % (20 * 60 * 5) == 0) {
                    PlayerData.saveAllPlayerData();
                    WitheredUtil.log(Level.INFO, "Saved playerData.");
                }

            }
        }.runTaskTimer(Withered.getPlugin(), 1, 1);

    }

}
