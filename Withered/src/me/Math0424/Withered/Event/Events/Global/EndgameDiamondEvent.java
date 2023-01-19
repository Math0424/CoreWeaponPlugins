package me.Math0424.Withered.Event.Events.Global;

import me.Math0424.Withered.Core.PlayerData;
import me.Math0424.Withered.Event.Events.EventAbstract;
import me.Math0424.Withered.Files.Changeable.Lang;
import me.Math0424.Withered.Inventory.InventoryManager;
import me.Math0424.Withered.Inventory.ItemManager;
import me.Math0424.Withered.Util.WitheredUtil;
import me.Math0424.Withered.Withered;
import me.Math0424.Withered.Worlds.WorldManager;
import me.Math0424.CoreWeapons.Util.ItemStackUtil;
import me.Math0424.CoreWeapons.Util.MyUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class EndgameDiamondEvent extends EventAbstract {

    private static Player holder;
    private int lastAnnouncedTime = 0;
    private static final ItemStack diamond = ItemStackUtil.createItemStack(Material.DIAMOND, Lang.DIAMONDEVENTDIAMOND.getValue(0), Arrays.asList(Lang.DIAMONDEVENTDIAMOND.getValue(1), Lang.DIAMONDEVENTDIAMOND.getValue(2), Lang.DIAMONDEVENTDIAMOND.getValue(3)));
    private static boolean diamondEventOnGoing = false;

    @Override
    public void initialize() {
        cancelAllEvents();

        ongoingEvents.add(this);
        isGlobalEventOngoing = true;
        diamondEventOnGoing = true;

        ArrayList<Player> subjects = getGlobalParticipants();
        for (Player p : subjects) {
            addParticipant(p);
        }

        transferDiamondRandom();
    }

    @Override
    public void tick() {
        if (lastAnnouncedTime > timeInMin()) {
            for (Player p : getGlobalParticipants()) {
                ItemManager.setWatchText(p, timeInMin(), Collections.singletonList(Lang.DIAMONDEVENTINFO.convert(p, 0)));
            }
            lastAnnouncedTime = timeInMin();
        }

        if (time % 10 == 0) {
            ItemManager.setCompassGlobalText(holder.getLocation(), Arrays.asList(Lang.DIAMONDEVENTINFO.getValue(1).replace("{player}", holder.getName())));
        }

        if (time == 0) {
            for (Player p : getGlobalParticipants()) {
                removePlayer(p);
                p.sendMessage(Lang.DIAMONDEVENTEND.convert(p, 0).replace("{player}", holder.getName()));
            }
            reward(holder, 1000, 100, 500);
            PlayerData data = PlayerData.getPlayerData(holder);
            data.addToWins();
            new BukkitRunnable() {
                public void run() {
                    WorldManager.loadWorldFromBackupFile();
                }
            }.runTaskLater(Withered.getPlugin(), (20 * 10));

        }
        time--;
    }

    @Override
    public void OnPlayerDeath(Player killer, Player dead) {
        if (dead == holder) {
            if (killer == null) {
                transferDiamondRandom();
            } else {
                transferDiamondTo(dead, killer);
            }
        }
    }

    @Override
    public void OnPlayerLeave(Player p) {
        if (participants.contains(p)) {
            removePlayer(p);
        }
        EndgameDiamondEvent.removeDiamondIfHolder(p);
        transferDiamondRandom();
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
            p.sendTitle(Lang.DIAMONDEVENTSTART.convert(p, 0), Lang.DIAMONDEVENTSTART.convert(p, 1), 10, 100, 10);
            p.sendMessage(Lang.DIAMONDEVENTSTART.convert(p, 0));
            p.sendMessage(Lang.DIAMONDEVENTSTART.convert(p, 1));
            p.sendMessage(Lang.DIAMONDEVENTSTART.convert(p, 2));
        }
    }

    @Override
    public int getTickTime() {
        return 18000;
    }

    public static ArrayList<Player> getPossibleDiamondHolders() {
        ArrayList<Player> subjects = new ArrayList<>();
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!WitheredUtil.isInSpawn(p.getLocation())) {
                subjects.add(p);
            }
        }
        return subjects;
    }

    public static void transferDiamondRandom() {
        if (!diamondEventOnGoing) {
            return;
        }
        List<Player> list = getPossibleDiamondHolders();
        if (list.size() == 0) {
            WorldManager.loadWorldFromBackupFile();
            return;
        }
        if (holder != null) {
            removeDiamondIfHolder(holder);
        }
        //TODO: transfer message
        holder = list.get(MyUtil.random(list.size() - 1));
        holder.getInventory().setItem(22, diamond);
        holder.sendMessage(Lang.DIAMONDEVENTINFO.convert(holder, 2));
        holder.playSound(holder.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 10, 1);
    }

    public static void transferDiamondTo(Player dead, Player killer) {
        if (!diamondEventOnGoing) {
            return;
        }
        if (dead == holder) {
            removeDiamondIfHolder(dead);
            killer.getInventory().setItem(22, diamond);
            holder = killer;
            holder.sendMessage(Lang.DIAMONDEVENTINFO.convert(holder, 2));
            holder.playSound(holder.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 10, 1);
        }
    }

    public static void removeDiamondIfHolder(Player p) {
        if (!diamondEventOnGoing) {
            return;
        }
        if (p == holder) {
            p.getInventory().setItem(22, null);
            InventoryManager.updatePlayerInventory(p);
            holder = null;
        }
    }

    public static boolean isDiamondHolder(Player p) {
        return p == holder;
    }

}
