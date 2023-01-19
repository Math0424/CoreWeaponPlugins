package me.Math0424.GunCreator.GUI;

import me.Math0424.CoreWeapons.Guns.Attachments.AttachmentModifier;
import me.Math0424.CoreWeapons.Guns.Bullets.BulletType;
import me.Math0424.CoreWeapons.Guns.Gun.Gun;
import me.Math0424.CoreWeapons.Guns.Gun.GunType;
import me.Math0424.CoreWeapons.Guns.QualityEnum;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ProtoGun {

    private String name = "MyCustomGun";
    private boolean isPrimaryGun = true;
    private QualityEnum qualityEnum = QualityEnum.FACTORY;
    private String ammoID = "CustomAmmo";
    private String fireSoundID = "Fire";
    private float firePitch = 1;
    private int fireSoundRange = 100;
    private GunType gunType = GunType.AUTOMATIC;
    private BulletType bulletType = BulletType.REGULAR;
    private List<AttachmentModifier> incompatibleAttachments = new ArrayList<>();
    private int maxAmmoCount = 30;
    private int maxBurstCount = 0;
    private int shotsAddedPerReloadCycle = 1;
    private int bulletsDrawnPerReloadCycle = 1;
    private int reloadSpeed = 1;
    private int fireSpeed = 5;

    private double bulletDrop = .1;
    private double bulletSpeed = 3;
    private double bulletDamage = 5;
    private double headshotMultiplier = 1.3;
    private double bulletFalloff = .1;
    private double bulletSpread = 10;
    private float bulletPower = 0;
    private int bulletCountPerShot = 1;

    public void setGun(Gun g) {
        name = g.getName();
        isPrimaryGun = g.isPrimaryGun();
        ammoID = g.getAmmoID();
        fireSoundID = g.getFireSoundID();
        firePitch = g.getFirePitch();
        fireSoundRange = g.getFireSoundRange();
        gunType = g.getGunType();
        bulletType = g.getBulletType();
        incompatibleAttachments = g.getIncompatibleModifiers() == null ? new ArrayList<>() : g.getIncompatibleModifiers();
        maxAmmoCount = g.getMaxShotCount();
        maxBurstCount = g.getMaxBurstCount();
        shotsAddedPerReloadCycle = g.getShotsAddedPerReloadCycle();
        bulletsDrawnPerReloadCycle = g.getBulletsDrawnPerReloadCycle();
        reloadSpeed = g.getReloadSpeed();
        fireSpeed = g.getFireSpeed();
        bulletDrop = g.getBulletDrop();
        bulletSpeed = g.getBulletSpeed();
        bulletDamage = g.getBulletDamage();
        headshotMultiplier = g.getHeadshotMultiplier();
        bulletFalloff = g.getBulletFalloff();
        bulletSpread = g.getBulletSpread();
        bulletPower = g.getBulletPower();
        bulletCountPerShot = g.getBulletCountPerShot();
    }

    public void ApplyToItemStack(ItemStack item) {
        new Gun(name,
                isPrimaryGun,
                ammoID,
                fireSoundID,
                firePitch,
                fireSoundRange,
                gunType,
                bulletType,
                incompatibleAttachments,
                qualityEnum.getValue(),
                maxBurstCount,
                maxAmmoCount,
                shotsAddedPerReloadCycle,
                bulletsDrawnPerReloadCycle,
                reloadSpeed,
                fireSpeed,
                bulletDrop,
                bulletSpeed,
                bulletDamage,
                headshotMultiplier,
                bulletFalloff,
                bulletSpread,
                bulletPower,
                bulletCountPerShot
        ).applyToItem(item);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public QualityEnum getQualityEnum() {
        return qualityEnum;
    }

    public void setQualityEnum(QualityEnum qualityEnum) {
        this.qualityEnum = qualityEnum;
    }

    public boolean isPrimaryGun() {
        return isPrimaryGun;
    }

    public void setPrimaryGun(boolean primaryGun) {
        isPrimaryGun = primaryGun;
    }

    public String getAmmoID() {
        return ammoID;
    }

    public void setAmmoID(String ammoID) {
        this.ammoID = ammoID;
    }

    public String getFireSoundID() {
        return fireSoundID;
    }

    public void setFireSoundID(String fireSoundID) {
        this.fireSoundID = fireSoundID;
    }

    public float getFirePitch() {
        return firePitch;
    }

    public void setFirePitch(float firePitch) {
        this.firePitch = firePitch;
    }

    public int getFireSoundRange() {
        return fireSoundRange;
    }

    public void setFireSoundRange(int fireSoundRange) {
        this.fireSoundRange = fireSoundRange;
    }

    public GunType getGunType() {
        return gunType;
    }

    public void setGunType(GunType gunType) {
        this.gunType = gunType;
    }

    public BulletType getBulletType() {
        return bulletType;
    }

    public void setBulletType(BulletType bulletType) {
        this.bulletType = bulletType;
    }

    public List<AttachmentModifier> getIncompatibleAttachments() {
        return incompatibleAttachments;
    }

    public int getMaxAmmoCount() {
        return maxAmmoCount;
    }

    public void setMaxAmmoCount(int maxAmmoCount) {
        this.maxAmmoCount = maxAmmoCount;
    }

    public int getMaxBurstCount() {
        return maxBurstCount;
    }

    public void setMaxBurstCount(int maxBurstCount) {
        this.maxBurstCount = maxBurstCount;
    }

    public int getShotsAddedPerReloadCycle() {
        return shotsAddedPerReloadCycle;
    }

    public void setShotsAddedPerReloadCycle(int shotsAddedPerReloadCycle) {
        this.shotsAddedPerReloadCycle = shotsAddedPerReloadCycle;
    }

    public int getBulletsDrawnPerReloadCycle() {
        return bulletsDrawnPerReloadCycle;
    }

    public void setBulletsDrawnPerReloadCycle(int bulletsDrawnPerReloadCycle) {
        this.bulletsDrawnPerReloadCycle = bulletsDrawnPerReloadCycle;
    }

    public int getReloadSpeed() {
        return reloadSpeed;
    }

    public void setReloadSpeed(int reloadSpeed) {
        this.reloadSpeed = reloadSpeed;
    }

    public int getFireSpeed() {
        return fireSpeed;
    }

    public void setFireSpeed(int fireSpeed) {
        this.fireSpeed = fireSpeed;
    }

    public double getBulletDrop() {
        return bulletDrop;
    }

    public void setBulletDrop(double bulletDrop) {
        this.bulletDrop = bulletDrop;
    }

    public double getBulletSpeed() {
        return bulletSpeed;
    }

    public void setBulletSpeed(double bulletSpeed) {
        this.bulletSpeed = bulletSpeed;
    }

    public double getBulletDamage() {
        return bulletDamage;
    }

    public void setBulletDamage(double bulletDamage) {
        this.bulletDamage = bulletDamage;
    }

    public double getHeadshotMultiplier() {
        return headshotMultiplier;
    }

    public void setHeadshotMultiplier(double headshotMultiplier) {
        this.headshotMultiplier = headshotMultiplier;
    }

    public double getBulletFalloff() {
        return bulletFalloff;
    }

    public void setBulletFalloff(double bulletFalloff) {
        this.bulletFalloff = bulletFalloff;
    }

    public double getBulletSpread() {
        return bulletSpread;
    }

    public void setBulletSpread(double bulletSpread) {
        this.bulletSpread = bulletSpread;
    }

    public float getBulletPower() {
        return bulletPower;
    }

    public void setBulletPower(float bulletPower) {
        this.bulletPower = bulletPower;
    }

    public int getBulletCountPerShot() {
        return bulletCountPerShot;
    }

    public void setBulletCountPerShot(int bulletCountPerShot) {
        this.bulletCountPerShot = bulletCountPerShot;
    }
}
