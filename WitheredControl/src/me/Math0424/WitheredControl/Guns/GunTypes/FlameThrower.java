package me.Math0424.WitheredControl.Guns.GunTypes;

import me.Math0424.CoreWeapons.Guns.Bullets.BulletType;
import me.Math0424.CoreWeapons.Guns.Gun.IGun;
import org.bukkit.ChatColor;
import org.bukkit.Sound;

public class FlameThrower implements IGun {

    @Override
    public String name() {
        return ChatColor.YELLOW + "FlameThrower";
    }

    @Override
    public Integer modelId() {
        return 5;
    }

    @Override
    public Integer ammoId() {
        return 1;
    }

    @Override
    public Integer clipSize() {
        return 100;
    }

    @Override
    public Double bulletDrop() {
        return 0.3;
    }

    @Override
    public Double bulletSpeed() {
        return 1.0;
    }

    @Override
    public Double bulletDamage() {
        return 3.0;
    }

    @Override
    public Double bulletFalloff() {
        return 0.08;
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
        return 2;
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
        return 4.0;
    }

    @Override
    public Float explosiveYield() {
        return 0f;
    }

    @Override
    public BulletType bulletType() {
        return BulletType.FLAME;
    }

    @Override
    public Sound fireSound() {
        return Sound.ENTITY_BLAZE_HURT;
    }

    @Override
    public Float firePitch() {
        return 2f;
    }

    @Override
    public Integer fireVolume() {
        return 2;
    }
}
