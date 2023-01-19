package me.Math0424.WitheredControl.Guns.GunTypes;

import me.Math0424.CoreWeapons.Guns.Bullets.BulletType;
import me.Math0424.CoreWeapons.Guns.Gun.IGun;
import org.bukkit.ChatColor;
import org.bukkit.Sound;

public class M240 implements IGun {

    @Override
    public String name() {
        return ChatColor.GRAY + "M240";
    }

    @Override
    public Integer modelId() {
        return 10;
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
        return 0.2;
    }

    @Override
    public Double bulletSpeed() {
        return 2.5;
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
        return 30;
    }

    @Override
    public Integer bulletCount() {
        return 1;
    }

    @Override
    public Integer reloadRate() {
        return 5;
    }

    @Override
    public Integer fireRate() {
        return 2;
    }

    @Override
    public Integer shotsReloadedPerReload() {
        return 5;
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
        return 1.5f;
    }

    @Override
    public Integer fireVolume() {
        return 3;
    }
}
