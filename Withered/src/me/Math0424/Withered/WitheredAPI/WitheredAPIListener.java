package me.Math0424.Withered.WitheredAPI;

import me.Math0424.Withered.Core.PlayerData;
import me.Math0424.Withered.Core.Steady;
import me.Math0424.Withered.Entities.Mech.MechData;
import me.Math0424.Withered.Files.Changeable.BlockConfig;
import me.Math0424.Withered.Files.Changeable.Config;
import me.Math0424.Withered.Files.Changeable.Lang;
import me.Math0424.Withered.Gameplay.CombatLogger;
import me.Math0424.Withered.Teams.Squad;
import me.Math0424.Withered.Util.WitheredUtil;
import me.Math0424.CoreWeapons.DamageHandler.DamageExplainer;
import me.Math0424.CoreWeapons.DamageHandler.DamageUtil;
import me.Math0424.CoreWeapons.Events.ArmorEvents.ArmorFailEvent;
import me.Math0424.CoreWeapons.Events.ArmorEvents.ArmorUseEvent;
import me.Math0424.CoreWeapons.Events.BulletEvents.BulletDestroyBlockEvent;
import me.Math0424.CoreWeapons.Events.BulletEvents.BulletHitBlockEvent;
import me.Math0424.CoreWeapons.Events.BulletEvents.BulletHitEntityEvent;
import me.Math0424.CoreWeapons.Events.BulletEvents.EntityDamagedByAPI;
import me.Math0424.CoreWeapons.Events.DeployableEvents.*;
import me.Math0424.CoreWeapons.Events.GrenadeEvents.GrenadeExplodeEvent;
import me.Math0424.CoreWeapons.Events.GrenadeEvents.GrenadeThrowEvent;
import me.Math0424.CoreWeapons.Events.GunEvents.GunFireEvent;
import me.Math0424.CoreWeapons.Events.GunEvents.GunScopeEvent;
import me.Math0424.CoreWeapons.Grenades.Grenade.GrenadeType;
import me.Math0424.CoreWeapons.Guns.Bullets.Entity.AcidBullet;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class WitheredAPIListener implements Listener {

    //GUNS

    @EventHandler
    public void GunFireEvent(GunFireEvent e) {
        if (WitheredUtil.isInSpawn(e.getShooter().getLocation())) {
            e.setCancelled(true);
            e.getShooter().sendMessage(Lang.ERRORS.convert(e.getShooter()));
        } else {
            PlayerData data = PlayerData.getPlayerData(e.getShooter());
            data.addToBullets(1);
            CombatLogger.putInCombat(e.getShooter());
        }
    }

    @EventHandler
    public void GunScopeEvent(GunScopeEvent e) {
        if (WitheredUtil.isInSpawn(e.getShooter().getLocation())) {
            e.setCancelled(true);
            e.getShooter().sendMessage(Lang.ERRORS.convert(e.getShooter()));
        }
    }

    //BULLETS

    @EventHandler
    public void BulletDestroyBlockEvent(BulletDestroyBlockEvent e) {
        if (!Config.DESTRUCRIBLEWORLD.getBoolVal()) {
            e.setCancelled(true);
            return;
        }
        if (WitheredUtil.isInSpawn(e.getBlock().getLocation()) || !BlockConfig.bulletBreakable.contains(e.getBlock().getType())) {
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
    public void BulletHitBlockEvent(BulletHitBlockEvent e) {
        if (WitheredUtil.isInSpawn(e.getHitBlock().getLocation())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void BulletHitEntityEvent(BulletHitEntityEvent e) {
        if (WitheredUtil.isInSpawn(e.getHitEntity().getLocation())) {
            e.setCancelled(true);
        } else if (e.getHitEntity() instanceof Player) {
            CombatLogger.putInCombat((Player) e.getHitEntity());
        }
    }

    @EventHandler
    public void EntityDamagedByBulletEvent(EntityDamagedByAPI e) {
        if (e.getWitheredDamage().getCause() != DamageExplainer.NUKE) {
            if (e.getWitheredDamage().getDamaged() != e.getWitheredDamage().getDamager() && e.getWitheredDamage().getDamaged() instanceof Player && e.getWitheredDamage().getDamager() instanceof Player) {
                if (Squad.isInSameSquad((Player) e.getWitheredDamage().getDamaged(), (Player) e.getWitheredDamage().getDamager())) {
                    if (!Config.SQUADFRIENDLYFIRE.getBoolVal()) {
                        e.setCancelled(true);
                    } else if (Config.SQUADFRIENDLYFIREPUNISH.getBoolVal()) {
                        Player p = (Player) e.getWitheredDamage().getDamager();
                        p.sendMessage(Lang.SQUADFRIENDLYFIRE.convert(p));
                        DamageUtil.setInstantDamage(e.getWitheredDamage().getDamageAmount(), p, e.getWitheredDamage().getDamaged(), e.getWitheredDamage().getGun(), DamageExplainer.PUNISHMENT);
                        e.getWitheredDamage().setDamageAmount(1.0);
                    }
                }
            }
        }
    }

    //GRENADES

    @EventHandler
    public void GrenadeExplodeEvent(GrenadeExplodeEvent e) {
        if (WitheredUtil.isInSpawn(e.getItem().getLocation())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void GrenadeThrowEvent(GrenadeThrowEvent e) {
        if (WitheredUtil.isInSpawn(e.getThrower().getLocation()) || (!Config.DESTRUCRIBLEWORLD.getBoolVal() && e.getGrenade().getGrenadeType() == GrenadeType.SINGULARITY)) {
            e.setCancelled(true);
            e.getThrower().sendMessage(Lang.ERRORS.convert(e.getThrower()));
        } else {
            PlayerData data = PlayerData.getPlayerData(e.getThrower());
            data.addToGrenadesThrown();
            CombatLogger.putInCombat(e.getThrower());
        }
    }

    //ARMOR

    @EventHandler
    public void ArmorUseEvent(ArmorUseEvent e) {
        if (WitheredUtil.isInSpawn(e.getPlayer().getLocation())) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatColor.RED + Lang.ERRORS.convert(e.getPlayer()));
        }
    }

    @EventHandler
    public void onArmorFailEvent(ArmorFailEvent e) {
        switch (e.getReason()) {
            case BROKE:
                e.getPlayer().sendMessage(ChatColor.RED + Lang.ARMORFAILREASON.convert(e.getPlayer(), 1).replace("{armor}", e.getArmor().getName() + ChatColor.RED));
                break;
            case ALMOSTOUT:
                e.getPlayer().sendMessage(ChatColor.RED + Lang.ARMORFAILREASON.convert(e.getPlayer(), 0).replace("{armor}", e.getArmor().getName() + ChatColor.RED));
                break;
        }
    }

    //DEPLOYABLES

    @EventHandler
    public void deployableUnDeployFailedEvent(DeployableUnDeployFailedEvent e) {
        switch (e.getFailReason()) {
            case FULLINV:
                e.getPlayer().sendMessage(Lang.DEPLOYABLEPICKUPFAIL.convert(e.getPlayer(), 0));
                break;
            case DISABLED:
                e.getPlayer().sendMessage(Lang.DEPLOYABLEPICKUPFAIL.convert(e.getPlayer(), 1));
                break;
            case ATTACKED:
                e.getPlayer().sendMessage(Lang.DEPLOYABLEPICKUPFAIL.convert(e.getPlayer(), 2));
                break;
            case NOTOWNER:
                if (Bukkit.getPlayer(e.getDeployable().getOwner()) != null && Squad.isInSameSquad(e.getPlayer(), Bukkit.getPlayer(e.getDeployable().getOwner()))) {
                    e.setCancelled(true);
                } else {
                    new Steady() {
                        public void moved(Player p) {
                            e.getPlayer().sendMessage(Lang.DEPLOYABLEHACKING.convert(p, 2));
                        }

                        public void ticked(int timeRemaining) {
                            if (timeRemaining % 40 == 0) {
                                e.getPlayer().sendMessage(Lang.DEPLOYABLEHACKING.convert(e.getPlayer(), 0).replace("{time}", String.valueOf(timeRemaining / 20)));
                            }
                        }

                        public void complete(Player p) {
                            e.getDeployable().setOwner(p.getName());
                            p.sendMessage(Lang.DEPLOYABLEHACKING.convert(p, 1));
                        }
                    }.runTimer(e.getPlayer(), (20 * 15));
                }
                break;
        }
    }

    @EventHandler
    public void deployablePlayerKickedOutOfShieldEvent(DeployablePlayerKickedOutOfShieldEvent e) {
        if (!MechData.isInMech(e.getPlayer()) && Bukkit.getPlayer(e.getDeployable().getOwner()) != null &&
                (e.getPlayer().getName().equals(e.getDeployable().getOwner()) || Squad.isInSameSquad(e.getPlayer(), Bukkit.getPlayer(e.getDeployable().getOwner())))) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void deployableDeployFailedEvent(DeployableDeployFailedEvent e) {
        switch (e.getFailReason()) {
            case TOOCLOSE:
                e.getPlayer().sendMessage(Lang.DEPLOYABLEPLACEFAIL.convert(e.getPlayer()));
                break;
        }
    }

    @EventHandler
    public void deployableUnDeployEvent(DeployableUnDeployEvent e) {
        if (MechData.isInMech(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void deployableDeployEvent(DeployableDeployEvent e) {
        if (MechData.isInMech(e.getPlayer())) {
            e.setCancelled(true);
            return;
        }
        if (WitheredUtil.isInSpawn(e.getLocation())) {
            e.getPlayer().sendMessage(Lang.ERRORS.convert(e.getPlayer()));
            e.setCancelled(true);
        }
    }

}
