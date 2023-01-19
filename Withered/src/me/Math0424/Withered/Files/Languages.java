package me.Math0424.Withered.Files;

import org.bukkit.configuration.file.FileConfiguration;

public enum Languages {

    ENGLISH(FileLoader.englishConfig),
    SPANISH(FileLoader.spanishConfig),
    CHINESE(FileLoader.chineseConfig),
    GERMAN(FileLoader.germanConfig);

    private final FileConfiguration file;

    Languages(FileConfiguration file) {
        this.file = file;
    }

    public FileConfiguration getFile() {
        return file;
    }
}
