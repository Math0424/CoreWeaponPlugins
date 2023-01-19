package me.Math0424.Withered.Event.Events.Global;

import me.Math0424.Withered.Entities.Mech.MechSuit;
import me.Math0424.Withered.Event.Events.EventAbstract;
import me.Math0424.Withered.Files.Changeable.Lang;
import me.Math0424.Withered.Inventory.ItemManager;
import me.Math0424.CoreWeapons.Util.MyUtil;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class MechSuitEvent extends EventAbstract {

    private Location point;
    private int lastAnnouncedTime = 0;
    private boolean retrieved = false;
    private MechSuit mech;

    @Override
    public void initialize() {
        cancelAllEvents();

        ongoingEvents.add(this);
        isGlobalEventOngoing = true;

        ArrayList<Player> subjects = getGlobalParticipants();
        for (Player p : subjects) {
            addParticipant(p);
        }

        point = getRandomPoint();

        mech = new MechSuit(point.getWorld());
        mech.spawn(point);

        ItemManager.setCompassGlobalText(point, Arrays.asList(Lang.MECHSUITEVENTINFO.getValue(1)));

    }

    @Override
    public void tick() {
        if (lastAnnouncedTime > timeInMin()) {
            for (Player p : getGlobalParticipants()) {
                ItemManager.setWatchText(p, timeInMin(), Collections.singletonList(Lang.MECHSUITEVENTINFO.convert(p, 0)));
            }
            lastAnnouncedTime = timeInMin();
        }

        if (time % 10 == 0) {
            MyUtil.drawCircle(point, 10, 50, Color.PURPLE);
            if (!retrieved) {
                for (Player p : getGlobalParticipants()) {
                    if (p.getLocation().distance(point) < 10) {
                        p.sendMessage(Lang.MECHSUITEVENTEND.convert(p));
                        retrieved = true;
                    }
                }
            }
        }

        if (time == 0) {
            for (Player p : getGlobalParticipants()) {
                if (!retrieved) {
                    p.sendMessage(Lang.MECHSUITEVENTEND.convert(p));
                }
                mech.getBukkitEntity().remove();
                removePlayer(p);
            }
            ongoingEvents.remove(this);
            isGlobalEventOngoing = false;
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
    public void OnPlayerJoin(Player p) {
        addParticipant(p);
    }

    @Override
    public void addParticipant(Player p) {
        if (!participants.contains(p)) {
            participants.add(p);
            p.playSound(p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 100, 1);
            p.sendTitle(Lang.MECHSUITEVENTSTART.convert(p, 0), Lang.MECHSUITEVENTSTART.convert(p, 1), 10, 100, 10);
            p.sendMessage(Lang.MECHSUITEVENTSTART.convert(p, 0));
            p.sendMessage(Lang.MECHSUITEVENTSTART.convert(p, 1));
        }
    }


}
