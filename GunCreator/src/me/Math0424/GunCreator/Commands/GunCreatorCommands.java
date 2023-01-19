package me.Math0424.GunCreator.Commands;

import me.Math0424.CoreWeapons.Armor.Armor;
import me.Math0424.CoreWeapons.Core.Container;
import me.Math0424.CoreWeapons.Grenades.Grenade.Grenade;
import me.Math0424.CoreWeapons.Guns.Ammo.Ammo;
import me.Math0424.CoreWeapons.Guns.Gun.Gun;
import me.Math0424.CoreWeapons.Util.ItemStackUtil;
import me.Math0424.CoreWeapons.Util.MyUtil;
import me.Math0424.GunCreator.GUI.MyGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;

import java.text.SimpleDateFormat;
import java.util.*;

public class GunCreatorCommands implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (commandSender instanceof Player) {
            Player p = (Player) commandSender;
            if (args.length <= 0 || !p.hasPermission("guncreator.use")) {
                return false;
            }
            switch (args[0].toLowerCase()) {
                case "stringify":
                    Container cont = Container.getContainerItem(Gun.class, p.getItemInHand());
                    if (cont == null) {
                        cont = Container.getContainerItem(Ammo.class, p.getItemInHand());
                    }
                    if (cont == null) {
                        cont = Container.getContainerItem(Grenade.class, p.getItemInHand());
                    }
                    if (cont == null) {
                        cont = Container.getContainerItem(Armor.class, p.getItemInHand());
                    }

                    if (cont != null) {
                        Map<String, Object> map = new HashMap<>();
                        cont.getObject().serialize(map);

                        ItemStack set = ItemStackUtil.visualNameClone(cont.getItemStack());
                        ItemStackUtil.setLore(set, "JSON" + cont.getObject().friendlyName(), MyUtil.serializeMap(map));

                        p.getInventory().addItem(set);
                        p.sendMessage(ChatColor.GREEN + "Basic item generated, plugins that don't support advanced item storage can now use this item for things like kits!");
                        p.sendMessage(ChatColor.AQUA + "Note that whatever plugin stores the item needs to be able to at least store item lore.");
                    } else {
                        p.sendMessage(ChatColor.RED + "Cannot stringify this item!");
                    }
                    break;
                case "create":
                    if (args.length >= 2) {
                        switch (args[1].toLowerCase()) {
                            case "gun":
                                p.sendMessage(ChatColor.GREEN + "Opening new GunCreation GUI");
                                MyGUI.resetGUI(p);
                                MyGUI.openGUI(p);
                                return true;
                            case "ammo":
                                p.sendMessage(ChatColor.GREEN + "Opening AmmoCreation GUI");
                                MyGUI.openAmmoGUI(p);
                                return true;
                        }
                    }
                    return false;
                case "resume":
                    p.sendMessage(ChatColor.GREEN + "Resuming GunCreation GUI");
                    MyGUI.openGUI(p);
                    break;
                case "edit":
                    Container<Gun> meta = Container.getContainerItem(Gun.class, p.getItemInHand());
                    if (meta != null) {
                        MyGUI.openGUI(p, meta);
                    } else {
                        p.sendMessage(ChatColor.RED + "Held item is not a gun!");
                    }
                    return false;
                case "metadata":
                    meta = Container.getContainerItem(Gun.class, p.getItemInHand());
                    if (meta != null) {
                        Gun g = meta.getObject();
                        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm");

                        String player = "None";
                        if (g.getOwner() != null) {
                            if (Bukkit.getPlayer(g.getOwner()) != null) {
                                player = Bukkit.getPlayer(g.getOwner()).getDisplayName();
                            } else {
                                player = g.getOwner().toString();
                            }
                        }

                        p.sendMessage( ChatColor.YELLOW + "Now viewing the metaData of " + g.getName() + "\n" +
                                ChatColor.GREEN + "Owner: " + ChatColor.RED + player + "\n" +
                                ChatColor.GREEN + "Quality: " + ChatColor.RED + g.getQuality() + "\n" +
                                ChatColor.GREEN + "Bulletsfired: " + ChatColor.RED + g.getTotalBulletsFired() + "\n" +
                                ChatColor.GREEN + "ShotsFired: " + ChatColor.RED + g.getTotalShotsFired() + "\n" +
                                ChatColor.GREEN + "Playerkills: " + ChatColor.RED + g.getPlayerKills() + "\n" +
                                ChatColor.GREEN + "Entitykills: " + ChatColor.RED + g.getEntityKills() + "\n" +
                                ChatColor.GREEN + "Bullets: " + ChatColor.RED + g.getCurrentShotCount() + "/" + g.getMaxShotCount() + "\n" +
                                ChatColor.GREEN + "CreationDate: " + ChatColor.RED + sdf.format(new Date(g.getCreationDate())));
                    } else {
                        p.sendMessage(ChatColor.RED + "Held item is not a gun!");
                    }
                    return false;
            }

        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
        if (args.length > 0 && commandSender instanceof Player) {
            Player p = (Player) commandSender;
            if (p.hasPermission("guncreator.use")) {
                String currentArg = args[args.length - 1];
                List<String> possibleArgs = new ArrayList<>();
                if (args.length == 1) {
                    possibleArgs.add("create");
                    possibleArgs.add("edit");
                    possibleArgs.add("metadata");
                    possibleArgs.add("stringify");
                    if (MyGUI.getPlayerInstance(p) != null) {
                        possibleArgs.add("resume");
                    }
                } else switch (args[0].toLowerCase()) {
                    case "create":
                        possibleArgs.add("gun");
                        possibleArgs.add("ammo");
                        break;
                }
                return finish(possibleArgs, currentArg);
            }
        }
        return null;
    }

    public static List<String> finish(List<String> possible, String args) {
        List<String> results = new ArrayList<String>();
        StringUtil.copyPartialMatches(args, possible, results);
        Collections.sort(results);
        return results;
    }


}
