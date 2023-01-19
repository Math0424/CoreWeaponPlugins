package me.Math0424.CoreHooks.Bukkit;

import me.Math0424.CoreWeapons.Events.ArmorEvents.ArmorUseEvent;
import me.Math0424.CoreWeapons.Events.DeployableEvents.DeployableDeployEvent;
import me.Math0424.CoreWeapons.Events.GrenadeEvents.GrenadeThrowEvent;
import me.Math0424.CoreWeapons.Events.GunEvents.GunFireEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class BukkitHook implements Listener {

    //disabled events
    @EventHandler
    public void GunFireEvent(GunFireEvent e) {
        if (!e.getShooter().hasPermission("core.gun.fire")) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void deployableDeployEvent(DeployableDeployEvent e) {
        if (!e.getPlayer().hasPermission("core.deployable.deploy")) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void armorUseEvent(ArmorUseEvent e) {
        if (!e.getPlayer().hasPermission("core.armor.use")) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void grenadeThrowEvent(GrenadeThrowEvent e) {
        if (!e.getThrower().hasPermission("core.grenades.use")) {
            e.setCancelled(true);
        }
    }

}
