package me.Math0424.Withered.Event.Events.Local;

import me.Math0424.Withered.Event.Events.EventAbstract;
import me.Math0424.Withered.Files.Changeable.Lang;
import me.Math0424.Withered.Inventory.ItemManager;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class AssassinationEvent extends EventAbstract {

    private final HashMap<Player, Player> targets = new HashMap<>();
    int lastAnnouncedTime = 0;

    @Override
    public void initialize() {

        ArrayList<Player> subjects = getRandomParticipants(1 / 4.0);
        for (Player p : subjects) {
            addParticipant(p);
        }

        for (int i = 0; i < subjects.size() - 1; ++i) {
            if (i == subjects.size() - 1) {
                targets.put(subjects.get(i), subjects.get(0));
            } else {
                targets.put(subjects.get(i), subjects.get(i + 1));
            }
        }

    }

    @Override
    public void tick() {
        if (lastAnnouncedTime > timeInMin()) {
            for (Player p : targets.keySet()) {
                ItemManager.setWatchText(p, timeInMin(), Collections.singletonList(Lang.ASSASSINATIONEVENTINFO.convert(p, 0)));
            }
            lastAnnouncedTime = timeInMin();
        }
        if (time % 60 == 0) {
            for (Player p : targets.keySet()) {
                ItemManager.setCompassText(p, targets.get(p).getLocation(), Arrays.asList(Lang.ASSASSINATIONEVENTINFO.convert(p, 1).replace("{player}", targets.get(p).getName())));
            }
        }
        if (time == 0) {
            for (Player p : targets.keySet()) {
                p.sendMessage(Lang.ASSASSINATIONEVENTEND.convert(p, 2));
                removePlayer(p);
            }
            ongoingEvents.remove(this);
        }
        time--;
    }

    @Override
    public void OnPlayerDeath(Player killer, Player dead) {
        for (Player assassin : targets.keySet()) {
            if (killer != null && killer == assassin && targets.get(assassin) == dead) {
                assassin.sendMessage(Lang.ASSASSINATIONEVENTEND.convert(assassin, 0));
                reward(assassin, 100, 400, 400);
                removePlayer(assassin);
                return;
            } else if (targets.get(assassin) == dead) {
                assassin.sendMessage(Lang.ASSASSINATIONEVENTEND.convert(assassin, 1));
                removePlayer(assassin);
                return;
            }
        }
    }

    @Override
    public void OnPlayerLeave(Player p) {
        for (Player assassin : targets.keySet()) {
            if (targets.get(assassin) == p) {
                assassin.sendMessage(Lang.ASSASSINATIONEVENTEND.convert(assassin, 2));
                reward(assassin, 50, 50, 200);
            }
        }
        if (participants.contains(p)) {
            removePlayer(p);
        }
    }

    @Override
    public int getTickTime() {
        return 18000;
    }

    @Override
    public void removePlayer(Player p) {
        super.removePlayer(p);
        targets.remove(p);
    }

    public void OnPlayerJoin(Player p) {

    }

    @Override
    public void addParticipant(Player p) {
        if (!participants.contains(p)) {
            participants.add(p);
            p.playSound(p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 100, 1);
            p.sendTitle(Lang.ASSASSINATIONEVENTSTART.convert(p, 0), Lang.ASSASSINATIONEVENTSTART.convert(p, 1), 10, 100, 10);
            p.sendMessage(Lang.ASSASSINATIONEVENTSTART.convert(p, 0));
            p.sendMessage(Lang.ASSASSINATIONEVENTSTART.convert(p, 1));
        }
    }

}
