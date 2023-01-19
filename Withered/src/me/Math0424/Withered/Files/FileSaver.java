package me.Math0424.Withered.Files;

import me.Math0424.Withered.Chests.LevelChest;
import me.Math0424.Withered.Entities.MobHandler;
import me.Math0424.Withered.Event.EventManager;
import me.Math0424.Withered.Gameplay.Cars.CarSpawnSerializable;
import me.Math0424.Withered.Signs.SignData;
import me.Math0424.Withered.Structures.Structure;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.ArrayList;

public class FileSaver {

    public static void saveStructures() {
        if (Structure.structures.isEmpty()) {
            return;
        }
        FileLoader.structureData.set("structures", Structure.structures);
        save(FileLoader.structureData, FileLoader.structureDataFile);
    }

    public static void saveMobData() {
        if (MobHandler.getCars().isEmpty() && MobHandler.getMechs().isEmpty()) {
            return;
        }
        FileLoader.mobData.set("cars", MobHandler.getCars().values().toArray());
        FileLoader.mobData.set("mechs", MobHandler.getMechs().values().toArray());
        save(FileLoader.mobData, FileLoader.mobDataFile);
    }

    public static void saveStaticMobData() {
        if (MobHandler.getShopkeepers().isEmpty() && MobHandler.getBankers().isEmpty() && MobHandler.getGunSmiths().isEmpty()) {
            return;
        }
        ArrayList<String> shopkeepers = new ArrayList<>();
        for (String s : MobHandler.getShopkeepers().keySet()) {
            shopkeepers.add(s + ":" + MobHandler.getShopkeepers().get(s));
        }
        FileLoader.staticMobData.set("shopkeepers", shopkeepers);
        FileLoader.staticMobData.set("bankers", MobHandler.getBankers());
        FileLoader.staticMobData.set("gunSmiths", MobHandler.getGunSmiths());
        save(FileLoader.staticMobData, FileLoader.staticMobDataFile);
    }

    public static void saveChests() {
        if (LevelChest.chests.isEmpty()) {
            return;
        }
        FileLoader.chestData.set("chests", LevelChest.chests);
        save(FileLoader.chestData, FileLoader.chestDataFile);
    }

    public static void saveSignData() {
        if (SignData.signData.isEmpty()) {
            return;
        }
        FileLoader.signData.set("signs", SignData.signData);
        save(FileLoader.signData, FileLoader.signDataFile);
    }

    public static void saveSpawningData() {
        if (EventManager.eventLocations.isEmpty() && CarSpawnSerializable.carSpawns.isEmpty()) {
            return;
        }
        FileLoader.spawningData.set("eventLocations", EventManager.eventLocations);
        FileLoader.spawningData.set("carSpawns", CarSpawnSerializable.carSpawns);
        save(FileLoader.spawningData, FileLoader.spawningDataFile);
    }

    public static void saveResetLevel() {
        FileLoader.resetLevelData.set("resetLevel", FileLoader.resetLevel);
        save(FileLoader.resetLevelData, FileLoader.resetLevelFile);
    }

    private static void save(FileConfiguration configuration, File file) {
        try {
            configuration.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
