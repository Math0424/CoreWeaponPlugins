package me.Math0424.CoreHooks.GriefPrevention;

import me.Math0424.CoreWeapons.Events.ArmorEvents.ArmorUseEvent;
import me.Math0424.CoreWeapons.Events.BulletEvents.BulletDestroyBlockEvent;
import me.Math0424.CoreWeapons.Events.BulletEvents.BulletHitBlockEvent;
import me.Math0424.CoreWeapons.Events.BulletEvents.BulletHitEntityEvent;
import me.Math0424.CoreWeapons.Events.BulletEvents.EntityDamagedByAPI;
import me.Math0424.CoreWeapons.Events.DeployableEvents.DeployableDeployEvent;
import me.Math0424.CoreWeapons.Events.GrenadeEvents.GrenadeExplodeEvent;
import me.Math0424.CoreWeapons.Events.GrenadeEvents.GrenadeThrowEvent;
import me.Math0424.CoreWeapons.Events.GunEvents.GunFireEvent;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class GriefPreventionHook implements Listener {

    //disabled events
    @EventHandler
    public void GunFireEvent(GunFireEvent e) {
        Claim claim = GriefPrevention.instance.dataStore.getClaimAt(e.getShooter().getLocation(), true, null);
        if (claim.allowEdit(e.getShooter()) != null) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void deployableDeployEvent(DeployableDeployEvent e) {
        Claim claim = GriefPrevention.instance.dataStore.getClaimAt(e.getPlayer().getLocation(), true, null);
        if (claim.allowEdit(e.getPlayer()) != null) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void armorUseEvent(ArmorUseEvent e) {
        Claim claim = GriefPrevention.instance.dataStore.getClaimAt(e.getPlayer().getLocation(), true, null);
        if (claim.allowAccess(e.getPlayer()) != null) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void bulletDestroyBlockEvent(BulletDestroyBlockEvent e) {
        if (e.getShooter() instanceof Player p) {
            Claim claim = GriefPrevention.instance.dataStore.getClaimAt(e.getShooter().getLocation(), true, null);
            if (claim.allowEdit(p) != null) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void bulletHitBlockEvent(BulletHitBlockEvent e) {
        if (e.getShooter() instanceof Player p) {
            Claim claim = GriefPrevention.instance.dataStore.getClaimAt(e.getShooter().getLocation(), true, null);
            if (claim.allowEdit(p) != null) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void bulletHitEntityEvent(BulletHitEntityEvent e) {
        if (e.getShooter() instanceof Player p) {
            Claim claim = GriefPrevention.instance.dataStore.getClaimAt(e.getShooter().getLocation(), true, null);
            if (claim.allowEdit(p) != null) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void entityDamagedByAPI(EntityDamagedByAPI e) {
        if (e.getWitheredDamage().getDamager() != null && e.getWitheredDamage().getDamager() instanceof Player) {
            Player p = (Player)e.getWitheredDamage().getDamager();
            Claim claim = GriefPrevention.instance.dataStore.getClaimAt(p.getLocation(), true, null);
            if (claim.allowEdit(p) != null) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void grenadeThrowEvent(GrenadeThrowEvent e) {
        Claim claim = GriefPrevention.instance.dataStore.getClaimAt(e.getThrower().getLocation(), true, null);
        if (claim.allowEdit(e.getThrower()) != null) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void grenadeExplodeEvent(GrenadeExplodeEvent e) {
        Claim claim = GriefPrevention.instance.dataStore.getClaimAt(e.getThrower().getLocation(), true, null);
        if (claim.allowEdit(e.getThrower()) != null) {
            e.setCancelled(true);
        }
    }


}
