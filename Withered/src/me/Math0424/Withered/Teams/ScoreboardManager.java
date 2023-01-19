package me.Math0424.Withered.Teams;

import me.Math0424.Withered.Files.Changeable.Config;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class ScoreboardManager {

    private static final Scoreboard mainScoreboard;

    static {
        mainScoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        if (mainScoreboard.getTeam("main") == null) {
            Team t = mainScoreboard.registerNewTeam("main");
            if (!Config.NAMETAGVISIBILITY.getBoolVal()) {
                t.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
            }
        }
    }

    public static void updateHelmet(Player p) {
        p.setScoreboard(mainScoreboard);
        ItemStack item = new ItemStack(Material.LEATHER_HELMET);
        ItemMeta itemMeta = item.getItemMeta();
        LeatherArmorMeta meta = (LeatherArmorMeta) itemMeta;
        meta.setUnbreakable(true);
        meta.removeItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        if (Squad.isInSquad(p)) {
            Squad s = Squad.getPlayerSquad(p);
            meta.setDisplayName(ChatColor.DARK_GRAY + s.getName());
            meta.setColor(s.getColor());
        } else {
            meta.setDisplayName(ChatColor.GRAY + "Independents");
            meta.setColor(Color.fromRGB(140, 70, 0));
        }
        item.setItemMeta(meta);
        p.getInventory().setHelmet(item);
    }

    public static Scoreboard getMainScoreboard() {
        return mainScoreboard;
    }

    public static void setMainTeam(Player p) {
        mainScoreboard.getTeam("main").addEntry(p.getName());
    }


}
