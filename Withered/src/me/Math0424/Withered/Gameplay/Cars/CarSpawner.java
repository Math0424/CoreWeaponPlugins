package me.Math0424.Withered.Gameplay.Cars;

import me.Math0424.Withered.Entities.Cars.Car;
import me.Math0424.Withered.Entities.Cars.CarData;
import me.Math0424.Withered.Entities.MobHandler;
import me.Math0424.Withered.Files.Changeable.Config;
import me.Math0424.Withered.Util.WitheredUtil;
import me.Math0424.Withered.Withered;
import me.Math0424.CoreWeapons.Util.MyUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collections;

public class CarSpawner {

    static {
        new BukkitRunnable() {
            public void run() {
                if (CarSpawnSerializable.carSpawns.size() > 0) {
                    if (Config.DISABLECARSPAWNS.getBoolVal()) {
                        WitheredUtil.debug("Vehicles: car spawns disabled");
                        return;
                    }
                    if (MobHandler.getCars().size() > Config.MAXCARSPAWNS.getIntVal()) {
                        WitheredUtil.debug("Vehicles: Canceled spawn due to max car limit");
                        return;
                    }
                    CarSpawnSerializable spawn = CarSpawnSerializable.carSpawns.get(MyUtil.random(CarSpawnSerializable.carSpawns.size()));
                    for (Player p : spawn.getLocation().getWorld().getPlayers()) {
                        if (spawn.getLocation().distance(p.getLocation()) < 50) {
                            WitheredUtil.debug("Vehicles: Canceled spawn due to player being too close");
                            return;
                        }
                    }

                    double x = spawn.getLocation().getX() + MyUtil.randomPosNeg(spawn.getRadius());
                    double z = spawn.getLocation().getZ() + MyUtil.randomPosNeg(spawn.getRadius());
                    double y = spawn.getLocation().getWorld().getHighestBlockYAt((int) x, (int) z) + 1;
                    double y1 = spawn.getLocation().getY();
                    if (Math.abs(y - y1) >= 10) {
                        WitheredUtil.debug("Vehicles: Canceled spawn due being too high to spawn");
                        return;
                    }
                    Location carSpawn = new Location(spawn.getLocation().getWorld(), x - .5, y, z - .5);

                    int chance = MyUtil.random(100000) + 1;
                    Collections.shuffle(CarSerializable.carTypes);
                    for (CarSerializable car : CarSerializable.getCarTypes()) {
                        if (car.getLevel() <= spawn.getLevel() && chance <= 100000 * (car.getChance() / 100)) {
                            Car c = new Car(carSpawn.getWorld(), new CarData(car));
                            c.spawn(carSpawn);
                            WitheredUtil.debug("Vehicles: spawned car");
                            return;
                        }
                    }
                    WitheredUtil.debug("Vehicles: no car spawned");
                } else {
                    WitheredUtil.debug("Vehicles: no spawns to spawn cars at");
                }
            }
        }.runTaskTimer(Withered.getPlugin(), 1200, 1200);
    }


}
