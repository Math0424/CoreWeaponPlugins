package me.Math0424.WitheredGunGame.Guns.GunTypes;

import me.Math0424.CoreWeapons.Guns.Attachments.Attachment;
import me.Math0424.CoreWeapons.Guns.Bullets.BulletType;
import me.Math0424.CoreWeapons.Guns.Gun.GunType;
import me.Math0424.CoreWeapons.Guns.Gun.IGun;
import org.bukkit.ChatColor;
import me.Math0424.CoreWeapons.Sound.SoundCache;

import java.util.List;

public class Advanced_Sniper implements IGun {

    @Override
    public String name() {
        return ChatColor.BLUE + "Advanced Sniper";
    }

    @Override
    public int modelId() {
        return 27;
    }

    @Override
    public String ammoId() {
        return null;
    }

    @Override
    public int maxAmmoCount() {
        return 10;
    }

    @Override
    public int maxBurstRoundCount() {
        return 0;
    }

    @Override
    public List<Attachment> attachments() {
        return null;
    }

    @Override
    public List<String> illegalAttachmentClassifiers() {
        return null;
    }

    @Override
    public double bulletDrop() {
        return 0.1;
    }

    @Override
    public double bulletSpeed() {
        return 4.0;
    }

    @Override
    public double bulletDamage() {
        return 8.0;
    }

    @Override
    public double bulletFalloff() {
        return 0.0;
    }

    @Override
    public int bulletSpread() {
        return 4;
    }

    @Override
    public int bulletCount() {
        return 1;
    }

    @Override
    public int reloadRate() {
        return 10;
    }

    @Override
    public int fireRate() {
        return 20;
    }

    @Override
    public int bulletsPerReload() {
        return 1;
    }

    @Override
    public int ammoPerReload() {
        return -1;
    }

    @Override
    public GunType fireType() {
        return GunType.AUTOMATIC;
    }

    @Override
    public boolean isPrimaryGun() {
        return true;
    }

    @Override
    public double bulletHeadShotDamage() {
        return 10.0;
    }

    @Override
    public float explosiveYield() {
        return 0f;
    }

    @Override
    public BulletType bulletType() {
        return BulletType.BLINDNESS;
    }

    @Override
    public SoundCache fireSound() {
        return SoundCache.SNIPER_GUN;
    }

    @Override
    public float firePitch() {
        return 2f;
    }

    @Override
    public int fireVolume() {
        return 70;
    }
}
