package me.Math0424.Withered.WitheredAPI.Serializable;

import me.Math0424.Withered.Loot.LootItem;
import me.Math0424.Withered.Util.WitheredUtil;
import me.Math0424.CoreWeapons.Guns.Bullets.BulletType;
import me.Math0424.CoreWeapons.Guns.Gun.GunType;
import me.Math0424.CoreWeapons.Guns.Gun.Gun;
import me.Math0424.CoreWeapons.Guns.QualityEnum;
import me.Math0424.CoreWeapons.Sound.SoundCache;
import org.bukkit.ChatColor;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class GunSerializable implements ConfigurationSerializable {

    private static final ArrayList<GunSerializable> guns = new ArrayList<>();

    public Gun baseClass;

    private final List<String> illegalAttachmentClassifiers = new ArrayList<>();

    private String name;

    private int modelId;
    private int ammoId;
    private int maxAmmoCount;

    private double bulletDrop;
    private double bulletSpeed;
    private double bulletDamage;
    private double bulletHeadShotDamage;
    private double bulletFalloff;
    private int bulletSpread;
    private int bulletCount;

    private int reloadRate;
    private int fireRate;
    private int burstRoundCount;

    private int bulletsPerReload;
    private int ammoPerReload;

    private boolean isPrimaryGun;

    private float explosiveYield;

    private BulletType bulletType;
    private SoundCache fireSound;
    private GunType fireType;

    private float firePitch;
    private int fireVolume;


    //not gun
    private Integer level;
    private Double chanceOfSpawning;

    public GunSerializable(Map<String, Object> map) {
        try {
            String str = (String) map.get("name");
            this.name = str.replaceAll("&", "§");
            this.modelId = (int) map.get("modelId");
            this.ammoId = (int) map.get("ammoId");

            this.bulletsPerReload = (int) map.get("bulletsPerReload");
            this.bulletDrop = (double) map.get("bulletDrop");
            this.bulletDamage = (double) map.get("bulletDamage");
            this.bulletSpeed = (double) map.get("bulletSpeed");
            this.bulletFalloff = (double) map.get("bulletFalloff");
            this.bulletSpread = (int) map.get("bulletSpread");
            this.bulletCount = (int) map.get("bulletCount");
            this.bulletHeadShotDamage = (double) map.get("bulletHeadShotDamage");

            this.ammoPerReload = (int) map.get("ammoPerReload");
            this.maxAmmoCount = (int) map.get("maxAmmoCount");
            this.reloadRate = (int) map.get("reloadRate");

            this.burstRoundCount = map.get("burstRoundCount") != null ? (int) map.get("burstRoundCount") : 0;
            this.fireRate = (int) map.get("fireRate");
            this.isPrimaryGun = (boolean) map.get("isPrimaryGun");
            this.explosiveYield = map.get("explosiveYield") != null ? Float.valueOf(String.valueOf(map.get("explosiveYield"))) : 0f;

            this.bulletType = BulletType.valueOf((String) map.get("bulletType"));
            this.fireSound = SoundCache.valueOf((String) map.get("fireSound"));
            this.fireType = GunType.valueOf((String) map.get("fireType"));

            this.firePitch = Float.valueOf(String.valueOf(map.get("firePitch")));
            this.fireVolume = (int) map.get("fireVolume");

            List<String> illegalAttachmentsList = (List<String>) map.get("illegalAttachments");
            if (illegalAttachmentsList != null) {
                for (String s : illegalAttachmentsList) {
                    illegalAttachmentClassifiers.add(s.toUpperCase());
                }
            }

            this.chanceOfSpawning = (Double) map.get("chanceOfSpawning");
            this.level = (Integer) map.get("level");

            WitheredUtil.debug("Successfully loaded gun " + name);
        } catch (Exception e) {
            e.printStackTrace();
            WitheredUtil.log(Level.SEVERE, "Failed to load gun " + ((String) map.get("name")).replaceAll("&", "§"));
        }
    }

    public static GunSerializable deserialize(Map<String, Object> map) {
        GunSerializable i = new GunSerializable(map);
        guns.add(i);
        Gun g = new Gun(i.illegalAttachmentClassifiers, i.name, AmmoSerializable.getById(i.ammoId).getName(), i.modelId, i.maxAmmoCount, i.bulletDrop, i.bulletSpeed, i.bulletDamage, i.bulletHeadShotDamage, i.bulletFalloff, i.bulletSpread, i.bulletCount, i.reloadRate, i.fireRate, i.bulletsPerReload, i.ammoPerReload, i.isPrimaryGun, i.explosiveYield, i.bulletType, i.fireSound, i.fireType, i.burstRoundCount, i.firePitch, i.fireVolume);
        i.baseClass = g;
        i.addStartingAttachments(map);
        LootItem.getLootItems().add(new LootItem(Gun.getGunItemStack(g, QualityEnum.NEW), 1, i.level, i.chanceOfSpawning));
        return i;
    }

    private void addStartingAttachments(Map<String, Object> map) {
        List<String> startingAttachmentsList = (List<String>) map.get("startingAttachments");
        if (startingAttachmentsList != null) {
            for (String s : startingAttachmentsList) {
                AttachmentSerializable a = AttachmentSerializable.getByName(s);
                if (a != null) {
                    baseClass.addAttachment(a.baseClass);
                } else {
                    WitheredUtil.log(Level.SEVERE, "Unknown attachment " + s);
                }
            }
        }
    }

    public Map<String, Object> serialize() {
        return null;
    }

    public static GunSerializable getByName(String name) {
        for (GunSerializable x : guns) {
            if (ChatColor.stripColor(x.name).equalsIgnoreCase(name)) {
                return x;
            }
        }
        return null;
    }

    public static ArrayList<GunSerializable> getGuns() {
        return guns;
    }
}
