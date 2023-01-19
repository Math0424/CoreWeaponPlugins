package me.Math0424.Withered.Files;

import me.Math0424.Withered.Entities.MobHandler;
import me.Math0424.Withered.Event.EventManager;
import me.Math0424.Withered.Files.Changeable.BlockConfig;
import me.Math0424.Withered.Files.Changeable.Config;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.ArrayList;

public class FileLoader {

    public static FileConfiguration englishConfig, spanishConfig, chineseConfig, germanConfig;

    //config files
    public static FileConfiguration carConfig, itemConfig, structureConfig, blockConfig, shopkeeperConfig, config;

    //data files static
    public static File signDataFile, chestDataFile, spawningDataFile, resetLevelFile;
    public static FileConfiguration signData, chestData, spawningData, resetLevelData; //spawning data vehicles, events
    //data files dynamic
    public static File mobDataFile, staticMobDataFile, structureDataFile;
    public static FileConfiguration mobData, staticMobData, structureData;

    public static int resetLevel = 0;

    public static void LoadConfig() {
        //lang
        englishConfig = FileManager.loadConfiguration("lang_en.yml", "Configs/Lang/");
        spanishConfig = FileManager.loadConfiguration("lang_sp.yml", "Configs/Lang/");
        chineseConfig = FileManager.loadConfiguration("lang_ch.yml", "Configs/Lang/");
        germanConfig = FileManager.loadConfiguration("lang_de.yml", "Configs/Lang/");

        config = FileManager.loadConfiguration("config.yml", "Configs/");
        Config.load();
    }

    public static void LoadFiles() {

        carConfig = FileManager.loadConfiguration("carConfig.yml", "Configs/");
        itemConfig = FileManager.loadConfiguration("itemConfig.yml", "Configs/");
        structureConfig = FileManager.loadConfiguration("structureConfig.yml", "Configs/");
        blockConfig = FileManager.loadConfiguration("blockConfig.yml", "Configs/");
        shopkeeperConfig = FileManager.loadConfiguration("shopkeeperConfig.yml", "Configs/");
        BlockConfig.load();

        signDataFile = FileManager.getFileInDataFolder("signData.yml", "Data/Static/");
        signData = FileManager.loadConfiguration("signData.yml", "Data/Static/");

        chestDataFile = FileManager.getFileInDataFolder("chestData.yml", "Data/Static/");
        chestData = FileManager.loadConfiguration("chestData.yml", "Data/Static/");

        spawningDataFile = FileManager.getFileInDataFolder("spawningData.yml", "Data/Static/");
        spawningData = FileManager.loadConfiguration("spawningData.yml", "Data/Static/");

        staticMobDataFile = FileManager.getFileInDataFolder("staticMobData.yml", "Data/Static/");
        staticMobData = FileManager.loadConfiguration("staticMobData.yml", "Data/Static/");

        resetLevelFile = FileManager.getFileInDataFolder("resetLevel.yml", "Data/Static/");
        resetLevelData = FileManager.loadConfiguration("resetLevel.yml", "Data/Static/");

        //dynamic
        mobDataFile = FileManager.getFileInDataFolder("mobData.yml", "Data/Dynamic/");
        mobData = FileManager.loadConfiguration("mobData.yml", "Data/Dynamic/");

        structureDataFile = FileManager.getFileInDataFolder("structureData.yml", "Data/Dynamic/");
        structureData = FileManager.loadConfiguration("structureData.yml", "Data/Dynamic/");

        //manual
        if (spawningData.get("eventLocations") != null) {
            EventManager.eventLocations = (ArrayList<Location>) spawningData.getList("eventLocations");
        }
        if (staticMobData.get("gunSmiths") != null) {
            MobHandler.gunSmiths = (ArrayList<String>) staticMobData.getList("gunSmiths");
        }
        if (staticMobData.get("bankers") != null) {
            MobHandler.bankers = (ArrayList<String>) staticMobData.getList("bankers");
        }
        if (resetLevelData.get("resetLevel") != null) {
            FileLoader.resetLevel = resetLevelData.getInt("resetLevel");
        }
        if (staticMobData.get("shopkeepers") != null) {
            ArrayList<String> strings = (ArrayList<String>) staticMobData.getList("shopkeepers");
            if (strings != null) {
                for (String s : strings) {
                    String[] x = s.split(":");
                    MobHandler.shopkeepers.put(x[0], x[1]);
                }
            }
        }

        FileManager.copyInternalFiles("Schematics");

        FileManager.copyInternalFiles("Crafting/Ammo");
        FileManager.loadMassConfigurations("Crafting/Ammo");

        FileManager.copyInternalFiles("Configs/Armor");
        FileManager.loadMassConfigurations("Configs/Armor");

        FileManager.copyInternalFiles("Crafting/Grenades");
        FileManager.loadMassConfigurations("Crafting/Grenades");

        FileManager.copyInternalFiles("Configs/Deployables");
        FileManager.loadMassConfigurations("Configs/Deployables");

        FileManager.copyInternalFiles("Crafting/Attachments");
        FileManager.loadMassConfigurations("Crafting/Attachments");

        //!!guns depend on attachments and ammo to be loaded first!!
        FileManager.copyInternalFiles("Crafting/Guns");
        FileManager.loadMassConfigurations("Crafting/Guns");

        resetLevel = spawningData.getInt("resetLevel");
    }

}