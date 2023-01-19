package me.Math0424.Withered.Commands;

import me.Math0424.Withered.Core.PlayerData;
import me.Math0424.Withered.Files.Changeable.Lang;
import me.Math0424.Withered.Files.Languages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;


public class LanguageCommand implements CommandExecutor, TabCompleter {


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (commandSender instanceof Player) {
            Player p = (Player) commandSender;
            PlayerData data = PlayerData.getPlayerData(p);
            if (args.length == 1) {
                try {
                    data.setLang(Languages.valueOf(args[0].toUpperCase()));
                    p.sendMessage(Lang.CHANGELANGUAGE.convert(p, 1) + data.getLang().name().toLowerCase());
                } catch (Exception e) {
                    p.sendMessage(Lang.CHANGELANGUAGE.convert(p, 0) + data.getLang().name().toLowerCase());
                }
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
        if (args.length > 0 && commandSender instanceof Player) {
            String currentArg = args[args.length - 1];
            List<String> possibleArgs = new ArrayList<>();
            for (Languages l : Languages.values()) {
                possibleArgs.add(l.name().toLowerCase());
            }
            return CommandHelper.finish(possibleArgs, currentArg);
        }
        return null;
    }


}
