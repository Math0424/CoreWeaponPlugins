package me.Math0424.Withered.Commands;

import me.Math0424.Withered.Entities.Mech.MechData;
import me.Math0424.Withered.Files.Changeable.Lang;
import me.Math0424.Withered.Util.WitheredUtil;
import me.Math0424.CoreWeapons.DamageHandler.DamageExplainer;
import me.Math0424.CoreWeapons.DamageHandler.DamageUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class SuicideCommand implements CommandExecutor {

    public static HashMap<Player, Long> lastDeath = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (lastDeath.get(p) != null && TimeUnit.MILLISECONDS.toSeconds(500000 + lastDeath.get(p) - System.currentTimeMillis()) > 0) {
                p.sendMessage(Lang.ERRORS.convert(p, 1).replace("{time}", String.valueOf(TimeUnit.MILLISECONDS.toSeconds(500000 + lastDeath.get(p) - System.currentTimeMillis()))));
            } else if (!WitheredUtil.isInSpawn(p.getLocation()) && !MechData.isInMech(p)) {
                DamageUtil.setDamage(200.0, p, p, null, DamageExplainer.SUICIDE);
                lastDeath.put(p, System.currentTimeMillis());
            } else {
                p.sendMessage(ChatColor.RED + Lang.ERRORS.convert(p));
            }
        }
        return false;
    }

}
