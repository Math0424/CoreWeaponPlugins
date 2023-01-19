package me.Math0424.Withered.Worlds;

import me.Math0424.Withered.Files.FileLoader;
import me.Math0424.Withered.Files.FileManager;
import me.Math0424.Withered.Files.FileSaver;
import me.Math0424.Withered.Util.WitheredUtil;
import me.Math0424.Withered.Withered;
import me.Math0424.CoreWeapons.Data.PlayerData;
import org.bukkit.*;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import java.io.File;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.logging.Level;

public class WorldManager {

    public static Material currentMapMaterial = Material.ANDESITE;

    static {
        for (World w : Bukkit.getWorlds()) {
            w.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
            w.setGameRule(GameRule.DISABLE_RAIDS, true);
            w.setGameRule(GameRule.DO_INSOMNIA, false);
            w.setGameRule(GameRule.DO_ENTITY_DROPS, false);
            w.setGameRule(GameRule.DO_LIMITED_CRAFTING, true);
            w.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
            w.setGameRule(GameRule.DO_MOB_LOOT, false);
            w.setGameRule(GameRule.SHOW_DEATH_MESSAGES, false);
            w.setGameRule(GameRule.DO_TRADER_SPAWNING, false);
            w.setGameRule(GameRule.DO_PATROL_SPAWNING, false);
            if (w.getDifficulty() == Difficulty.PEACEFUL) {
                w.setDifficulty(Difficulty.EASY);
            }
        }
    }

    public static void loadDefaultMap() {
        downloadIncludedWorld.runTaskAsynchronously(Withered.getPlugin());

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.kickPlayer(ChatColor.YELLOW+"Downloading world from internet,\nserver will then load the world,\nserver will then spam errors and shutdown... (this is normal)");
        }

    }

    static BukkitRunnable downloadIncludedWorld = new BukkitRunnable() {
        public void run() {
            try {
                WitheredUtil.log(Level.INFO, "Downloading default world...");
                File tempBackupWorld = new File(Withered.getPlugin().getDataFolder() + "/Data/", "tempDefaultWorld");
                File restingPlace = new File(Withered.getPlugin().getDataFolder() + "/Data/", "BackupWorlds");

                try {
                    FileUtils.copyURLToFile(new URL("https://www.dropbox.com/s/k0o36ovd9dojy7s/WitheredStarterPack.zip?dl=1"), tempBackupWorld);
                } catch (Exception e) {
                    WitheredUtil.log(Level.SEVERE, "Failed to acquire world!");
                    e.printStackTrace();
                } finally {
                    WitheredUtil.log(Level.INFO, "Successfully acquired world with a size of " + ChatColor.AQUA + new DecimalFormat("#.##").format(tempBackupWorld.length() / (1024.0 * 1024.0)) + " mb");

                    new File(Withered.getPlugin().getDataFolder() + "/Data/BackupWorlds", "Static").mkdirs();
                    new File(Withered.getPlugin().getDataFolder() + "/Data/BackupWorlds", "world").mkdirs();
                    FileManager.unzip(tempBackupWorld, restingPlace);

                    WitheredUtil.log(Level.INFO, "Finished loading the world!");

                    Bukkit.getScheduler().runTask(Withered.getPlugin(), WorldManager::loadWorldFromBackupFile);

                }
            } catch (Exception e) {
                WitheredUtil.log(Level.SEVERE, "Could not download the default world!");
                e.printStackTrace();
            }
        }
    };

    public static void loadWorldFromBackupFile() {

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.kickPlayer(ChatColor.YELLOW+"Loading world from backup file,\nserver will spam errors and shutdown... (this is normal)");
        }

        FileLoader.resetLevel++;

        PlayerData.saveAllPlayerData();
        FileSaver.saveResetLevel();

        FileManager.deleteFile(new File(Withered.getPlugin().getDataFolder(), "Data/Dynamic/"));

        for (World w : Bukkit.getWorlds()) {
            File backupWorld = new File(Withered.getPlugin().getDataFolder(), "Data/BackupWorlds/" + w.getName());
            if (Bukkit.getServer().getWorld(w.getUID()) != null) {
                File replaceWorld = new File((Bukkit.getServer().getWorld(w.getUID()).getWorldFolder().getAbsolutePath()));
                FileManager.deleteFile(replaceWorld);
                FileManager.copyFiles(backupWorld, replaceWorld);

                File backupWorldDataSource = new File(Withered.getPlugin().getDataFolder(), "Data/BackupWorlds/Static");
                File backupWorldDataDestination = new File(Withered.getPlugin().getDataFolder(), "Data/Static");
                FileManager.deleteFile(backupWorldDataDestination);
                FileManager.copyFiles(backupWorldDataSource, backupWorldDataDestination);
            }
        }

        //I hate this and everything about it, god please help me
        ((CraftServer)Bukkit.getServer()).getServer().worldServer.clear();
        Bukkit.getServer().shutdown();
    }

    public static void createWorldBackup() {
        for (World w : Bukkit.getWorlds()) {
            w.save();
            
            File backupWorld = new File(Withered.getPlugin().getDataFolder(), "Data/BackupWorlds/" + w.getName());
            File replaceWorld = new File((Bukkit.getServer().getWorld(w.getUID()).getWorldFolder().getAbsolutePath()));
            FileManager.deleteFile(backupWorld);
            FileManager.copyFiles(replaceWorld, backupWorld);

            File backupWorldDataDestination = new File(Withered.getPlugin().getDataFolder(), "Data/BackupWorlds/Static");
            File backupWorldDataSource = new File(Withered.getPlugin().getDataFolder(), "Data/Static");
            FileManager.deleteFile(backupWorldDataDestination);
            FileManager.copyFiles(backupWorldDataSource, backupWorldDataDestination);
        }
    }

}
