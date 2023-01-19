package me.Math0424.CoreHooks.Lands;

import me.Math0424.CoreHooks.Main;
import me.Math0424.CoreWeapons.Events.ArmorEvents.ArmorUseEvent;
import me.Math0424.CoreWeapons.Events.BulletEvents.BulletDestroyBlockEvent;
import me.Math0424.CoreWeapons.Events.BulletEvents.BulletHitBlockEvent;
import me.Math0424.CoreWeapons.Events.BulletEvents.BulletHitEntityEvent;
import me.Math0424.CoreWeapons.Events.BulletEvents.EntityDamagedByAPI;
import me.Math0424.CoreWeapons.Events.DeployableEvents.DeployableDeployEvent;
import me.Math0424.CoreWeapons.Events.GrenadeEvents.GrenadeExplodeEvent;
import me.Math0424.CoreWeapons.Events.GrenadeEvents.GrenadeThrowEvent;
import me.Math0424.CoreWeapons.Events.GunEvents.GunFireEvent;
import me.angeschossen.lands.api.integration.LandsIntegration;
import me.angeschossen.lands.api.land.Land;
import me.angeschossen.lands.api.role.enums.ManagementSetting;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class LandsHook implements Listener {

    private static LandsIntegration instance;

    public static void init() {
        instance = new LandsIntegration(Main.getPlugin());
    }

    //disabled events
    @EventHandler
    public void GunFireEvent(GunFireEvent e) {
        Land l = instance.getLand(e.getShooter().getLocation());
        if (l != null && !l.canManagement(e.getShooter().getUniqueId(), ManagementSetting.SETTING_EDIT_LAND)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void deployableDeployEvent(DeployableDeployEvent e) {
        Land l = instance.getLand(e.getLocation());
        if (l != null && !l.canManagement(e.getPlayer().getUniqueId(), ManagementSetting.SETTING_EDIT_LAND)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void armorUseEvent(ArmorUseEvent e) {
        Land l = instance.getLand(e.getPlayer().getLocation());
        if (l != null && !l.canManagement(e.getPlayer().getUniqueId(), ManagementSetting.SETTING_EDIT_LAND)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void bulletDestroyBlockEvent(BulletDestroyBlockEvent e) {
        Land l = instance.getLand(e.getBlock().getLocation());
        if (l != null && !l.canManagement(e.getShooter().getUniqueId(), ManagementSetting.SETTING_EDIT_LAND)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void bulletHitBlockEvent(BulletHitBlockEvent e) {
        Land l = instance.getLand(e.getHitBlock().getLocation());
        if (l != null && !l.canManagement(e.getShooter().getUniqueId(), ManagementSetting.SETTING_EDIT_LAND)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void bulletHitEntityEvent(BulletHitEntityEvent e) {
        if (e.getHitEntity() instanceof Player && e.getShooter() instanceof Player p) {
            if (instance.canPvP(p, (Player)e.getHitEntity(), e.getHitEntity().getLocation(), true, true)) {
                e.setCancelled(true);
            }
        } else {
            Land l = instance.getLand(e.getHitEntity().getLocation());
            if (l != null && !l.canManagement(e.getShooter().getUniqueId(), ManagementSetting.SETTING_EDIT_LAND)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void entityDamagedByAPI(EntityDamagedByAPI e) {
        if (e.getWitheredDamage().getDamager() != null && e.getWitheredDamage().getDamager() instanceof Player) {
            Land l = instance.getLand(e.getWitheredDamage().getDamager().getLocation());
            if (l != null && !l.canManagement(e.getWitheredDamage().getDamager().getUniqueId(), ManagementSetting.SETTING_EDIT_LAND)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void grenadeThrowEvent(GrenadeThrowEvent e) {
        Land l = instance.getLand(e.getThrower().getLocation());
        if (l != null && !l.canManagement(e.getThrower().getUniqueId(), ManagementSetting.SETTING_EDIT_LAND)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void grenadeExplodeEvent(GrenadeExplodeEvent e) {
        Land l = instance.getLand(e.getThrower().getLocation());
        if (l != null && !l.canManagement(e.getThrower().getUniqueId(), ManagementSetting.SETTING_EDIT_LAND)) {
            e.setCancelled(true);
        }
    }


}
