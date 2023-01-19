package me.Math0424.SurvivalGuns.Files;

import me.Math0424.SurvivalGuns.SurvivalGuns;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;

public class FileManager {

    public static void copyInternalFiles(String jarSourceLocation) {
        copyInternalFiles(jarSourceLocation, false);
    }

    public static void copyInternalFiles(String jarSourceLocation, boolean replace) {
        try {
            String resourcePath = new File(SurvivalGuns.getPlugin().getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
            JarFile jf = new JarFile(resourcePath);
            Enumeration<JarEntry> entries = jf.entries();
            while (entries.hasMoreElements()) {
                JarEntry je = entries.nextElement();
                if (je.getName().startsWith(jarSourceLocation) && !je.getName().equals(jarSourceLocation + "/")) {
                    File outFile = new File(SurvivalGuns.getPlugin().getDataFolder(), je.getName());
                    if (replace) {
                        deleteFile(outFile);
                    }
                    if (!outFile.exists()) {
                        if (outFile.isDirectory()) {
                            copyInternalFiles(jarSourceLocation);
                        } else {
                            SurvivalGuns.getPlugin().saveResource(je.getName(), false);
                        }
                    }
                }
            }
            jf.close();
        } catch (Exception e) {
            e.printStackTrace();
            SurvivalGuns.log(Level.SEVERE, "Error while copying internal files. thats not good...");
        }
    }

    public static boolean deleteFile(File toDelete) {
        if (toDelete != null) {
            if (toDelete.exists()) {
                if (toDelete.listFiles() != null) {
                    File[] files = toDelete.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        if (files[i].isDirectory()) {
                            deleteFile(files[i]);
                        } else {
                            if (!files[i].delete()) {
                                SurvivalGuns.log(Level.INFO, "Could not delete " + files[i]);
                            }
                        }
                    }
                }
                return toDelete.delete();
            }
        }
        return false;
    }

    public static FileConfiguration loadConfiguration(String fileName, String dataFolder) {
        File file = getFileInDataFolder(fileName, dataFolder);

        if (!file.exists()) {
            SurvivalGuns.getPlugin().saveResource(dataFolder + fileName, false);
        }
        FileConfiguration fileToReplace = new YamlConfiguration();
        try {
            fileToReplace.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        return fileToReplace;
    }

    public static void loadMassConfigurations(String dataFolder) {
        File file = new File(SurvivalGuns.getPlugin().getDataFolder(), dataFolder);
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                if (f.isDirectory()) {
                    loadMassConfigurations(dataFolder + f.getName());
                } else {
                    FileConfiguration tempConfig = new YamlConfiguration();
                    try {
                        tempConfig.load(f);
                    } catch (IOException | InvalidConfigurationException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }


    public static File getFileInDataFolder(String fileName, String dataFolder) {
        return new File(SurvivalGuns.getPlugin().getDataFolder(), dataFolder + fileName);
    }


}
