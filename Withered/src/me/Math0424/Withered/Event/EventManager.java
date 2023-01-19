package me.Math0424.Withered.Event;

import me.Math0424.Withered.Event.Events.EventAbstract;
import me.Math0424.Withered.Event.Events.EventType;
import me.Math0424.Withered.Event.Events.Global.EndgameDiamondEvent;
import me.Math0424.Withered.Files.Changeable.Config;
import me.Math0424.Withered.Files.Changeable.Lang;
import me.Math0424.Withered.Files.FileLoader;
import me.Math0424.Withered.Files.FileSaver;
import me.Math0424.Withered.Inventory.ItemManager;
import me.Math0424.Withered.Util.WitheredUtil;
import me.Math0424.Withered.Withered;
import me.Math0424.CoreWeapons.Util.MyUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;

public class EventManager {

    public static ArrayList<Location> eventLocations = new ArrayList<>();

    private static int timeTillGlobalEvent;
    private static int timeTillNextLocalEvent;

    static {
        timeTillGlobalEvent = Config.GLOBALEVENTTIME.getIntVal();
        timeTillNextLocalEvent = Config.LOCALEVENTTIME.getIntVal();

        if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) != Config.ENDGAMEDIAMONDDAY.getIntVal()) {
            FileLoader.resetLevelData.set("hasResetToday", false);
            FileSaver.saveResetLevel();
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                timeTillGlobalEvent--;
                timeTillNextLocalEvent--;
                if (timeTillGlobalEvent == 0) {

                    if (Config.ENDGAMEDIAMONDDAY.getIntVal() != 0 && Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Config.ENDGAMEDIAMONDDAY.getIntVal() && !FileLoader.spawningData.getBoolean("hasResetToday")) {
                        if (EndgameDiamondEvent.getPossibleDiamondHolders().size() >= Math.max(2, Config.GLOBALEVENTMINPLAYERS.getIntVal())) {
                            EndgameDiamondEvent diamond = new EndgameDiamondEvent();
                            diamond.activate();
                            FileLoader.resetLevelData.set("hasResetToday", true);
                            FileSaver.saveSpawningData();
                        }
                        return;
                    }

                    EventAbstract e = EventType.getGlobalEvents().get(MyUtil.random(EventType.getGlobalEvents().size() - 1)).getEventClass();
                    if (EventAbstract.getGlobalParticipants().size() >= Math.max(2, Config.GLOBALEVENTMINPLAYERS.getIntVal())) {
                        e.activate();
                    } else {
                        WitheredUtil.debug("Global event canceled, too few players");
                    }
                    timeTillNextLocalEvent = Config.GLOBALEVENTTIME.getIntVal() + e.timeInMin();

                } else if (timeTillNextLocalEvent == 0) {
                    if (EventAbstract.getPossibleParticipants().size() > 2) {
                        EventAbstract e = EventType.getLocalEvents().get(MyUtil.random(EventType.getLocalEvents().size() - 1)).getEventClass();
                        if (e.timeInMin() < timeTillGlobalEvent) {
                            e.activate();
                        }
                    }
                    timeTillNextLocalEvent = Config.LOCALEVENTTIME.getIntVal();
                }
                ItemManager.setGlobalPlayerWatch(timeTillGlobalEvent + 1, Collections.singletonList(Lang.COMPASS.getValue(2)));
            }
        }.runTaskTimer(Withered.getPlugin(), 20 * 60, 20 * 60);
    }

    public static void OnPlayerDeath(Player killer, Player dead) {
        Iterator<EventAbstract> iter = EventAbstract.ongoingEvents.iterator();
        while (iter.hasNext()) {
            EventAbstract e = iter.next();
            e.OnPlayerDeath(killer, dead);
        }
    }

    public static void OnPlayerLeave(Player p) {
        Iterator<EventAbstract> iter = EventAbstract.ongoingEvents.iterator();
        while (iter.hasNext()) {
            EventAbstract e = iter.next();
            e.OnPlayerLeave(p);
        }
    }


}
