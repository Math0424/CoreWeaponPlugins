package me.Math0424.WitheredControl.Guns.GunTypes;

import me.Math0424.CoreWeapons.Guns.Bullets.BulletType;
import me.Math0424.CoreWeapons.Guns.Gun.IGun;
import org.bukkit.ChatColor;
import org.bukkit.Sound;

public class Laser implements IGun {

    @Override
    public String name() {
        return ChatColor.RED + "Laser";
    }

    @Override
    public Integer modelId() {
        return 7;
    }

    @Override
    public Integer ammoId() {
        return 1;
    }

    @Override
    public Integer clipSize() {
        return 50;
    }

    @Override
    public Double bulletDrop() {
        return 0.0;
    }

    @Override
    //distance
    public Double bulletSpeed() {
        return 50.0;
    }

    @Override
    public Double bulletDamage() {
        return 2.0;
    }

    @Override
    public Double bulletFalloff() {
        return 0.0;
    }

    @Override
    public Integer bulletSpread() {
        return 3;
    }

    @Override
    public Integer bulletCount() {
        return 1;
    }

    @Override
    public Integer reloadRate() {
        return 2;
    }

    @Override
    public Integer fireRate() {
        return 3;
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
        return true;
    }

    @Override
    public Double headShotDamage() {
        return 3.0;
    }

    @Override
    public Float explosiveYield() {
        return 0f;
    }

    @Override
    public BulletType bulletType() {
        return BulletType.LASER;
    }

    @Override
    public Sound fireSound() {
        return Sound.BLOCK_SCAFFOLDING_PLACE;
    }

    @Override
    public Float firePitch() {
        return 1f;
    }

    @Override
    public Integer fireVolume() {
        return 3;
    }
}
