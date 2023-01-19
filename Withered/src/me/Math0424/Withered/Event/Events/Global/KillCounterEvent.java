package me.Math0424.Withered.Event.Events.Global;

import me.Math0424.Withered.Event.Events.EventAbstract;
import me.Math0424.Withered.Files.Changeable.Lang;
import me.Math0424.Withered.Inventory.ItemManager;
import me.Math0424.Withered.Teams.ScoreboardManager;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class KillCounterEvent extends EventAbstract {

    private int lastAnnouncedTime = 0;
    private Objective objective;

    @Override
    public void initialize() {
        cancelAllEvents();

        ongoingEvents.add(this);
        isGlobalEventOngoing = true;

        ArrayList<Player> subjects = getGlobalParticipants();
        for (Player p : subjects) {
            addParticipant(p);
        }

        objective = ScoreboardManager.getMainScoreboard().registerNewObjective("Kills", "Kills");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        ItemManager.setCompassGlobalText(getRandomPoint(), Arrays.asList(Lang.KILLCOUNTEREVENTSTART.getValue(1)));

    }

    @Override
    public void tick() {
        if (lastAnnouncedTime > timeInMin()) {
            for (Player p : getGlobalParticipants()) {
                ItemManager.setWatchText(p, timeInMin(), Collections.singletonList(Lang.KILLCOUNTEREVENTINFO.convert(p, 0)));
            }
            lastAnnouncedTime = timeInMin();
        }

        if (time % 40 == 0) {
            for (Player p : getGlobalParticipants()) {
                Player closest = null;
                for (Player other : getGlobalParticipants()) {
                    if (other != p) {
                        if (closest != null) {
                            if (p.getLocation().distance(other.getLocation()) < p.getLocation().distance(closest.getLocation())) {
                                closest = other;
                            }
                        } else {
                            closest = other;
                        }
                    }
                }
                ItemManager.setCompassText(p, closest.getLocation(), Collections.singletonList(Lang.KILLCOUNTEREVENTINFO.convert(p, 1).replace("{player}", closest.getName())), false);
            }
        }

        if (time == 0) {
            Player highestPlayer = null;
            for (Player p : getGlobalParticipants()) {
                if (highestPlayer == null) {
                    if (objective.getScore(p.getName()).getScore() > 0) {
                        highestPlayer = p;
                    }
                } else {
                    if (objective.getScore(p.getName()).getScore() > objective.getScore(highestPlayer).getScore()) {
                        highestPlayer = p;
                    }
                }
                removePlayer(p);
            }
            if (highestPlayer != null) {
                for (Player p : getGlobalParticipants()) {
                    p.sendMessage(Lang.KILLCOUNTEREVENTEND.convert(p, 1).replace("{player}", highestPlayer.getName()));
                }
                reward(highestPlayer, 300, 800, 200);
            } else {
                for (Player p : getGlobalParticipants()) {
                    p.sendMessage(Lang.KILLCOUNTEREVENTEND.convert(p));
                }
            }
            ongoingEvents.remove(this);
            objective.unregister();
            isGlobalEventOngoing = false;
        }
        time--;
    }

    @Override
    public void OnPlayerDeath(Player killer, Player dead) {
        if (killer != null && dead != null) {

            System.out.println(killer.getName() + " killed " + dead.getName());

            objective.getScore(killer.getName()).setScore(objective.getScore(killer.getName()).getScore() + 1);
        }
    }

    @Override
    public void OnPlayerLeave(Player p) {
        if (participants.contains(p)) {
            removePlayer(p);
        }
    }

    @Override
    public int getTickTime() {
        return 18000;
    }

    @Override
    public void OnPlayerJoin(Player p) {
        addParticipant(p);
    }

    @Override
    public void addParticipant(Player p) {
        if (!participants.contains(p)) {
            participants.add(p);
            p.playSound(p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 100, 1);
            p.sendTitle(Lang.KILLCOUNTEREVENTSTART.convert(p, 0), Lang.KILLCOUNTEREVENTSTART.convert(p, 1), 10, 100, 10);
            p.sendMessage(Lang.KILLCOUNTEREVENTSTART.convert(p, 0));
            p.sendMessage(Lang.KILLCOUNTEREVENTSTART.convert(p, 1));
        }
    }

}
