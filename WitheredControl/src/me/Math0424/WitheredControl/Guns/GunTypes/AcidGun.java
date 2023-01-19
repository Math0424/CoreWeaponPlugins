package me.Math0424.WitheredControl.Guns.GunTypes;

import me.Math0424.CoreWeapons.Guns.Bullets.BulletType;
import me.Math0424.CoreWeapons.Guns.Gun.IGun;
import org.bukkit.ChatColor;
import org.bukkit.Sound;

public class AcidGun implements IGun {

    @Override
    public String name() {
        return ChatColor.GREEN + "AcidGun";
    }

    @Override
    public Integer modelId() {
        return 26;
    }

    @Override
    public Integer ammoId() {
        return 1;
    }

    @Override
    public Integer clipSize() {
        return 10;
    }

    @Override
    public Double bulletDrop() {
        return 0.2;
    }

    @Override
    public Double bulletSpeed() {
        return 3.0;
    }

    @Override
    public Double bulletDamage() {
        return 3.0;
    }

    @Override
    public Double bulletFalloff() {
        return 0.1;
    }

    @Override
    public Integer bulletSpread() {
        return 30;
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
        return 5;
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
        return BulletType.ACID;
    }

    @Override
    public Sound fireSound() {
        return Sound.ENTITY_ENDER_DRAGON_HURT;
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
