package me.Math0424.Withered.Entities.Mech;

import me.Math0424.Withered.Entities.MobHandler;
import me.Math0424.Withered.Files.Changeable.Config;
import me.Math0424.Withered.Files.Changeable.Lang;
import me.Math0424.Withered.Packets.PacketHandler;
import me.Math0424.Withered.Util.WitheredUtil;
import me.Math0424.Withered.WitheredAPI.Serializable.AmmoSerializable;
import me.Math0424.Withered.WitheredAPI.Serializable.GunSerializable;
import me.Math0424.CoreWeapons.Guns.Gun.Gun;
import me.Math0424.CoreWeapons.Guns.QualityEnum;
import me.Math0424.CoreWeapons.Util.InventoryUtil;
import me.Math0424.CoreWeapons.Util.ItemStackUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class MechData implements ConfigurationSerializable {

    private static final HashMap<Player, MechData> inMech = new HashMap<>();

    private String uuid;

    private Player player;
    public MechSuit mechSuit;
    private Float mechHealth;

    private Inventory mainInv;

    private Double playerHealth;
    private int playerFood;

    public MechData(MechSuit mechSuit) {
        this.mechSuit = mechSuit;
        this.uuid = mechSuit.getUniqueIDString();
        this.mechHealth = Config.MECHSUITHEALTH.getIntVal().floatValue();
        mechSuit.setHealth(mechHealth);
        mainInv = getDefaultInventory();
    }

    private Inventory getDefaultInventory() {
        Inventory inv = Bukkit.createInventory(null, 54);
        Gun primaryMechGun = GunSerializable.getByName(Config.MECHSUITPRIMARYGUN.getStrVal()).baseClass;
        Gun secondaryMechGun = GunSerializable.getByName(Config.MECHSUITSECONDARYGUN.getStrVal()).baseClass;
        if (primaryMechGun != null) {
            inv.setItem(0, Gun.getGunItemStack(primaryMechGun, QualityEnum.NEW));
        }
        if (secondaryMechGun != null) {
            inv.setItem(1, Gun.getGunItemStack(secondaryMechGun, QualityEnum.NEW));
            AmmoSerializable secondaryAmmo = AmmoSerializable.getByName(secondaryMechGun.getAmmoId());
            if (secondaryAmmo != null) {
                ItemStack ammoStack = secondaryAmmo.baseClass.getItemStack();
                ammoStack.setAmount(64);
                inv.setItem(7, ammoStack);
            }
        }
        inv.setItem(8, ItemStackUtil.createItemStack(Material.GLISTERING_MELON_SLICE, Lang.MECHINFO.getValue(2), 1));
        return inv;
    }

    public void setPlayer(Player player) {
        this.player = player;
        player.closeInventory();
        rotateInventory(player.getInventory(), mainInv);
        this.playerFood = player.getFoodLevel();
        this.playerHealth = player.getHealth();
        inMech.put(player, this);
        mechHealth = mechSuit.getHealth();
        player.teleport(mechSuit.getBukkitEntity().getLocation());
        player.setFoodLevel(6);
        player.setHealth(20);
        player.sendMessage(Lang.MECHINFO.convert(player, 0));
        player.setWalkSpeed(.08f);
        player.setSprinting(false);
        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_PISTON_CONTRACT, 3, 1);
        mechSuit.getBukkitEntity().remove();
        PacketHandler.changeToMech(player);
    }

    public void removePlayer(Boolean spawn) {
        player.closeInventory();
        rotateInventory(player.getInventory(), mainInv);
        player.setHealth(playerHealth);
        player.setFoodLevel(playerFood);
        inMech.remove(player);
        player.sendMessage(Lang.MECHINFO.convert(player, 1));
        player.setExp(0);
        player.setLevel(0);
        player.setWalkSpeed(.2f);
        player.removePotionEffect(PotionEffectType.SPEED);
        if (spawn) {
            mechSuit.spawn(player.getLocation()).setHealth(mechHealth);
            player.getWorld().playSound(player.getLocation(), Sound.BLOCK_PISTON_EXTEND, 3, 1);
        }
        PacketHandler.changeToPlayer(player);
        this.player = null;
    }

    public void removePlayer() {
        removePlayer(true);
    }

    private void rotateInventory(Inventory inv1, Inventory inv2) {
        Inventory tempInv = Bukkit.createInventory(null, 54);
        for (int i = 0; i < tempInv.getSize(); i++) {
            tempInv.setItem(i, inv1.getItem(i));
            inv1.setItem(i, inv2.getItem(i));
        }
        for (int i = 0; i < tempInv.getSize(); i++) {
            inv2.setItem(i, tempInv.getItem(i));
        }
    }

    public static boolean isInMech(Player p) {
        return inMech.containsKey(p);
    }

    public static HashMap<Player, MechData> getInMech() {
        return inMech;
    }

    public Float getMechHealth() {
        return mechHealth;
    }

    public void setMechHealth(Float mechHealth) {
        this.mechHealth = mechHealth;
    }

    public MechData(Map<String, Object> map) {
        try {
            this.uuid = (String) map.get("uuid");
            this.mechHealth = Float.valueOf(String.valueOf(map.get("mechHealth")));
            this.mainInv = InventoryUtil.fromString((String) map.get("mainInv"));

            WitheredUtil.debug("Successfully loaded mechData with uuid of " + uuid);
        } catch (Exception e) {
            e.printStackTrace();
            WitheredUtil.log(Level.SEVERE, "Failed to load mechData with uuid of " + uuid);
        }
    }

    public static MechData deserialize(Map<String, Object> map) {
        MechData m = new MechData(map);
        MobHandler.getMechs().put(m.uuid, m);
        return m;
    }

    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("uuid", uuid);
        map.put("mechHealth", mechHealth);
        map.put("mainInv", InventoryUtil.toString(mainInv));
        return map;
    }

}
