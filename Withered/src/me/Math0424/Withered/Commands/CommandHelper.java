package me.Math0424.Withered.Commands;

import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandHelper {

    public static boolean hasPermission(Player p, String permission) {
        return (p.isOp() || p.hasPermission(permission.toLowerCase()));
    }

    public static List<String> finish(List<String> possible, String args) {
        List<String> results = new ArrayList<String>();
        StringUtil.copyPartialMatches(args, possible, results);
        Collections.sort(results);
        return results;
    }

}
