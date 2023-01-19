package me.Math0424.WitheredControl.Guns.GunTypes;

import me.Math0424.CoreWeapons.Guns.Bullets.BulletType;
import me.Math0424.CoreWeapons.Guns.Gun.IGun;
import org.bukkit.ChatColor;
import org.bukkit.Sound;

public class Remington_870 implements IGun {

    @Override
    public String name() {
        return ChatColor.GRAY + "Remington 870";
    }

    @Override
    public Integer modelId() {
        return 13;
    }

    @Override
    public Integer ammoId() {
        return 1;
    }

    @Override
    public Integer clipSize() {
        return 8;
    }

    @Override
    public Double bulletDrop() {
        return 0.25;
    }

    @Override
    public Double bulletSpeed() {
        return 2.2;
    }

    @Override
    public Double bulletDamage() {
        return 2.5;
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
        return 5;
    }

    @Override
    public Integer reloadRate() {
        return 9;
    }

    @Override
    public Integer fireRate() {
        return 18;
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
        return false;
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
        return BulletType.REGULAR;
    }

    @Override
    public Sound fireSound() {
        return Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR;
    }

    @Override
    public Float firePitch() {
        return 1.5f;
    }

    @Override
    public Integer fireVolume() {
        return 3;
    }
}
