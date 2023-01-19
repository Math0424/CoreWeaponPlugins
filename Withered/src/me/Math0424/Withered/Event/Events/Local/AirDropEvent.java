package me.Math0424.Withered.Event.Events.Local;

import me.Math0424.Withered.Chests.LevelChest;
import me.Math0424.Withered.Event.Events.EventAbstract;
import me.Math0424.Withered.Files.Changeable.Lang;
import me.Math0424.Withered.Inventory.ItemManager;
import me.Math0424.Withered.Teams.Squad;
import me.Math0424.CoreWeapons.Util.MyUtil;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class AirDropEvent extends EventAbstract {

    private Location point;
    private int lastAnnouncedTime = 0;
    private LevelChest chest;
    private boolean retrieved = false;

    @Override
    public void initialize() {

        point = getRandomPoint();

        chest = new LevelChest(point, MyUtil.random(50));
        chest.setForceNonPop(true);
        chest.setCustomName("Airdrop level " + chest.getLevel().toString());
        chest.populate(false);

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
                ItemManager.setWatchText(p, timeInMin(), Collections.singletonList(Lang.AIRDROPEVENTINFO.convert(p, 0)));
            }
            lastAnnouncedTime = timeInMin();
        }

        if (time % 10 == 0) {
            MyUtil.drawCircle(point, 10, 50, Color.PURPLE);
            if (!retrieved) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.getLocation().distance(point) < 10) {
                        p.sendMessage(Lang.AIRDROPEVENTEND.convert(p));
                        retrieved = true;
                    }
                }
            }
        }

        if (time == 0) {
            for (Player p : participants) {
                if (!retrieved) {
                    p.sendMessage(Lang.AIRDROPEVENTEND.convert(p));
                }
                point.getBlock().breakNaturally();
                removePlayer(p);
                ongoingEvents.remove(this);
            }
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
    public void OnPlayerJoin(Player p) {

    }

    @Override
    public void addParticipant(Player p) {
        if (!participants.contains(p)) {
            participants.add(p);
            p.playSound(p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 100, 1);
            p.sendTitle(Lang.AIRDROPEVENTSTART.convert(p, 0), Lang.AIRDROPEVENTSTART.convert(p, 1), 10, 100, 10);
            p.sendMessage(Lang.AIRDROPEVENTSTART.convert(p, 0));
            p.sendMessage(Lang.AIRDROPEVENTSTART.convert(p, 1));
            p.sendMessage(Lang.AIRDROPEVENTSTART.convert(p, 2).replace("{level}", chest.getLevel().toString()));
            ItemManager.setCompassText(p, point, Arrays.asList(Lang.AIRDROPEVENTINFO.convert(p, 1)));
        }
    }

    @Override
    public int getTickTime() {
        return 9600;
    }

    public void cancelEvent(boolean remove) {
        super.cancelEvent(remove);
        point.getBlock().breakNaturally();
    }


}
