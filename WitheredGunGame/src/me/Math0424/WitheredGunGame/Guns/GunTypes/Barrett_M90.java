package me.Math0424.WitheredGunGame.Guns.GunTypes;

import me.Math0424.CoreWeapons.Guns.Attachments.Attachment;
import me.Math0424.CoreWeapons.Guns.Bullets.BulletType;
import me.Math0424.CoreWeapons.Guns.Gun.GunType;
import me.Math0424.CoreWeapons.Guns.Gun.IGun;
import org.bukkit.ChatColor;
import me.Math0424.CoreWeapons.Sound.SoundCache;

import java.util.List;

public class Barrett_M90 implements IGun {

    @Override
    public String name() {
        return ChatColor.GRAY + "Barrett M90";
    }

    @Override
    public int modelId() {
        return 3;
    }

    @Override
    public String ammoId() {
        return null;
    }

    @Override
    public int maxAmmoCount() {
        return 7;
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
        return 0.2;
    }

    @Override
    public double bulletSpeed() {
        return 4.5;
    }

    @Override
    public double bulletDamage() {
        return 10.0;
    }

    @Override
    public double bulletFalloff() {
        return 0.0;
    }

    @Override
    public int bulletSpread() {
        return 3;
    }

    @Override
    public int bulletCount() {
        return 1;
    }

    @Override
    public int reloadRate() {
        return 20;
    }

    @Override
    public int fireRate() {
        return 50;
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
        return GunType.SEMI_AUTOMATIC;
    }

    @Override
    public boolean isPrimaryGun() {
        return true;
    }

    @Override
    public double bulletHeadShotDamage() {
        return 15.0;
    }

    @Override
    public float explosiveYield() {
        return 0f;
    }

    @Override
    public BulletType bulletType() {
        return BulletType.REGULAR;
    }

    @Override
    public SoundCache fireSound() {
        return SoundCache.SNIPER_GUN;
    }

    @Override
    public float firePitch() {
        return 1f;
    }

    @Override
    public int fireVolume() {
        return 70;
    }
}
