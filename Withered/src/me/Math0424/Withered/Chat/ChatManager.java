package me.Math0424.Withered.Chat;

import me.Math0424.Withered.Core.PlayerData;
import me.Math0424.Withered.Files.Changeable.Config;
import me.Math0424.Withered.Teams.Squad;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatManager {


    public static void rotateChannel(Player p) {
        PlayerData data = PlayerData.getPlayerData(p);
        switch (data.getTalkType()) {
            case GLOBAL:
                data.setTalkType(TalkType.LOCAL);
                break;
            case LOCAL:
                if (Squad.isInSquad(p)) {
                    data.setTalkType(TalkType.SQUAD);
                } else if (p.hasPermission("withered.chat.admin")) {
                    data.setTalkType(TalkType.ADMIN);
                } else {
                    data.setTalkType(TalkType.GLOBAL);
                }
                break;
            case SQUAD:
                if (p.hasPermission("withered.chat.admin")) {
                    data.setTalkType(TalkType.ADMIN);
                } else {
                    data.setTalkType(TalkType.GLOBAL);
                }
                break;
            case ADMIN:
                data.setTalkType(TalkType.GLOBAL);
                break;
        }
    }

    public static void rotateChannelFromSquadKick(Player p) {
        PlayerData data = PlayerData.getPlayerData(p);
        if (data.getTalkType() == TalkType.SQUAD) {
            rotateChannel(p);
        }
    }

    public static void asyncPlayerChatEvent(AsyncPlayerChatEvent e) {
        e.setCancelled(true);
        PlayerData data = PlayerData.getPlayerData(e.getPlayer());
        String message = getChatMessage(data, e.getPlayer(), e.getMessage());
        System.out.println(message);
        switch (data.getTalkType()) {
            case ADMIN:
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.hasPermission("withered.chat.admin") || p.isOp()) {
                        p.sendMessage(ChatColor.GREEN + message);
                    }
                }
                break;
            case GLOBAL:
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.sendMessage(message);
                }
                break;
            case LOCAL:
                for (Player p : e.getPlayer().getWorld().getPlayers()) {
                    if (p.getLocation().distance(e.getPlayer().getLocation()) < Config.LOCALCHATRANGE.getIntVal()) {
                        p.sendMessage(message);
                    }
                }
                break;
            case SQUAD:
                for (Player p : Squad.getPlayerSquad(e.getPlayer()).getMembers()) {
                    p.sendMessage(ChatColor.RED + e.getPlayer().getName() + ": " + ChatColor.GREEN + e.getMessage());
                }
                break;
        }
    }

    private static String getChatMessage(PlayerData data, Player p, String message) {
        return ChatColor.translateAlternateColorCodes('&', Config.CHATFORMAT.getStrVal())
                .replace("[player]", p.getName())
                .replace("[message]", message)
                .replace("[squad]", Squad.getPlayerSquad(p) == null ? "" : " [" + Squad.getPlayerSquad(p).getName() + "]")
                .replace("[type]", data.getTalkType().name().substring(0, 1))
                .replace("[level]", getLevelColor(data.getLevel()) + String.valueOf(data.getLevel()) + ChatColor.RESET);
    }

    private static ChatColor getLevelColor(int i) {
        int level = i / 10;
        switch (level) {
            case 10:
                return ChatColor.AQUA;
            case 9:
                return ChatColor.DARK_AQUA;
            case 8:
                return ChatColor.LIGHT_PURPLE;
            case 7:
                return ChatColor.DARK_PURPLE;
            case 6:
                return ChatColor.YELLOW;
            case 5:
                return ChatColor.BLUE;
            case 4:
                return ChatColor.DARK_BLUE;
            case 3:
                return ChatColor.RED;
            case 2:
                return ChatColor.DARK_RED;
            case 1:
                return ChatColor.GREEN;
            case 0:
                return ChatColor.DARK_GREEN;
        }
        return ChatColor.GOLD;
    }

}
