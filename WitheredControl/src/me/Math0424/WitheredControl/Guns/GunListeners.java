package me.Math0424.WitheredControl.Guns;

import me.Math0424.CoreWeapons.Events.BulletEvents.BulletDestroyBlockEvent;
import me.Math0424.CoreWeapons.Events.BulletEvents.BulletHitEntityEvent;
import me.Math0424.CoreWeapons.Events.GunEvents.GunFireEvent;
import me.Math0424.CoreWeapons.Guns.Ammo.IAmmo;
import me.Math0424.CoreWeapons.Guns.Gun.IGun;
import me.Math0424.WitheredControl.Arenas.Arena;
import me.Math0424.WitheredControl.Arenas.ArenaPOINT;
import me.Math0424.WitheredControl.Arenas.ArenaTEAM;
import me.Math0424.WitheredControl.Guns.GunTypes.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class GunListeners implements Listener {

    public void registerGuns() {
        IAmmo.register(new FakeAmmo());
        IGun.register(new PKP());
        IGun.register(new Advanced_Sniper());
        IGun.register(new Scar_H());
        IGun.register(new M240());
        IGun.register(new AA_12());
        IGun.register(new M4());
        IGun.register(new UMP45());
        IGun.register(new Remington_870());
        IGun.register(new Laser());
        IGun.register(new Famas());
        IGun.register(new Barrett_M90());
        IGun.register(new Tec9());
        IGun.register(new MP5());
        IGun.register(new LaserShotGun());
        IGun.register(new AK_47());
        IGun.register(new FlameThrower());
        IGun.register(new Mosin_Nagant());
        IGun.register(new Deagle());
        IGun.register(new AcidGun());
        IGun.register(new P226());
    }

    @EventHandler
    public void bulletHitEntityEvent(BulletHitEntityEvent e) {
        if(e.getHitEntity() instanceof Player) {
            Player hit = (Player)e.getHitEntity();
            Arena hitArena = Arena.getArena(hit);
            Arena shooterArena = Arena.getArena(e.getShooter());
            if (shooterArena != null && hitArena == shooterArena) {
                if(hitArena instanceof ArenaPOINT || hitArena instanceof ArenaTEAM) {
                    if (hitArena.getTeam(hit) == hitArena.getTeam(e.getShooter())) {
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void bulletDestroyBlockEvent(BulletDestroyBlockEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void gunFireEvent(GunFireEvent e) {
        if (Arena.getArena(e.getShooter()) != null && !Arena.getArena(e.getShooter()).isGameRunning()) {
            e.setCancelled(true);
        }
    }
	
}
