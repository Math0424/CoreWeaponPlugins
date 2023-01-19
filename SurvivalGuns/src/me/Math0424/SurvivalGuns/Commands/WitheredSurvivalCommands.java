package me.Math0424.SurvivalGuns.Commands;

import me.Math0424.SurvivalGuns.CoreWeapons.*;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WitheredSurvivalCommands implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (args.length <= 0 || !commandSender.hasPermission("survivalguns.commands.give")) {
            return false;
        }
        switch (args[0].toLowerCase()) {
            case "give":
                if (commandSender instanceof Player && args.length == 2) {
                    Player p = (Player) commandSender;
                    Inventory inv = Bukkit.createInventory(null, 54);
                    switch (args[1].toLowerCase()) {
                        case "guns":
                            for (GunSerializable i : GunSerializable.getRegistered()) {
                                inv.addItem(i.getItem());
                            }
                            p.openInventory(inv);
                            return true;
                        case "ammo":
                            for (AmmoSerializable i : AmmoSerializable.getRegistered()) {
                                inv.addItem(i.getItem());
                            }
                            p.openInventory(inv);
                            return true;
                        case "grenades":
                            for (GrenadeSerializable i : GrenadeSerializable.getRegistered()) {
                                inv.addItem(i.getItem());
                            }
                            p.openInventory(inv);
                            return true;
                        case "items":
                            for (ComponentSerializable i : ComponentSerializable.getRegistered()) {
                                ItemStack item = i.getItem();
                                item.setAmount(item.getMaxStackSize());
                                inv.addItem(item);
                            }
                            p.openInventory(inv);
                            return true;
                        case "attachments":
                            for (AttachmentSerializable i : AttachmentSerializable.getRegistered()) {
                                inv.addItem(i.getItem());
                            }
                            p.openInventory(inv);
                            return true;
                    }
                }
                return false;
            case"giveitem":
                if (args.length >= 3) {
                    Player toGive = Bukkit.getPlayer(args[1]);
                    if (toGive != null) {
                        String item = args[2];
                        int count = 1;
                        if (args.length >= 4) {
                            count = Integer.parseInt(args[3]);
                        }
                        for(IGivable givable : IGivable.givables) {
                            if (givable.getName().equals(item)) {
                                ItemStack stack = givable.getItem();
                                stack.setAmount(count);
                                toGive.getInventory().addItem(stack);
                            }
                        }
                    }
                }
                return false;
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
        if (args.length > 0 && commandSender.hasPermission("survivalguns.commands.give")) {
            String currentArg = args[args.length - 1];
            List<String> possibleArgs = new ArrayList<>();
            if (args.length == 1) {
                possibleArgs.add("giveitem");
                possibleArgs.add("give");
            } else switch (args[0].toLowerCase()) {
                case "give":
                    if (args.length <= 2) {
                        possibleArgs.add("guns");
                        possibleArgs.add("ammo");
                        possibleArgs.add("grenades");
                        possibleArgs.add("items");
                        possibleArgs.add("attachments");
                    }
                    break;
                case "giveitem":
                    if (args.length == 2) {
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            possibleArgs.add(p.getName());
                        }
                    } else if(args.length == 3) {
                        for(IGivable givable : IGivable.givables) {
                            possibleArgs.add(givable.getName());
                        }
                    }
            }
            return finish(possibleArgs, currentArg);
        }
        return null;
    }

    public static List<String> finish(List<String> possible, String args) {
        List<String> results = new ArrayList<>();
        StringUtil.copyPartialMatches(args, possible, results);
        Collections.sort(results);
        return results;
    }

}
