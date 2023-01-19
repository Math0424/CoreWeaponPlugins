package me.Math0424.WitheredControl.Guns.GunTypes;

import me.Math0424.CoreWeapons.Guns.Bullets.BulletType;
import me.Math0424.CoreWeapons.Guns.Gun.IGun;
import org.bukkit.ChatColor;
import org.bukkit.Sound;

public class Scar_H implements IGun {

    @Override
    public String name() {
        return ChatColor.GRAY + "Scar-H";
    }

    @Override
    public Integer modelId() {
        return 15;
    }

    @Override
    public Integer ammoId() {
        return 1;
    }

    @Override
    public Integer clipSize() {
        return 30;
    }

    @Override
    public Double bulletDrop() {
        return 0.3;
    }

    @Override
    public Double bulletSpeed() {
        return 4.0;
    }

    @Override
    public Double bulletDamage() {
        return 5.0;
    }

    @Override
    public Double bulletFalloff() {
        return 0.05;
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
        return 3;
    }

    @Override
    public Integer fireRate() {
        return 4;
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
        return 5.0;
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
        return 3f;
    }

    @Override
    public Integer fireVolume() {
        return 3;
    }
}
