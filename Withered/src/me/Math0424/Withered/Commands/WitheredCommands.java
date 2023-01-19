package me.Math0424.Withered.Commands;

import me.Math0424.Withered.Core.PlayerData;
import me.Math0424.Withered.Currency.CurrencyManager;
import me.Math0424.Withered.Entities.Cars.Car;
import me.Math0424.Withered.Entities.Cars.CarData;
import me.Math0424.Withered.Entities.Mech.MechSuit;
import me.Math0424.Withered.Entities.Villagers.Banker;
import me.Math0424.Withered.Entities.Villagers.GunSmith;
import me.Math0424.Withered.Entities.Villagers.Shopkeeper;
import me.Math0424.Withered.Event.Events.EventAbstract;
import me.Math0424.Withered.Event.Events.EventType;
import me.Math0424.Withered.Files.Changeable.Config;
import me.Math0424.Withered.Inventory.InventoryManager;
import me.Math0424.Withered.Inventory.ShopkeeperInventoryInterpreter;
import me.Math0424.Withered.Loot.ItemSerializable;
import me.Math0424.Withered.Structures.StructureSerializable;
import me.Math0424.Withered.WitheredAPI.Serializable.AmmoSerializable;
import me.Math0424.Withered.WitheredAPI.Serializable.AttachmentSerializable;
import me.Math0424.Withered.WitheredAPI.Serializable.GrenadeSerializable;
import me.Math0424.Withered.WitheredAPI.Serializable.GunSerializable;
import me.Math0424.CoreWeapons.Armor.Armor;
import me.Math0424.CoreWeapons.Deployables.Deployable;
import me.Math0424.CoreWeapons.Guns.Gun.Gun;
import me.Math0424.CoreWeapons.Guns.QualityEnum;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class WitheredCommands implements CommandExecutor, TabCompleter {

    //Used for basic commands
    //not setup commands
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (commandSender instanceof Player) {
            Player p = (Player) commandSender;
            if (args.length <= 0) {
                return false;
            }
            switch (args[0].toLowerCase()) {
                case "give":
                    if (CommandHelper.hasPermission(p, "withered.commands.give") && args.length >= 2) {
                        Inventory inv = Bukkit.createInventory(null, 54);
                        switch (args[1].toLowerCase()) {
                            case "guns":
                                for (GunSerializable i : GunSerializable.getGuns()) {
                                    inv.addItem(Gun.getGunItemStack(i.baseClass, QualityEnum.NEW));
                                }
                                p.openInventory(inv);
                                return true;
                            case "ammo":
                                for (AmmoSerializable i : AmmoSerializable.getAmmo()) {
                                    ItemStack item = i.baseClass.getItemStack();
                                    item.setAmount(64);
                                    inv.addItem(item);
                                }
                                p.openInventory(inv);
                                return true;
                            case "deployables":
                                for (Deployable i : Deployable.getDeployables()) {
                                    inv.addItem(i.getDeployableItemstack());
                                }
                                p.openInventory(inv);
                                return true;
                            case "grenades":
                                for (GrenadeSerializable i : GrenadeSerializable.getGrenades()) {
                                    inv.addItem(i.baseClass.getItemStack());
                                }
                                p.openInventory(inv);
                                return true;
                            case "armor":
                                for (Armor i : Armor.getArmor()) {
                                    inv.addItem(i.getItemStack());
                                }
                                p.openInventory(inv);
                                return true;
                            case "structures":
                                for (StructureSerializable s : StructureSerializable.getStructures()) {
                                    inv.addItem(s.getItemStack());
                                }
                                p.openInventory(inv);
                                return true;
                            case "items":
                                for (ItemSerializable i : ItemSerializable.getItems()) {
                                    ItemStack item = i.getItemStack();
                                    item.setAmount(item.getMaxStackSize());
                                    inv.addItem(item);
                                }
                                p.openInventory(inv);
                                return true;
                            case "attachments":
                                for (AttachmentSerializable i : AttachmentSerializable.getAttachments()) {
                                    inv.addItem(i.getItemStack());
                                }
                                p.openInventory(inv);
                                return true;
                            case "money":
                                if (args.length >= 3) {
                                    try {
                                        int i = Integer.parseInt(args[2]);
                                        if (args.length >= 4) {
                                            if (Bukkit.getPlayer(args[3]) != null) {
                                                Player other = Bukkit.getPlayer(args[3]);
                                                PlayerData data = PlayerData.getPlayerData(other);
                                                data.addToCash(i);
                                                CurrencyManager.updateCurrency(other);
                                                other.sendMessage(ChatColor.GREEN + "You have been given " + i + " money");
                                                p.sendMessage(ChatColor.GREEN + "Gave " + other.getName() + " " + i + " money");
                                            } else {
                                                p.sendMessage(ChatColor.RED + "Unknown/Offline Player " + args[3]);
                                            }
                                        } else {
                                            PlayerData data = PlayerData.getPlayerData(p);
                                            data.addToCash(i);
                                            CurrencyManager.updateCurrency(p);
                                            p.sendMessage(ChatColor.GREEN + "Given " + i + " money");
                                        }
                                    } catch (Exception e) {
                                        p.sendMessage(ChatColor.RED + "Please enter a valid amount");
                                    }
                                } else {
                                    PlayerData data = PlayerData.getPlayerData(p);
                                    data.addToCash(Config.CURRENCYSTARTINGVALUE.getIntVal());
                                    CurrencyManager.updateCurrency(p);
                                    p.sendMessage(ChatColor.GREEN + "Given " + Config.CURRENCYSTARTINGVALUE.getIntVal() + " money");
                                }
                                return true;
                            case "xp":
                                if (args.length >= 3) {
                                    try {
                                        int i = Integer.parseInt(args[2]);
                                        if (args.length >= 4) {
                                            if (Bukkit.getPlayer(args[3]) != null) {
                                                Player other = Bukkit.getPlayer(args[3]);
                                                PlayerData data = PlayerData.getPlayerData(other);
                                                data.addXp(i);
                                                other.sendMessage(ChatColor.LIGHT_PURPLE + "You have been given " + i + " xp");
                                                p.sendMessage(ChatColor.LIGHT_PURPLE + "Gave " + other.getName() + " " + i + " xp");
                                            } else {
                                                p.sendMessage(ChatColor.RED + "Unknown/Offline Player " + args[3]);
                                            }
                                        } else {
                                            PlayerData data = PlayerData.getPlayerData(p);
                                            data.addXp(i);
                                            CurrencyManager.updateCurrency(p);
                                            p.sendMessage(ChatColor.LIGHT_PURPLE + "Given " + i + " xp");
                                        }
                                    } catch (Exception e) {
                                        p.sendMessage(ChatColor.RED + "Please enter a valid amount");
                                    }
                                } else {
                                    PlayerData data = PlayerData.getPlayerData(p);
                                    data.addXp(100);
                                    p.sendMessage(ChatColor.LIGHT_PURPLE + "Given 100 xp");
                                }
                                return true;
                        }
                    }
                    return false;
                case "summon":
                    if (CommandHelper.hasPermission(p, "withered.commands.summon") && args.length >= 2) {
                        switch (args[1].toLowerCase()) {
                            case "car":
                                Car car = new Car(p.getWorld(), new CarData("AdminCar", Material.REPEATING_COMMAND_BLOCK, 0, 100, .2, .7, .5));
                                car.spawn(p.getLocation());
                                return true;
                            case "mech":
                                new MechSuit(p.getWorld()).spawn(p.getLocation());
                                return true;
                            case "shopkeeper":
                                if (args.length >= 3 && ShopkeeperInventoryInterpreter.getShopkeeperInventories().get(args[2]) != null) {
                                    new Shopkeeper(p.getWorld(), args[2]).spawn(p.getLocation());
                                } else {
                                    p.sendMessage(ChatColor.RED + "Enter a valid shopkeeper name!");
                                }
                                return true;
                            case "banker":
                                new Banker(p.getWorld()).spawn(p.getLocation());
                                return true;
                            case "gunsmith":
                                new GunSmith(p.getWorld()).spawn(p.getLocation());
                                return true;
                        }
                    }
                    return false;
                case "activate":
                    if (CommandHelper.hasPermission(p, "withered.commands.activate") && args.length >= 2) {
                        switch (args[1].toLowerCase()) {
                            case "global":
                                switch (args[2].toLowerCase()) {
                                    case "dropcrate":
                                    case "endgamediamond":
                                    case "killcounter":
                                    case "mechsuit":
                                    case "weaponscache":
                                        if (EventAbstract.getGlobalParticipants().size() < 2) {
                                            p.sendMessage(ChatColor.RED + "cannot start event with less then 2 players!");
                                        } else {
                                            EventType.valueOf(args[2].toUpperCase()).getEventClass().activate();
                                        }
                                }
                                return true;
                            case "local":
                                switch (args[2].toLowerCase()) {
                                    case "airdrop":
                                    case "pointcapture":
                                    case "assassination":
                                    case "defend":
                                        if (EventAbstract.getPossibleParticipants().size() < 2) {
                                            p.sendMessage(ChatColor.RED + "cannot start event with less then 2 players!");
                                        } else {
                                            EventType.valueOf(args[2].toUpperCase()).getEventClass().activate();
                                        }
                                        return true;
                                }
                                return true;
                        }
                    }
                    return false;
                case "updateinv":
                    InventoryManager.updatePlayerInventory(p);
                    break;
                case "stats":
                    if (args.length == 2) {
                        if (Bukkit.getPlayer(args[1]) != null) {
                            p.sendMessage(ChatColor.YELLOW + PlayerData.getPlayerData(Bukkit.getPlayer(args[1])).toString());
                        } else {
                            p.sendMessage(ChatColor.RED + "Invalid playername!");
                        }
                    } else {
                        p.sendMessage(ChatColor.GREEN + PlayerData.getPlayerData(p).toString());
                    }
                    return false;
                case "topplayers":


                    return false;
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
            if (args.length == 1) {
                possibleArgs.add("give");
                possibleArgs.add("summon");
                possibleArgs.add("activate");
                possibleArgs.add("updateinv");
                possibleArgs.add("stats");
            } else switch (args[0].toLowerCase()) {
                case "give":
                    if (CommandHelper.hasPermission(p, "withered.commands.give") && args.length <= 2) {
                        possibleArgs.add("guns");
                        possibleArgs.add("ammo");
                        possibleArgs.add("deployables");
                        possibleArgs.add("grenades");
                        possibleArgs.add("armor");
                        possibleArgs.add("structures");
                        possibleArgs.add("items");
                        possibleArgs.add("attachments");
                        possibleArgs.add("money");
                        possibleArgs.add("xp");
                    }
                    break;
                case "summon":
                    if (CommandHelper.hasPermission(p, "withered.commands.summon")) {
                        if (args.length <= 2) {
                            possibleArgs.add("car");
                            possibleArgs.add("mech");
                            possibleArgs.add("shopkeeper");
                            possibleArgs.add("banker");
                            possibleArgs.add("gunsmith");
                        } else if (args.length <= 3 && args[1].equalsIgnoreCase("shopkeeper")) {
                            possibleArgs.addAll(ShopkeeperInventoryInterpreter.getShopkeeperInventories().keySet());
                        }
                    }
                    break;
                case "activate":
                    if (CommandHelper.hasPermission(p, "withered.commands.activate")) {
                        if (args.length == 2) {
                            possibleArgs.add("global");
                            possibleArgs.add("local");
                        } else if (args.length <= 3) {
                            switch (args[1].toLowerCase()) {
                                case "global":
                                    possibleArgs.add("dropcrate");
                                    possibleArgs.add("endgamediamond");
                                    possibleArgs.add("killcounter");
                                    possibleArgs.add("mechsuit");
                                    possibleArgs.add("weaponscache");
                                    break;
                                case "local":
                                    possibleArgs.add("airdrop");
                                    possibleArgs.add("assassination");
                                    possibleArgs.add("defend");
                                    possibleArgs.add("pointcapture");
                                    break;
                            }
                        }
                    }
                    break;

            }
            return CommandHelper.finish(possibleArgs, currentArg);
        }
        return null;
    }


}
