package me.Math0424.Withered.Event.Events.Global;

import me.Math0424.Withered.Chests.LevelChest;
import me.Math0424.Withered.Event.Events.EventAbstract;
import me.Math0424.Withered.Files.Changeable.Config;
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

public class DropCrateEvent extends EventAbstract {

    private Location point;
    private int lastAnnouncedTime = 0;
    private LevelChest chest;
    private boolean retrieved = false;
    private Player controller = null;

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

        chest = new LevelChest(point, Config.DROPCRATELEVEL.getIntVal());
        chest.setForceNonPop(true);
        chest.setCustomName("DropCrate");

        ItemManager.setCompassGlobalText(point, Arrays.asList(Lang.DROPCRATEEVENTINFO.getValue(1)));

    }

    @Override
    public void tick() {
        if (lastAnnouncedTime > timeInMin()) {
            for (Player p : getGlobalParticipants()) {
                ItemManager.setWatchText(p, timeInMin(), Collections.singletonList(Lang.DROPCRATEEVENTINFO.convert(p, 0)));
            }
            lastAnnouncedTime = timeInMin();
        }

        if (time % 10 == 0) {
            MyUtil.drawCircle(point, 10, 50, Color.PURPLE);
            if (!retrieved) {
                for (Player p : getGlobalParticipants()) {
                    if (p.getLocation().distance(point) < 100) {
                        p.setCompassTarget(point);
                        if (controller == null) {
                            boolean newController = true;
                            for (Player other : getGlobalParticipants()) {
                                if (other != p && other.getLocation().distance(point) < 100) {
                                    if (!Squad.isInSameSquad(other, p)) {
                                        newController = false;
                                    }
                                }
                            }
                            if (newController) {
                                if (Squad.isInSquad(p)) {
                                    controller = Squad.getPlayerSquad(p).getOwner();
                                    sendGroupMessage(getGlobalParticipants(), Lang.DROPCRATEEVENTINFO, 2, new String[]{"{squad}", Squad.getPlayerSquad(p).getName()});
                                } else {
                                    controller = p;
                                    sendGroupMessage(getGlobalParticipants(), Lang.DROPCRATEEVENTINFO, 3, new String[]{"{player}", p.getName()});
                                }
                            }
                        } else {
                            if (Squad.isInSquad(p)) {
                                if (!Squad.isInSameSquad(p, controller)) {
                                    controller = null;
                                    sendGroupMessage(getGlobalParticipants(), Lang.DROPCRATEEVENTINFO, 4);
                                }
                            } else if (controller != p) {
                                controller = null;
                                sendGroupMessage(getGlobalParticipants(), Lang.DROPCRATEEVENTINFO, 4);
                            }
                        }

                    }
                }
            }
        }

        if (time > 3600 && time < 3800) {
            MyUtil.drawColoredLine(point, point.clone().add(0, 255, 0), Color.RED, 1);
        }

        if (time == 3600) {
            chest.populate(false);
        }

        if (time < 3600 && !retrieved) {
            for (Player p : getGlobalParticipants()) {
                if (p.getLocation().distance(point) < 10) {
                    p.sendMessage(Lang.DROPCRATEEVENTEND.convert(p));
                    retrieved = true;
                }
            }
        }

        if (time == 0) {
            for (Player p : getGlobalParticipants()) {
                if (!retrieved) {
                    p.sendMessage(Lang.DROPCRATEEVENTEND.convert(p));
                }
                removePlayer(p);
            }
            ongoingEvents.remove(this);
            isGlobalEventOngoing = false;
            point.getBlock().breakNaturally();
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
        return 3600 + (Config.DROPCRATESPAWNTIME.getIntVal() * 60 * 20);
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
            p.sendTitle(Lang.DROPCRATEEVENTSTART.convert(p, 0), Lang.DROPCRATEEVENTSTART.convert(p, 1), 10, 100, 10);
            p.sendMessage(Lang.DROPCRATEEVENTSTART.convert(p, 0));
            p.sendMessage(Lang.DROPCRATEEVENTSTART.convert(p, 1));
        }
    }

    public void cancelEvent(boolean remove) {
        super.cancelEvent(remove);
        if (point != null)
            point.getBlock().breakNaturally();
    }

}
