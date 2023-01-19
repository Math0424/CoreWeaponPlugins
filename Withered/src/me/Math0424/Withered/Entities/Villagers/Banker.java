package me.Math0424.Withered.Entities.Villagers;

import me.Math0424.Withered.Entities.MobHandler;
import me.Math0424.Withered.Files.FileSaver;
import me.Math0424.Withered.Gameplay.VillagerManagers.BankerManager;
import me.Math0424.Withered.Withered;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.scheduler.BukkitRunnable;

public class Banker extends VillagerBase {

    public Banker(World w) {
        super(w);

        Banker b = this;
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!MobHandler.getBankers().contains(b.getUniqueIDString())) {
                    MobHandler.getBankers().add(b.getUniqueIDString());
                    FileSaver.saveStaticMobData();
                }
            }
        }.runTaskLater(Withered.getPlugin(), 1);

        this.getBukkitEntity().setCustomName("Banker");
        ((Villager) this.getBukkitEntity()).setProfession(Villager.Profession.LIBRARIAN);
    }


    public void death() {
        MobHandler.removeFromBankers(this);
    }

    public void trade(Player player) {
        player.openInventory(BankerManager.createInventory(player));
    }

    public VillagerBase clazz() {
        return this;
    }

}
