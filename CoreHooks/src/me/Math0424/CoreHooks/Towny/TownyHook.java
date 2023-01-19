package me.Math0424.CoreHooks.Towny;

import com.palmergames.bukkit.towny.Towny;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.TownBlock;
import com.palmergames.bukkit.towny.object.TownyPermission;
import com.palmergames.bukkit.towny.utils.CombatUtil;
import com.palmergames.bukkit.towny.utils.PlayerCacheUtil;
import me.Math0424.CoreWeapons.Events.ArmorEvents.ArmorUseEvent;
import me.Math0424.CoreWeapons.Events.BulletEvents.BulletDestroyBlockEvent;
import me.Math0424.CoreWeapons.Events.BulletEvents.BulletHitBlockEvent;
import me.Math0424.CoreWeapons.Events.BulletEvents.BulletHitEntityEvent;
import me.Math0424.CoreWeapons.Events.BulletEvents.EntityDamagedByAPI;
import me.Math0424.CoreWeapons.Events.DeployableEvents.DeployableDeployEvent;
import me.Math0424.CoreWeapons.Events.GrenadeEvents.GrenadeExplodeEvent;
import me.Math0424.CoreWeapons.Events.GrenadeEvents.GrenadeThrowEvent;
import me.Math0424.CoreWeapons.Events.GunEvents.GunFireEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class TownyHook implements Listener {

    private Towny towny = (Towny) Bukkit.getServer().getPluginManager().getPlugin("Towny");

    //disabled events
    @EventHandler
    public void GunFireEvent(GunFireEvent e) {
        try {
            TownBlock block = TownyAPI.getInstance().getTownBlock(e.getShooter().getLocation());
            if (CombatUtil.preventPvP(block.getWorld(), block)) {
                e.setCancelled(true);
            }
        } catch (Exception ignored) {}
    }

    @EventHandler
    public void deployableDeployEvent(DeployableDeployEvent e) {
        if (!PlayerCacheUtil.getCachePermission(e.getPlayer(), e.getLocation(), Material.ARMOR_STAND, TownyPermission.ActionType.ITEM_USE)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void armorUseEvent(ArmorUseEvent e) {
        if (!PlayerCacheUtil.getCachePermission(e.getPlayer(), e.getPlayer().getLocation(), Material.DIAMOND_CHESTPLATE, TownyPermission.ActionType.ITEM_USE)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void bulletDestroyBlockEvent(BulletDestroyBlockEvent e) {
        if (e.getShooter() instanceof Player p) {
            if (!PlayerCacheUtil.getCachePermission(p, e.getBlock().getLocation(), e.getBlock().getType(), TownyPermission.ActionType.DESTROY)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void bulletHitBlockEvent(BulletHitBlockEvent e) {
        if (e.getShooter() instanceof Player p) {
            if (!PlayerCacheUtil.getCachePermission(p, e.getHitBlock().getLocation(), e.getHitBlock().getType(), TownyPermission.ActionType.DESTROY)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void bulletHitEntityEvent(BulletHitEntityEvent e) {
        if (CombatUtil.preventDamageCall(towny, e.getShooter(), e.getHitEntity())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void entityDamagedByAPI(EntityDamagedByAPI e) {
        if (e.getWitheredDamage().getDamager() != null && e.getWitheredDamage().getDamaged() != null) {
            if (CombatUtil.preventDamageCall(towny, e.getWitheredDamage().getDamager(), e.getWitheredDamage().getDamaged())) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void grenadeThrowEvent(GrenadeThrowEvent e) {
        if (!PlayerCacheUtil.getCachePermission(e.getThrower(), e.getThrower().getLocation(), Material.SLIME_BALL, TownyPermission.ActionType.ITEM_USE)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void grenadeExplodeEvent(GrenadeExplodeEvent e) {
        if (!PlayerCacheUtil.getCachePermission(e.getThrower(), e.getItem().getLocation(), e.getItem().getItemStack().getType(), TownyPermission.ActionType.DESTROY)) {
            e.setCancelled(true);
        }
    }


}
