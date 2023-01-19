package me.Math0424.Withered.Event.Events.Local;

import me.Math0424.Withered.Event.Events.EventAbstract;
import me.Math0424.Withered.Files.Changeable.Lang;
import me.Math0424.Withered.Inventory.ItemManager;
import me.Math0424.Withered.Teams.Squad;
import me.Math0424.CoreWeapons.Util.MyUtil;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class PointCaptureEvent extends EventAbstract {

    private Player lastClosest;
    private Location point;
    int lastAnnouncedTime = 0;

    @Override
    public void initialize() {

        point = getRandomPoint();

        ArrayList<Player> subjects = getRandomParticipants(1 / 4.0);
        for (Player p : subjects) {
            addParticipant(p);
            if (Squad.isInSquad(p)) {
                for (Player s : Squad.getPlayerSquad(p).getMembers()) {
                    addParticipant(s);
                }
            }
        }

    }

    @Override
    public void tick() {
        if (lastAnnouncedTime > timeInMin()) {
            for (Player p : participants) {
                ItemManager.setWatchText(p, timeInMin(), Collections.singletonList(Lang.POINTCAPTUREEVENTINFO.convert(p, 0)));
            }
            lastAnnouncedTime = timeInMin();
        }

        if (time % 10 == 0) {
            for (Player p : participants) {
                if (lastClosest == null || (p != lastClosest && p.getLocation().distance(point) < lastClosest.getLocation().distance(point))) {
                    p.sendMessage(Lang.POINTCAPTUREEVENTINFO.convert(p, 2));
                    if (lastClosest != null)
                        lastClosest.sendMessage(Lang.POINTCAPTUREEVENTINFO.convert(lastClosest, 3));
                    lastClosest = p;
                }
                MyUtil.drawCircle(point, 10, 50, Color.PURPLE, p);
            }
        }

        if (time == 0) {
            for (Player p : participants) {
                if (p == lastClosest) {
                    p.sendMessage(Lang.POINTCAPTUREVENTEND.convert(p, 0));
                    reward(lastClosest, 100, 100, 200);
                } else {
                    p.sendMessage(Lang.POINTCAPTUREVENTEND.convert(p, 1));
                    removePlayer(p);
                }
            }
            ongoingEvents.remove(this);
        }
        time--;
    }

    @Override
    public void OnPlayerDeath(Player killer, Player dead) {

    }

    @Override
    public void OnPlayerLeave(Player p) {
        if (participants.contains(p)) {
            removePlayer(p);
        }
    }

    public void OnPlayerJoin(Player p) {

    }

    @Override
    public void addParticipant(Player p) {
        if (!participants.contains(p)) {
            participants.add(p);
            p.playSound(p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 100, 1);
            p.sendTitle(Lang.POINTCAPTUREEVENTSTART.convert(p, 0), Lang.POINTCAPTUREEVENTSTART.convert(p, 1), 10, 100, 10);
            p.sendMessage(Lang.POINTCAPTUREEVENTSTART.convert(p, 0));
            p.sendMessage(Lang.POINTCAPTUREEVENTSTART.convert(p, 1));
            ItemManager.setCompassText(p, point, Arrays.asList(Lang.POINTCAPTUREEVENTINFO.convert(p, 1)));
        }
    }

    @Override
    public int getTickTime() {
        return 18000;
    }


}
