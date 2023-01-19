package me.Math0424.Withered.Entities.Villagers;

import me.Math0424.Withered.Entities.MobHandler;
import me.Math0424.Withered.Files.FileSaver;
import me.Math0424.Withered.Inventory.ShopkeeperInventoryInterpreter;
import me.Math0424.Withered.Withered;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.scheduler.BukkitRunnable;

public class Shopkeeper extends VillagerBase {

    private final String invType;

    public Shopkeeper(World w, String invType) {
        super(w);

        this.invType = invType;

        Shopkeeper s = this;
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!MobHandler.getShopkeepers().containsKey(s.getUniqueIDString())) {
                    MobHandler.getShopkeepers().put(s.getUniqueIDString(), invType);
                    FileSaver.saveStaticMobData();
                }
            }
        }.runTaskLater(Withered.getPlugin(), 1);

        this.getBukkitEntity().setCustomName(invType);
        ((Villager) this.getBukkitEntity()).setProfession(Villager.Profession.CLERIC);

    }

    public void death() {
        MobHandler.removeFromShopkeepers(this);
    }

    public void trade(Player player) {
        player.openInventory(ShopkeeperInventoryInterpreter.getSafeShopkeeperInventory(invType));
    }

    public VillagerBase clazz() {
        return this;
    }

}
