package me.Math0424.Withered.Entities;

import me.Math0424.Withered.Entities.Cars.Car;
import me.Math0424.Withered.Entities.Cars.CarData;
import me.Math0424.Withered.Entities.Mech.MechData;
import me.Math0424.Withered.Entities.Mech.MechSuit;
import me.Math0424.Withered.Entities.Villagers.Banker;
import me.Math0424.Withered.Entities.Villagers.GunSmith;
import me.Math0424.Withered.Entities.Villagers.Shopkeeper;
import me.Math0424.Withered.Files.FileSaver;
import me.Math0424.Withered.Util.WitheredUtil;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftIronGolem;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftMinecartRideable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Villager;
import org.bukkit.event.world.ChunkLoadEvent;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class MobHandler {

    public static ArrayList<String> bankers = new ArrayList<>();
    public static ArrayList<String> gunSmiths = new ArrayList<>();
    public static HashMap<String, String> shopkeepers = new HashMap<>();

    private static final HashMap<String, CarData> cars = new HashMap<>();
    private static final HashMap<String, MechData> mechs = new HashMap<>();

    //used in case of duplicate entities, is cleared every 2 ticks
    public static List<UUID> loadedThisTick = new ArrayList<>();

    public static void chunkLoadEvent(ChunkLoadEvent e) {
        for (Entity ent : e.getChunk().getEntities()) {
            changeIfCustom(ent);
        }
    }

    public static void changeIfCustom(Entity ent) {
        if (loadedThisTick.contains(ent.getUniqueId())) {
            ent.remove();
            return;
        }
        loadedThisTick.add(ent.getUniqueId());
        if (ent instanceof Minecart && cars.containsKey(ent.getUniqueId().toString())) {
            EntityMinecartAbstract oldEntity = ((CraftMinecartRideable) ent).getHandle();
            Car newEntity = new Car(oldEntity.getBukkitEntity().getWorld(), cars.get(ent.getUniqueId().toString())).spawn(ent.getLocation());
            newEntity.setDamage(oldEntity.getDamage());
            oldEntity.getBukkitEntity().remove();
            WitheredUtil.debug("Replaced car at " + ent.getLocation());
        } else if (ent instanceof Villager && bankers.contains(ent.getUniqueId().toString())) {
            Villager oldEntity = ((Villager) ent);

            Banker b = new Banker(ent.getWorld());
            copyAttributes(b, (EntityLiving) ((CraftEntity) oldEntity).getHandle());
            b.spawn(ent.getLocation());

            WitheredUtil.debug("Replaced banker at " + ent.getLocation());
        } else if (ent instanceof Villager && shopkeepers.containsKey(ent.getUniqueId().toString())) {
            Villager oldEntity = ((Villager) ent);

            Shopkeeper s = new Shopkeeper(ent.getWorld(), shopkeepers.get(ent.getUniqueId().toString()));
            copyAttributes(s, (EntityLiving) ((CraftEntity) oldEntity).getHandle());
            s.spawn(ent.getLocation());

            WitheredUtil.debug("Replaced shopkeeper at " + ent.getLocation());
        } else if (ent instanceof Villager && gunSmiths.contains(ent.getUniqueId().toString())) {
            Villager oldEntity = ((Villager) ent);

            GunSmith g = new GunSmith(ent.getWorld());
            copyAttributes(g, (EntityLiving) ((CraftEntity) oldEntity).getHandle());
            g.spawn(ent.getLocation());

            WitheredUtil.debug("Replaced gunSmith at " + ent.getLocation());
        } else if (ent instanceof IronGolem && mechs.containsKey(ent.getUniqueId().toString())) {
            EntityIronGolem oldEntity = ((CraftIronGolem) ent).getHandle();
            MechSuit newEntity = new MechSuit(ent.getWorld()).spawn(ent.getLocation());
            copyAttributes(newEntity, oldEntity);
            WitheredUtil.debug("Replaced mech at " + ent.getLocation());
        }
    }

    public static void removeFromCars(Car c) {
        cars.remove(c.getUniqueIDString());
        FileSaver.saveMobData();
    }

    public static void removeFromMechs(MechSuit m) {
        mechs.remove(m.getUniqueIDString());
        FileSaver.saveMobData();
    }

    public static void removeFromBankers(Banker b) {
        bankers.remove(b.getUniqueIDString());
        FileSaver.saveStaticMobData();
    }

    public static void removeFromGunSmiths(GunSmith b) {
        gunSmiths.remove(b.getUniqueIDString());
        FileSaver.saveStaticMobData();
    }

    public static void removeFromShopkeepers(Shopkeeper s) {
        bankers.remove(s.getUniqueIDString());
        FileSaver.saveStaticMobData();
    }

    private static Field attributeMap;

    static {
        try {
            attributeMap = EntityLiving.class.getDeclaredField("attributeMap");
            attributeMap.setAccessible(true);
        } catch (NoSuchFieldException ignore) {
            WitheredUtil.log(Level.SEVERE, "Plugin failed to access the attributeMap of a LivingEntity!");
        }
    }

    public static void copyAttributes(EntityLiving newEntity, EntityLiving oldEntity) {
        try {
            attributeMap.set(newEntity, attributeMap.get(oldEntity));
            newEntity.v(oldEntity);
            newEntity.setHealth(oldEntity.getHealth());
            oldEntity.getBukkitEntity().remove();
        } catch (IllegalAccessException ignore) {
            WitheredUtil.log(Level.SEVERE, "Plugin failed to copy attributes of a " + newEntity.getClass().getName());
        }
    }

    public static ArrayList<String> getBankers() {
        return bankers;
    }

    public static HashMap<String, String> getShopkeepers() {
        return shopkeepers;
    }

    public static ArrayList<String> getGunSmiths() {
        return gunSmiths;
    }

    public static HashMap<String, CarData> getCars() {
        return cars;
    }

    public static HashMap<String, MechData> getMechs() {
        return mechs;
    }
}
