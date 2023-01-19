package me.Math0424.Withered.Event.Events;

import me.Math0424.Withered.Core.PlayerData;
import me.Math0424.Withered.Currency.CurrencyManager;
import me.Math0424.Withered.Event.EventManager;
import me.Math0424.Withered.Files.Changeable.Config;
import me.Math0424.Withered.Files.Changeable.Lang;
import me.Math0424.Withered.Inventory.ItemManager;
import me.Math0424.Withered.Util.WitheredUtil;
import me.Math0424.CoreWeapons.Util.MyUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class EventAbstract {

    public Integer time = 100;

    public ArrayList<Player> participants = new ArrayList<>();

    public void activate() {
        ongoingEvents.add(this);

        time = getTickTime();

        initialize();
    }

    public abstract void initialize();

    public abstract void tick();

    public abstract void OnPlayerDeath(Player killer, Player dead);

    public abstract void OnPlayerLeave(Player p);

    public abstract void OnPlayerJoin(Player p);

    public abstract void addParticipant(Player p);

    public abstract int getTickTime();

    public Integer timeInMin() {
        return time / 60 / 20;
    }

    public void cancelEvent(boolean remove) {
        if (remove) {
            ongoingEvents.remove(this);
        }
        for (Player p : participants) {
            p.sendMessage(ChatColor.RED + "Event has ended");
            ItemManager.resetCompass(p);
            ItemManager.resetWatch(p);
        }
        participants.clear();
    }

    public void reward(Player p, int xp, int money, int bank) {
        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 100, 1);
        p.sendMessage(ChatColor.LIGHT_PURPLE + "+" + xp + " XP");
        p.sendMessage(ChatColor.GREEN + "+" + money + " " + Config.CURRENCYNAME.getStrVal());
        p.sendMessage(ChatColor.DARK_GREEN + "+" + bank + " " + Lang.BASICWORDS.convert(p, 1));
        PlayerData data = PlayerData.getPlayerData(p);
        data.addToCash(money);
        data.addToBankCash(money);
        data.addXp(xp);
        CurrencyManager.updateCurrency(p);
        removePlayer(p);
    }

    public void removePlayer(Player p) {
        participants.remove(p);
        if (participants.size() == 0) {
            cancelEvent(true);
        }
        ItemManager.resetCompass(p);
        ItemManager.resetWatch(p);
    }

    public Location getRandomPoint() {
        if (EventManager.eventLocations.size() > 0) {//points == 0
            return EventManager.eventLocations.get(MyUtil.random(EventManager.eventLocations.size() - 1));
        }
        return new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
    }

    public Location getNearestPoint(Player p) {
        Location closest = new Location(p.getWorld(), 0, 0, 0);
        if (EventManager.eventLocations.size() > 0) {
            for (Location loc : EventManager.eventLocations) {
                if (p.getLocation().distance(loc) < p.getLocation().distance(closest)) {
                    closest = loc;
                }
            }
        }
        return closest;
    }

    public void sendGroupMessage(List<Player> sendTo, Lang lang, int i, String[]... replace) {
        for (Player p : sendTo) {
            String finalMessage = lang.convert(p, i);
            for (String[] s : replace) {
                finalMessage = finalMessage.replace(s[0], s[1]);
            }
            p.sendMessage(finalMessage);
        }
    }

    //static
    public static boolean isGlobalEventOngoing = false;
    public static ArrayList<EventAbstract> ongoingEvents = new ArrayList<>();

    public static ArrayList<Player> getPossibleParticipants() {
        ArrayList<Player> subjects = new ArrayList<>();
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!WitheredUtil.isInSpawn(p.getLocation()) && !isInEvent(p)) {
                subjects.add(p);
            }
        }
        return subjects;
    }

    public static ArrayList<Player> getGlobalParticipants() {
        ArrayList<Player> subjects = new ArrayList<>();
        for (Player p : Bukkit.getOnlinePlayers()) {
            subjects.add(p);
        }
        return subjects;
    }

    public static ArrayList<Player> getRandomParticipants(double frac) {
        ArrayList<Player> subjects = getPossibleParticipants();
        ArrayList<Player> returnSubjects = new ArrayList<>();
        if (subjects.size() >= 2) {
            if (subjects.size() == 2) {
                return subjects;
            }
            for (; returnSubjects.size() <= Math.max(2, (int) (subjects.size() / frac) + 1); ) {
                if (subjects.size() > 1) {
                    int rand = MyUtil.random(subjects.size() - 1);
                    returnSubjects.add(subjects.get(rand));
                    subjects.remove(rand);
                } else {
                    break;
                }
            }
        }
        return returnSubjects;
    }

    public static boolean isInEvent(Player p) {
        for (EventAbstract ev : ongoingEvents) {
            if (ev.participants.contains(p)) {
                return true;
            }
        }
        return false;
    }

    public static EventAbstract getEvent(Player p) {
        for (EventAbstract ev : ongoingEvents) {
            if (ev.participants.contains(p)) {
                return ev;
            }
        }
        return null;
    }

    public static boolean isInLocalEvent(Player p) {
        if (isInEvent(p)) {
            EventAbstract e = getEvent(p);
            for (EventType target : EventType.getLocalEvents()) {
                if (e.getClass().equals(target.getClazz())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void cancelAllEvents() {
        for (EventAbstract e : ongoingEvents) {
            e.cancelEvent(false);
        }
        ongoingEvents.clear();
    }

}
