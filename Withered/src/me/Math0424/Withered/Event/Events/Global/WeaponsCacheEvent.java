package me.Math0424.Withered.Event.Events.Global;

import me.Math0424.Withered.Chests.LevelChest;
import me.Math0424.Withered.Event.Events.EventAbstract;
import me.Math0424.Withered.Files.Changeable.Config;
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

public class WeaponsCacheEvent extends EventAbstract {

    private Location point;
    private int lastAnnouncedTime = 0;
    private LevelChest chest;
    private boolean retrieved = false;


    @Override
    public void initialize() {
        cancelAllEvents();

        isGlobalEventOngoing = true;
        ongoingEvents.add(this);

        ArrayList<Player> subjects = getGlobalParticipants();
        for (Player p : subjects) {
            addParticipant(p);
        }


        point = getRandomPoint();

        chest = new LevelChest(point, Config.WEAPONSCACHELEVEL.getIntVal());
        chest.setForceNonPop(true);
        chest.setCustomName("WeaponsCache");
        chest.populate(false);

        ItemManager.setCompassGlobalText(point, Arrays.asList(Lang.WEAPONSCACHEEVENTINFO.getValue(1)));

    }

    @Override
    public void tick() {
        if (lastAnnouncedTime > timeInMin()) {
            for (Player p : getGlobalParticipants()) {
                ItemManager.setWatchText(p, timeInMin(), Collections.singletonList(Lang.WEAPONSCACHEEVENTINFO.convert(p, 0)));
            }
            lastAnnouncedTime = timeInMin();
        }

        if (time % 10 == 0) {
            MyUtil.drawCircle(point, 10, 50, Color.PURPLE);
            if (!retrieved) {
                for (Player p : getGlobalParticipants()) {
                    p.setCompassTarget(point);
                    if (p.getLocation().distance(point) < 10) {
                        p.sendMessage(Lang.WEAPONSCACHEEVENTEND.convert(p));
                        retrieved = true;
                    }
                }
            }
        }

        if (time == 0) {
            for (Player p : getGlobalParticipants()) {
                if (!retrieved) {
                    p.sendMessage(Lang.WEAPONSCACHEEVENTEND.convert(p));
                }
                removePlayer(p);
            }
            point.getBlock().breakNaturally();
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
    public void OnPlayerJoin(Player p) {
        addParticipant(p);
    }

    @Override
    public void addParticipant(Player p) {
        if (!participants.contains(p)) {
            participants.add(p);
            p.playSound(p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 100, 1);
            p.sendTitle(Lang.WEAPONSCACHEEVENTSTART.convert(p, 0), Lang.WEAPONSCACHEEVENTSTART.convert(p, 1), 10, 100, 10);
            p.sendMessage(Lang.WEAPONSCACHEEVENTSTART.convert(p, 0));
            p.sendMessage(Lang.WEAPONSCACHEEVENTSTART.convert(p, 1));
        }
    }

    @Override
    public int getTickTime() {
        return 18000;
    }


    public void cancelEvent(boolean remove) {
        super.cancelEvent(remove);
        if (point != null)
            point.getBlock().breakNaturally();
    }


}
