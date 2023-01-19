package me.Math0424.CoreHooks.WorldGuard;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import me.Math0424.CoreWeapons.Events.ArmorEvents.ArmorUseEvent;
import me.Math0424.CoreWeapons.Events.BulletEvents.BulletDestroyBlockEvent;
import me.Math0424.CoreWeapons.Events.BulletEvents.BulletHitBlockEvent;
import me.Math0424.CoreWeapons.Events.BulletEvents.BulletHitEntityEvent;
import me.Math0424.CoreWeapons.Events.DeployableEvents.DeployableDeployEvent;
import me.Math0424.CoreWeapons.Events.GrenadeEvents.GrenadeExplodeEvent;
import me.Math0424.CoreWeapons.Events.GrenadeEvents.GrenadeThrowEvent;
import me.Math0424.CoreWeapons.Events.GunEvents.GunFireEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class WorldGuardHook implements Listener {

    private static StateFlag disableGuns;
    private static StateFlag disableGrenades;
    private static StateFlag disableArmor;
    private static StateFlag disableWithered;
    private static StateFlag disableExplosions;
    private static RegionContainer container;
    private static FlagRegistry registry;

    public static void loadWorldGuardHooks() {
        registry = WorldGuard.getInstance().getFlagRegistry();

        disableGuns = new StateFlag("core-guns", true);
        disableGrenades = new StateFlag("core-grenades", true);
        disableWithered = new StateFlag("core-all", true);
        disableArmor = new StateFlag("core-armor", true);
        disableExplosions = new StateFlag("core-explosions", true);

        registry.register(disableGuns);
        registry.register(disableGrenades);
        registry.register(disableWithered);
        registry.register(disableArmor);
        registry.register(disableExplosions);
    }

    public static void loadContainer() {
        container = WorldGuard.getInstance().getPlatform().getRegionContainer();
    }

    //disabled events
    @EventHandler
    public void GunFireEvent(GunFireEvent e) {
        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(e.getShooter());
        RegionQuery q = container.createQuery();
        if (!q.testState(localPlayer.getLocation(), localPlayer, disableGuns, disableWithered)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void deployableDeployEvent(DeployableDeployEvent e) {
        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(e.getPlayer());
        RegionQuery q = container.createQuery();
        if (!q.testState(localPlayer.getLocation(), localPlayer, disableArmor, disableWithered)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void armorUseEvent(ArmorUseEvent e) {
        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(e.getPlayer());
        RegionQuery q = container.createQuery();
        if (!q.testState(localPlayer.getLocation(), localPlayer, disableGuns, disableWithered)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void bulletDestroyBlockEvent(BulletDestroyBlockEvent e) {
        if (e.getShooter() instanceof Player p) {
            LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(p);
            RegionQuery q = container.createQuery();
            if (!q.testState(BukkitAdapter.adapt(e.getBlock().getLocation()), localPlayer, disableGuns, disableWithered, disableExplosions)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void bulletHitBlockEvent(BulletHitBlockEvent e) {
        if (e.getShooter() instanceof Player p) {
            LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(p);
            RegionQuery q = container.createQuery();
            if (!q.testState(BukkitAdapter.adapt(e.getHitBlock().getLocation()), localPlayer, disableGuns, disableWithered, disableExplosions)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void bulletHitEntityEvent(BulletHitEntityEvent e) {
        if (e.getShooter() instanceof Player p) {
            LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(p);
            RegionQuery q = container.createQuery();
            if (!q.testState(BukkitAdapter.adapt(e.getHitEntity().getLocation()), localPlayer, disableGuns, disableWithered)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void grenadeThrowEvent(GrenadeThrowEvent e) {
        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(e.getThrower());
        RegionQuery q = container.createQuery();
        if (!q.testState(localPlayer.getLocation(), localPlayer, disableGrenades, disableWithered)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void grenadeExplodeEvent(GrenadeExplodeEvent e) {
        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(e.getThrower());
        RegionQuery q = container.createQuery();
        if (!q.testState(BukkitAdapter.adapt(e.getItem().getLocation()), localPlayer, disableGuns, disableWithered, disableExplosions)) {
            e.setCancelled(true);
        }
    }


}
