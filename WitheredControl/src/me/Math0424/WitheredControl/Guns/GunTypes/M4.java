package me.Math0424.WitheredControl.Guns.GunTypes;

import me.Math0424.CoreWeapons.Guns.Bullets.BulletType;
import me.Math0424.CoreWeapons.Guns.Gun.IGun;
import org.bukkit.ChatColor;
import org.bukkit.Sound;

public class M4 implements IGun {

    @Override
    public String name() {
        return ChatColor.GRAY + "M4";
    }

    @Override
    public Integer modelId() {
        return 9;
    }

    @Override
    public Integer ammoId() {
        return 1;
    }

    @Override
    public Integer clipSize() {
        return 32;
    }

    @Override
    public Double bulletDrop() {
        return 0.3;
    }

    @Override
    public Double bulletSpeed() {
        return 3.6;
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
        return 20;
    }

    @Override
    public Integer bulletCount() {
        return 1;
    }

    @Override
    public Integer reloadRate() {
        return 4;
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
        return 4.0;
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
        return 2.6f;
    }

    @Override
    public Integer fireVolume() {
        return 3;
    }
}
