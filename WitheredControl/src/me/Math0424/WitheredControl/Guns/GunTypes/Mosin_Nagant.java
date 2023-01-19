package me.Math0424.WitheredControl.Guns.GunTypes;

import me.Math0424.CoreWeapons.Guns.Bullets.BulletType;
import me.Math0424.CoreWeapons.Guns.Gun.IGun;
import org.bukkit.ChatColor;
import org.bukkit.Sound;

public class Mosin_Nagant implements IGun {

    @Override
    public String name() {
        return ChatColor.GRAY + "Mosin-Nagant";
    }

    @Override
    public Integer modelId() {
        return 20;
    }

    @Override
    public Integer ammoId() {
        return 1;
    }

    @Override
    public Integer clipSize() {
        return 1;
    }

    @Override
    public Double bulletDrop() {
        return 0.2;
    }

    @Override
    public Double bulletSpeed() {
        return 4.5;
    }

    @Override
    public Double bulletDamage() {
        return 7.0;
    }

    @Override
    public Double bulletFalloff() {
        return 0.0;
    }

    @Override
    public Integer bulletSpread() {
        return 10;
    }

    @Override
    public Integer bulletCount() {
        return 1;
    }

    @Override
    public Integer reloadRate() {
        return 20;
    }

    @Override
    public Integer fireRate() {
        return 1;
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
        return true;
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
        return 10.0;
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
        return Sound.ENTITY_FIREWORK_ROCKET_BLAST;
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
