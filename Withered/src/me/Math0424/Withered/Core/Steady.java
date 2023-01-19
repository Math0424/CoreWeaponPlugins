package me.Math0424.Withered.Core;

import me.Math0424.Withered.Withered;
import me.Math0424.CoreWeapons.Util.MyUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;

public abstract class Steady {

    private Player player;
    private static final ArrayList<Steady> running = new ArrayList<>();
    private BukkitTask bukkitRunnable;

    public abstract void moved(Player p);

    public abstract void ticked(int timeRemaining);

    public abstract void complete(Player p);

    public void cancel() {
        bukkitRunnable.cancel();
        running.remove(this);
    }

    public void runTimer(Player p, int tickCount) {
        Location old = p.getLocation();
        this.player = p;
        for (Steady s : running) {
            if (p == s.player) {
                s.cancel();
                return;
            }
        }
        running.add(this);
        bukkitRunnable = new BukkitRunnable() {
            int tick = tickCount;

            @Override
            public void run() {
                tick--;
                ticked(tick);
                if (MyUtil.isSameBlockLocation(p.getLocation(), old)) {
                    if (tick == 0) {
                        complete(p);
                        Steady.this.cancel();
                    }
                } else {
                    moved(p);
                    Steady.this.cancel();
                }
            }
        }.runTaskTimer(Withered.getPlugin(), 1, 1);
    }
}
