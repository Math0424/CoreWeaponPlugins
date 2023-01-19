package me.Math0424.SurvivalGuns.Files;

import me.Math0424.SurvivalGuns.Files.Changeable.BlockConfig;
import me.Math0424.SurvivalGuns.Files.Changeable.Config;
import me.Math0424.SurvivalGuns.SurvivalGuns;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

public class FileLoader {

    public static FileConfiguration config, blockConfig, lang;

    public FileLoader() {
        //configs
        config = FileManager.loadConfiguration("config.yml", "Configs/");
        Config.load();

        //lang
        lang = FileManager.loadConfiguration("lang.yml", "Configs/");

        blockConfig = FileManager.loadConfiguration("blockConfig.yml", "Configs/");
        BlockConfig.load();

        if (!new File(SurvivalGuns.getPlugin().getDataFolder(), "Crafting/Guns").exists()) {
            FileManager.copyInternalFiles("Crafting/Ammo");
            FileManager.copyInternalFiles("Crafting/Components");
            FileManager.copyInternalFiles("Crafting/Attachments");
            FileManager.copyInternalFiles("Crafting/Grenades");
            FileManager.copyInternalFiles("Crafting/Guns");
        }

        FileManager.loadMassConfigurations("Crafting/Ammo");
        FileManager.loadMassConfigurations("Crafting/Components");
        FileManager.loadMassConfigurations("Crafting/Attachments");
        FileManager.loadMassConfigurations("Crafting/Grenades");
        FileManager.loadMassConfigurations("Crafting/Guns");
    }

}