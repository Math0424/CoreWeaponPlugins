package me.Math0424.Withered.Commands;

import me.Math0424.Withered.Files.Changeable.Lang;
import me.Math0424.Withered.Teams.Squad;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SquadCommands implements CommandExecutor, TabCompleter {

    private static final ArrayList<Player> ignore = new ArrayList<>();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (commandSender instanceof Player) {
            Player p = (Player) commandSender;
            if (!CommandHelper.hasPermission(p, "withered.squad.use")) {
                return false;
            }
            if (args.length <= 0) {
                return false;
            }
            switch (args[0].toLowerCase()) {
                case "invite":
                    if (Squad.isInSquad(p)) {
                        Squad squad = Squad.getPlayerSquad(p);
                        if (squad.getOwner() == p) {
                            if (args.length >= 2) {
                                Player o = Bukkit.getPlayer(args[1]);
                                if (o != null && o.isOnline() && !ignore.contains(o) && !Squad.isInSameSquad(o, p)) {
                                    if (!squad.getInvitedPlayers().contains(0)) {
                                        squad.getInvitedPlayers().add(o);
                                        o.sendMessage(Lang.SQUADINVITE.convert(p).replace("{player}", p.getName()).replace("{squad}", squad.getName()));
                                        p.sendMessage(Lang.SQUADINVITED.convert(p).replace("{player}", o.getName()).replace("{squad}", squad.getName()));
                                    } else {
                                        p.sendMessage(Lang.SQUADINVITE.convert(p, 1));
                                    }
                                }
                            }
                        } else {
                            p.sendMessage(Lang.SQUADCOMMAND.convert(p, 2));
                        }
                    } else {
                        p.sendMessage(Lang.SQUADCOMMAND.convert(p, 1));
                    }
                    break;
                case "invites":
                    if (args.length >= 2) {
                        switch (args[1].toLowerCase()) {
                            case "ignoreall":
                                if (!ignore.contains(p)) {
                                    ignore.add(p);
                                }
                                p.sendMessage(Lang.SQUADINVITES.convert(p, 1));
                                break;
                            case "allowall":
                                ignore.remove(p);
                                p.sendMessage(Lang.SQUADINVITES.convert(p, 0));
                                break;
                            case "accept":
                                if (args.length >= 3) {
                                    if (!Squad.isInSquad(p)) {
                                        String squadName = args[2];
                                        Squad squad = Squad.getSquad(squadName);
                                        if (squad != null && squad.getInvitedPlayers().contains(p)) {
                                            squad.getInvitedPlayers().remove(p);
                                            squad.addMember(p);
                                        }
                                    } else {
                                        p.sendMessage(Lang.SQUADCOMMAND.convert(p));
                                    }
                                }
                                break;
                            case "clearinvites":
                                Squad.removeFromAllInvites(p);
                                p.sendMessage(Lang.SQUADINVITES.convert(p, 2));
                                break;
                        }
                    }
                    break;
                case "leave":
                    if (Squad.isInSquad(p)) {
                        Squad.getPlayerSquad(p).removeMember(p);
                    } else {
                        p.sendMessage(Lang.SQUADCOMMAND.convert(p, 1));
                    }
                    break;
                case "chat":
                    if (Squad.isInSquad(p)) {
                        if (args.length <= 2) {
                            StringBuilder message = new StringBuilder();
                            for (int i = 1; i < args.length; i++) {
                                message.append(" ").append(args[i]);
                            }
                            for (Player o : Squad.getPlayerSquad(p).getMembers()) {
                                o.sendMessage(ChatColor.RED + p.getName() + ":" + ChatColor.GREEN + message.toString());
                            }
                        }
                    } else {
                        p.sendMessage(Lang.SQUADCOMMAND.convert(p, 1));
                    }
                    break;
                case "create":
                    if (CommandHelper.hasPermission(p, "withered.squad.create")) {
                        if (!Squad.isInSquad(p)) {
                            new Squad(p);
                        } else {
                            p.sendMessage(Lang.SQUADCOMMAND.convert(p));
                        }
                    }
                    break;

            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
        if (args.length > 0 && commandSender instanceof Player) {
            Player p = (Player) commandSender;
            if (!CommandHelper.hasPermission(p, "withered.squad.use")) {
                return null;
            }
            String currentArg = args[args.length - 1];
            List<String> possibleArgs = new ArrayList<>();

            if (args.length == 1) {
                possibleArgs.add("invite");
                possibleArgs.add("invites");
                possibleArgs.add("leave");
                possibleArgs.add("chat");
                if (CommandHelper.hasPermission(p, "withered.squad.create")) {
                    possibleArgs.add("create");
                }
            } else switch (args[0].toLowerCase()) {
                case "chat":
                    possibleArgs.add("[message]");
                    break;
                case "invite":
                    for (Player o : p.getWorld().getPlayers()) {
                        possibleArgs.add(o.getName());
                    }
                    break;
                case "invites":
                    if (args.length >= 2) {
                        if (args.length == 2) {
                            possibleArgs.add("ignoreall");
                            possibleArgs.add("allowall");
                            possibleArgs.add("accept");
                            possibleArgs.add("clearinvites");
                        } else if (args.length <= 3) {
                            switch (args[1].toLowerCase()) {
                                case "accept":
                                    for (Squad s : Squad.getAllActiveInvites(p)) {
                                        possibleArgs.add(s.getName());
                                    }
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
