package me.Math0424.Withered.Commands;

import me.Math0424.Withered.Chests.ChestManager;
import me.Math0424.Withered.Event.EventManager;
import me.Math0424.Withered.Files.FileSaver;
import me.Math0424.Withered.Gameplay.Cars.CarSpawnSerializable;
import me.Math0424.Withered.Util.WitheredUtil;
import me.Math0424.Withered.Worlds.WorldManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class WitheredSetupCommands implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (commandSender instanceof Player) {
            Player p = (Player) commandSender;
            if (args.length <= 0) {
                return false;
            }
            if (CommandHelper.hasPermission(p, "withered.commands.setup")) {
                switch (args[0].toLowerCase()) {
                    case "chestlevel":
                        if (args.length <= 2) {
                            try {
                                Integer level = Integer.parseInt(args[1]);
                                ChestManager.placeLevel.put(p, level);
                                p.sendMessage("Set place level of chests to " + level);
                            } catch (Exception e) {
                                p.sendMessage("Invalid integer!");
                            }
                        } else {
                            ChestManager.placeLevel.remove(p);
                            p.sendMessage("Switched to itemCount level placement");
                            p.sendMessage("(number of chests in hand = level)");
                        }
                        break;
                    case "setcarspawn":
                        if (args.length <= 3) {
                            try {
                                int level = Integer.parseInt(args[1]);
                                int radius = Integer.parseInt(args[2]);
                                new CarSpawnSerializable(p.getLocation(), level, radius);
                                p.sendMessage("created car spawn");
                            } catch (Exception e) {
                                p.sendMessage("Enter [level] [radius]!");
                            }
                        }
                        FileSaver.saveSpawningData();
                        break;
                    case "seteventlocation":
                        EventManager.eventLocations.add(p.getLocation());
                        FileSaver.saveSpawningData();
                        p.sendMessage("added current location to event spawn list");
                        break;
                    case "reloadchests":
                        ChestManager.repopulateAllChests();
                        p.sendMessage("done!");
                        break;
                    case "world":
                        if (args.length <= 3) {
                            switch (args[1].toLowerCase()) {
                                case "register":
                                    p.getWorld().getBlockAt(0, 0, 0).setType(Material.EMERALD_BLOCK);
                                    p.getWorld().getBlockAt(0, 1,0).setType(WorldManager.currentMapMaterial);
                                    p.sendMessage(ChatColor.GREEN + "World registered");
                                    break;
                                case "createbackup":
                                    p.sendMessage(ChatColor.RED + "This will create a backup of this world for reloading, it will delete any previous backups!");
                                    p.sendMessage(ChatColor.GREEN + "Type 'witheredsetup world CreateBackupPlease' to finalize");
                                    break;
                                case "createbackupplease":
                                    WorldManager.createWorldBackup();
                                    p.sendMessage(ChatColor.GREEN + "WorldBackup created!");
                                    break;
                                case "overridewithdefault":
                                    p.sendMessage(ChatColor.RED + "This will DELETE this current world AND the backup and replace with the included world");
                                    p.sendMessage(ChatColor.GREEN + "Type 'witheredsetup world OverrideWithDefaultPlease' to finalize");
                                    break;
                                case "overridewithdefaultplease":
                                    WorldManager.loadDefaultMap();
                                    break;
                                case "reloadfrombackupfile":
                                    p.sendMessage(ChatColor.RED + "This will DELETE this current world AND reload it from the backup world!");
                                    p.sendMessage(ChatColor.GREEN + "Type 'witheredsetup world ReloadFromBackupFilePlease' to finalize");
                                    break;
                                case "reloadfrombackupfileplease":
                                    WorldManager.loadWorldFromBackupFile();
                                    break;
                            }
                        }
                        break;
                }
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
        if (args.length > 0 && commandSender instanceof Player) {
            Player p = (Player) commandSender;
            String currentArg = args[args.length - 1];
            List<String> possibleArgs = new ArrayList<>();
            if (CommandHelper.hasPermission(p, "withered.commands.setup")) {
                if (args.length == 1) {
                    possibleArgs.add("chestlevel");
                    possibleArgs.add("setcarspawn");
                    possibleArgs.add("seteventlocation");
                    possibleArgs.add("reloadchests");
                    possibleArgs.add("world");
                } else switch (args[0].toLowerCase()) {
                    case "world":
                        possibleArgs.add("register");
                        possibleArgs.add("createbackup");
                        possibleArgs.add("overridewithdefault");
                        possibleArgs.add("reloadfrombackupfile");
                        break;
                    case "chestlevel":
                        possibleArgs.add("{level}");
                        break;
                    case "setcarspawn":
                        if (args.length == 2) {
                            possibleArgs.add("[level]");
                        } else if (args.length == 3) {
                            possibleArgs.add("[radius]");
                        }
                        break;
                }
            }
            return CommandHelper.finish(possibleArgs, currentArg);
        }
        return null;
    }


}
