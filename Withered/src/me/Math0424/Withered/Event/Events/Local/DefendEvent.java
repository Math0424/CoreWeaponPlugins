package me.Math0424.Withered.Event.Events.Local;

import me.Math0424.Withered.Event.Events.EventAbstract;
import me.Math0424.Withered.Files.Changeable.Lang;
import me.Math0424.Withered.Inventory.ItemManager;
import me.Math0424.Withered.Teams.Squad;
import me.Math0424.CoreWeapons.Util.MyUtil;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.*;

public class DefendEvent extends EventAbstract {

    private final List<Player> defenders = new ArrayList<>();
    private final List<Player> attackers = new ArrayList<>();
    private final HashMap<Player, Integer> captureTime = new HashMap<>();

    private List<Location> sphere = new ArrayList<>();

    private Location point;
    int lastAnnouncedTime = 0;

    int timeToCapture = 15;

    @Override
    public void initialize() {

        ArrayList<Player> subjects = getRandomParticipants(1 / 4.0);
        participants.addAll(subjects);

        //defenders
        Player defender = participants.get(MyUtil.random(participants.size() - 1));
        if (Squad.isInSquad(defender)) {
            defenders.addAll((Squad.getPlayerSquad(defender).getMembers()));
        }
        defenders.add(defender);

        //attackers
        for (Player p : participants) {
            if (p != defender) {
                attackers.add(p);
            }
        }

        point = getNearestPoint(defender);

        sphere = MyUtil.generateParticleSphere(point, 30, 10);

        for (Player p : subjects) {
            ItemManager.setCompassText(p, point, Arrays.asList(Lang.DEFENDEVENTINFO.convert(p, 1)));
        }

        displayInEvent();

    }

    @Override
    public void tick() {
        if (lastAnnouncedTime > timeInMin()) {
            for (Player p : participants) {
                ItemManager.setWatchText(p, timeInMin(), Collections.singletonList(Lang.DEFENDEVENTINFO.convert(p, 0)));
            }
            lastAnnouncedTime = timeInMin();
        }

        if (time % 20 == 0) {
            for (Player p : participants) {
                for (Location loc : sphere) {
                    p.spawnParticle(Particle.REDSTONE, loc.getX(), loc.getY(), loc.getZ(), 1, new Particle.DustOptions(Color.PURPLE, 1));
                }
            }

            for (Player p : attackers) {
                if (p.getLocation().distance(point) < 10) {
                    captureTime.put(p, captureTime.get(p) - 1);
                    sendGroupMessage(defenders, Lang.DEFENDEVENTINFO, 2, new String[]{"{time}", captureTime.get(p).toString()});
                    p.sendMessage(Lang.DEFENDEVENTINFO.convert(p, 2).replace("{time}", captureTime.get(p).toString()));

                    if (captureTime.get(p) == 0) {
                        reward(p, 300, 300, 200);
                        p.sendMessage(Lang.DEFENDEVENTEND.convert(p, 3));
                        for (Player pl : attackers) {
                            pl.sendMessage(Lang.DEFENDEVENTEND.convert(pl, 3));
                            reward(pl, 100, 100, 100);
                            removePlayer(pl);
                        }
                        for (Player pl : defenders) {
                            pl.sendMessage(Lang.DEFENDEVENTEND.convert(pl, 0));
                            removePlayer(pl);
                        }
                        ongoingEvents.remove(this);
                    }

                } else {
                    captureTime.put(p, timeToCapture);
                }
            }
        }

        if (time == 0) {

            for (Player p : attackers) {
                p.sendMessage(Lang.DEFENDEVENTEND.convert(p, 1));
                removePlayer(p);
            }
            for (Player p : defenders) {
                p.sendMessage(Lang.DEFENDEVENTEND.convert(p, 2));
                reward(p, 200, 200, 200);
                removePlayer(p);
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

    @Override
    public int getTickTime() {
        return 18000;
    }

    @Override
    public void removePlayer(Player p) {
        super.removePlayer(p);
        attackers.remove(p);
        defenders.remove(p);
    }

    public void OnPlayerJoin(Player p) {

    }

    @Deprecated
    @Override
    public void addParticipant(Player p) {

    }

    private void displayInEvent() {
        for (Player p : defenders) {
            p.playSound(p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 100, 1);
            p.sendTitle(Lang.DEFENDEVENTSTART.convert(p, 0), Lang.DEFENDEVENTSTART.convert(p, 1), 10, 100, 10);
            p.sendMessage(Lang.DEFENDEVENTSTART.convert(p, 0));
            p.sendMessage(Lang.DEFENDEVENTSTART.convert(p, 1));
        }
        for (Player p : attackers) {
            p.playSound(p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 100, 1);
            p.sendTitle(Lang.DEFENDEVENTSTART.convert(p, 2), Lang.DEFENDEVENTSTART.convert(p, 3), 10, 100, 10);
            p.sendMessage(Lang.DEFENDEVENTSTART.convert(p, 2));
            p.sendMessage(Lang.DEFENDEVENTSTART.convert(p, 3));
        }
    }

}
