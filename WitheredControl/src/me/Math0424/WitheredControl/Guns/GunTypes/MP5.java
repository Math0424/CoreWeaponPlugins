package me.Math0424.WitheredControl.Guns.GunTypes;

import me.Math0424.CoreWeapons.Guns.Bullets.BulletType;
import me.Math0424.CoreWeapons.Guns.Gun.IGun;
import org.bukkit.ChatColor;
import org.bukkit.Sound;

public class MP5 implements IGun {

    @Override
    public String name() {
        return ChatColor.GRAY + "MP5";
    }

    @Override
    public Integer modelId() {
        return 21;
    }

    @Override
    public Integer ammoId() {
        return 1;
    }

    @Override
    public Integer clipSize() {
        return 25;
    }

    @Override
    public Double bulletDrop() {
        return 0.2;
    }

    @Override
    public Double bulletSpeed() {
        return 2.0;
    }

    @Override
    public Double bulletDamage() {
        return 2.0;
    }

    @Override
    public Double bulletFalloff() {
        return 0.1;
    }

    @Override
    public Integer bulletSpread() {
        return 20;
    }

    @Override
    public Integer bulletCount() {
        return 1;
    }

    @Override
    public Integer reloadRate() {
        return 1;
    }

    @Override
    public Integer fireRate() {
        return 2;
    }

    @Override
    public Integer shotsReloadedPerReload() {
        return 1;
    }

    @Override
    public Integer ammoConsumedPerReload() {
        return -1;
    }

    @Override
    public boolean isAutomatic() {
        return true;
    }

    @Override
    public boolean isScoped() {
        return false;
    }

    @Override
    public Integer zoomLevel() {
        return 1;
    }

    @Override
    public boolean hasNightVision() {
        return false;
    }

    @Override
    public boolean isPrimaryGun() {
        return false;
    }

    @Override
    public Double headShotDamage() {
        return 2.0;
    }

    @Override
    public Float explosiveYield() {
        return 0f;
    }

    @Override
    public BulletType bulletType() {
        return BulletType.REGULAR;
    }

    @Override
    public Sound fireSound() {
        return Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR;
    }

    @Override
    public Float firePitch() {
        return 2f;
    }

    @Override
    public Integer fireVolume() {
        return 3;
    }
}
