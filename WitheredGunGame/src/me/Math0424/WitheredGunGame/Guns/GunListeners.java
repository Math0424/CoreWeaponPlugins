package me.Math0424.WitheredGunGame.Guns;

import me.Math0424.WitheredGunGame.Arenas.Arena;
import me.Math0424.WitheredGunGame.Arenas.ArenaPOINT;
import me.Math0424.WitheredGunGame.Arenas.ArenaTEAM;
import me.Math0424.WitheredGunGame.Guns.GunTypes.*;
import me.Math0424.CoreWeapons.Events.BulletEvents.BulletDestroyBlockEvent;
import me.Math0424.CoreWeapons.Events.BulletEvents.BulletHitEntityEvent;
import me.Math0424.CoreWeapons.Events.GunEvents.GunFireEvent;
import me.Math0424.CoreWeapons.Guns.Gun.Gun;
import me.Math0424.CoreWeapons.Guns.Gun.IGun;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;

public class GunListeners implements Listener {

    private static final HashMap<Integer, Gun> guns = new HashMap<Integer, Gun>();

    public void registerGuns() {
        guns.put(1, IGun.register(new PKP()));
        guns.put(2, IGun.register(new Advanced_Sniper()));
        guns.put(3, IGun.register(new Scar_H()));
        guns.put(4, IGun.register(new M240()));
        guns.put(5, IGun.register(new AA_12()));
        guns.put(6, IGun.register(new M4()));
        guns.put(7, IGun.register(new UMP45()));
        guns.put(8, IGun.register(new Remington_870()));
        guns.put(9, IGun.register(new Laser()));
        guns.put(10, IGun.register(new Famas()));
        guns.put(11, IGun.register(new Barrett_M90()));
        guns.put(12, IGun.register(new Tec9()));
        guns.put(13, IGun.register(new MP5()));
        guns.put(14, IGun.register(new LaserShotGun()));
        guns.put(15, IGun.register(new AK_47()));
        guns.put(16, IGun.register(new FlameThrower()));
        guns.put(17, IGun.register(new Mosin_Nagant()));
        guns.put(18, IGun.register(new Deagle()));
        guns.put(19, IGun.register(new AcidGun()));
        guns.put(20, IGun.register(new P226()));
    }

    @EventHandler
    public void bulletHitEntityEvent(BulletHitEntityEvent e) {
        if(e.getHitEntity() instanceof Player) {
            Player hit = (Player)e.getHitEntity();
            Arena hitArena = Arena.getArena(hit);
            Arena shooterArena = Arena.getArena(e.getShooter());
            if (shooterArena != null && hitArena == shooterArena) {
                if(hitArena instanceof ArenaPOINT || hitArena instanceof ArenaTEAM) {
                    if (hitArena.getTeam(hit).equals(hitArena.getTeam(e.getShooter()))) {
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
        if (Arena.getArena(e.getShooter()) != null && Arena.getArena(e.getShooter()).getPhase() != Arena.Phase.RUNNING) {
            e.setCancelled(true);
        }
    }

    public static HashMap<Integer, Gun> getGuns() {
        return guns;
    }
}
