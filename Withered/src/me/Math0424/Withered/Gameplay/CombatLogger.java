package me.Math0424.Withered.Gameplay;

import me.Math0424.Withered.Files.Changeable.Lang;
import me.Math0424.Withered.Withered;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CombatLogger {

    private static final HashMap<Player, Integer> inCombat = new HashMap<>();

    static {
        new BukkitRunnable() {
            @Override
            public void run() {
                List<Player> toRemove = new ArrayList<>();
                for (Player p : inCombat.keySet()) {
                    inCombat.put(p, inCombat.get(p) - 1);
                    if (inCombat.get(p) == 0) {
                        toRemove.add(p);
                        p.sendMessage(Lang.COMBATLOGINFO.convert(p, 1));
                    }
                }
                for (Player p : toRemove) {
                    inCombat.remove(p);
                }
            }
        }.runTaskTimer(Withered.getPlugin(), 20, 20);
    }

    public static void putInCombat(Player p) {
        if (!inCombat.containsKey(p)) {
            p.sendMessage(Lang.COMBATLOGINFO.convert(p, 0));
        }
        inCombat.put(p, 20);
    }

    public static boolean isInCombat(Player p) {
        return inCombat.containsKey(p);
    }

}
