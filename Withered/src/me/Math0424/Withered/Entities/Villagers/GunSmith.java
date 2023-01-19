package me.Math0424.Withered.Entities.Villagers;

import me.Math0424.Withered.Entities.MobHandler;
import me.Math0424.Withered.Files.FileSaver;
import me.Math0424.Withered.Gameplay.VillagerManagers.GunSmithManager;
import me.Math0424.Withered.Withered;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.scheduler.BukkitRunnable;

public class GunSmith extends VillagerBase {

    public GunSmith(World w) {
        super(w);

        GunSmith b = this;
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!MobHandler.getGunSmiths().contains(b.getUniqueIDString())) {
                    MobHandler.getGunSmiths().add(b.getUniqueIDString());
                    FileSaver.saveStaticMobData();
                }
            }
        }.runTaskLater(Withered.getPlugin(), 1);

        this.getBukkitEntity().setCustomName("GunSmith");
        ((Villager) this.getBukkitEntity()).setProfession(Villager.Profession.ARMORER);
    }

    public void death() {
        MobHandler.removeFromGunSmiths(this);
    }

    public void trade(Player player) {
        player.openInventory(GunSmithManager.createInventory(player));
    }

    public VillagerBase clazz() {
        return this;
    }

}
