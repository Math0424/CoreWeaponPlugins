package me.Math0424.Withered.Entities.Cars;

import me.Math0424.Withered.Entities.MobHandler;
import me.Math0424.Withered.Files.Changeable.BlockConfig;
import me.Math0424.Withered.Files.Changeable.Config;
import me.Math0424.Withered.Files.FileSaver;
import me.Math0424.Withered.Withered;
import me.Math0424.CoreWeapons.DamageHandler.DamageExplainer;
import me.Math0424.CoreWeapons.DamageHandler.DamageUtil;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;
import org.bukkit.material.MaterialData;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Car extends EntityMinecartRideable {

    private final Vector speed;
    private final CarData carData;

    public Car(World world, CarData carData) {
        super(EntityTypes.MINECART, ((CraftWorld) world).getHandle());
        this.carData = carData;
        speed = new Vector(0, 0, 0);

        Car c = this;
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!MobHandler.getCars().containsKey(c.getUniqueIDString())) {
                    carData.setUuid(c.getUniqueIDString());
                    MobHandler.getCars().put(c.getUniqueIDString(), carData);
                    FileSaver.saveMobData();
                }
            }
        }.runTaskLater(Withered.getPlugin(), 1);

    }

    //movement
    protected void i() {
        Location loc = this.getBukkitEntity().getLocation();
        //will freeze servers sometimes, why?
        try {
            this.move(EnumMoveType.SELF, this.getMot());
        } catch (Exception ignored) {
        }

        Entity entity = null;
        if (!this.getPassengers().isEmpty()) {
            entity = this.getPassengers().get(0);
        }

        if (this.onGround && entity instanceof EntityHuman) {
            //1.16.x .ba
            //1.16.2 .aT
            double forMot = ((EntityHuman) entity).aT;
            float finalYaw = Math.abs(entity.yaw <= 0 ? entity.yaw + 360 : entity.yaw);
            float x = MathHelper.sin(finalYaw * 0.017453292F) * carData.getAcceleration().floatValue();
            float z = MathHelper.cos(finalYaw * 0.017453292F) * carData.getAcceleration().floatValue();

            if (Config.CARSUSEROADBLOCK.getBoolVal() && !BlockConfig.roadBlocks.contains(loc.subtract(0, 1, 0).getBlock().getType())) {
                if (speed.length() > carData.getMaxSpeedOffRoad()) {
                    speed.multiply(.5);
                }
            }

            if (forMot > 0) {
                this.setMot(
                        speed.getX() - x,
                        0,
                        speed.getZ() + z
                );

                double slowSpeed = Math.min(1, speed.length() == 0 ? 0.95 : (carData.getMaxSpeed() / speed.length()));
                this.setMot(this.getMot().d(slowSpeed, 0.0D, slowSpeed));

            } else if (forMot < 0) {
                this.setMot(this.getMot().d(0.90D, 0.0D, 0.90D));
            } else {
                if (speed.length() > .05) {
                    speed.multiply(.90);
                    this.setMot(speed.getX(), 0, speed.getZ());
                } else {
                    this.setMot(0, 0, 0);
                }
            }

        } else if (this.onGround) {
            speed.multiply(.90);
            this.setMot(speed.getX(), 0, speed.getZ());
        }
    }

    public void tick() {
        double prevX = this.locX();
        double prevY = this.locY();
        double prevZ = this.locZ();
        float prevYaw = this.yaw;
        float prevPitch = this.pitch;

        this.checkBlockCollisions();
        this.pitch = 0.0F;

        //movement code
        this.i();
        speed.setX(this.getMot().getX());
        speed.setZ(this.getMot().getZ());
        //gravity
        this.setMot(this.getMot().add(0.0D, -.75D, 0.0D));


        //step height code
        Block inFront = this.getBukkitEntity().getLocation().toVector()
                .add(new Vector(this.getLookDirection().getX(), 0, this.getLookDirection().getZ()).rotateAroundY(67.5))
                .toLocation(this.getBukkitEntity().getWorld()).getBlock();
        if (!inFront.isPassable()) {
            this.G = 1.0F;
        } else {
            this.G = 0.0F;
        }

        //special stuff for damage
        if (this.getDamage() > carData.getHealth() * .25) {
            this.getBukkitEntity().getWorld().spawnParticle(org.bukkit.Particle.REDSTONE, this.getBukkitEntity().getLocation(), 1, new org.bukkit.Particle.DustOptions(Color.BLACK, 2f));
        }

        //change direction based on current location and previous
        double d4 = this.lastX - this.locX();
        double d5 = this.lastZ - this.locZ();
        if (d4 * d4 + d5 * d5 > 0.001D) {
            this.yaw = (float) (MathHelper.d(d5, d4) * 180.0D / 3.141592653589793D);
        }
        this.setYawPitch(this.yaw, this.pitch);

        //Bukkit vehicle move code
        org.bukkit.World bworld = this.world.getWorld();
        Location from = new Location(bworld, prevX, prevY, prevZ, prevYaw, prevPitch);
        Location to = new Location(bworld, this.locX(), this.locY(), this.locZ(), this.yaw, this.pitch);
        Vehicle vehicle = (Vehicle) this.getBukkitEntity();

        this.world.getServer().getPluginManager().callEvent(new org.bukkit.event.vehicle.VehicleUpdateEvent(vehicle));
        if (!from.equals(to)) {
            this.world.getServer().getPluginManager().callEvent(new org.bukkit.event.vehicle.VehicleMoveEvent(vehicle, from, to));
        }

        //collision code start
        for (Entity entity : this.world.getEntities(this, this.getBoundingBox().grow(0.20000000298023224D, 0.0D, 0.20000000298023224D))) {
            if (!this.w(entity) && entity.isCollidable()) {
                if (entity instanceof EntityMinecartAbstract) {
                    VehicleEntityCollisionEvent collisionEvent = new VehicleEntityCollisionEvent(vehicle, entity.getBukkitEntity());
                    this.world.getServer().getPluginManager().callEvent(collisionEvent);
                    if (collisionEvent.isCancelled()) {
                        continue;
                    }
                    entity.collide(this);
                } else if (speed.length() >= .4 && entity.getBukkitEntity() instanceof LivingEntity) {
                    //damage code
                    if (this.getBukkitEntity().getPassenger() != null) {
                        DamageUtil.setDamage(5.0, (LivingEntity) entity.getBukkitEntity(), this.getBukkitEntity().getPassenger(), null, DamageExplainer.RUNOVER);
                    } else {
                        DamageUtil.setDamage(5.0, (LivingEntity) entity.getBukkitEntity(), null, null, DamageExplainer.RUNOVER);
                    }
                }
            }
        }
        //collision code end

        //idfk what this does, car wont run w/out it
        //something about water and vehicles
        //this.aC();
        this.aJ();

    }

    public EnumInteractionResult a(EntityHuman human, EnumHand hand) {
        if (human.isSneaking() && carData.hasInventory()) {
            human.getBukkitEntity().openInventory(carData.getInventory());
            this.getBukkitEntity().getWorld().playSound(this.getBukkitEntity().getLocation(), Sound.BLOCK_CHEST_OPEN, 1, 1);
        } else if(!human.isSneaking()) {
            human.startRiding(this);
        }
        return EnumInteractionResult.PASS;
    }

    //damage
    public boolean damageEntity(DamageSource damagesource, float f) {
        if (!this.dead) {
            if (this.isInvulnerable(damagesource)) {
                return false;
            } else {
                Vehicle vehicle = (Vehicle) this.getBukkitEntity();
                org.bukkit.entity.Entity passenger = damagesource.getEntity() == null ? null : damagesource.getEntity().getBukkitEntity();
                VehicleDamageEvent event = new VehicleDamageEvent(vehicle, passenger, f);
                this.world.getServer().getPluginManager().callEvent(event);
                if (event.isCancelled()) {
                    return false;
                } else {
                    f = (float) event.getDamage();
                    this.d(-this.n());
                    this.c(10);
                    this.velocityChanged();
                    this.setDamage(this.getDamage() + f);

                    if (this.getDamage() > carData.getHealth()) {
                        VehicleDestroyEvent destroyEvent = new VehicleDestroyEvent(vehicle, passenger);
                        this.world.getServer().getPluginManager().callEvent(destroyEvent);
                        if (destroyEvent.isCancelled()) {
                            return false;
                        }

                        this.ejectPassengers();
                        this.a(damagesource);

                    }
                    return true;
                }
            }
        } else {
            return false;
        }
    }

    //death
    public void a(DamageSource damagesource) {
        this.die();
        this.getBukkitEntity().getWorld().createExplosion(this.getBukkitEntity().getLocation(), 4);
    }

    public void die() {
        MobHandler.removeFromCars(this);
        super.die();
    }

    //disable rails
    protected void b(BlockPosition blockposition, IBlockData iblockdata) {

    }

    //no
    protected void decelerate() {

    }

    public Car spawn(Location loc) {
        this.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        ((CraftWorld) loc.getWorld()).getHandle().addEntity(this);
        if (carData.getMaterial() != null) {
            ((Minecart) this.getBukkitEntity()).setDisplayBlock(new MaterialData(carData.getMaterial(), (byte) carData.getMaterialId()));
            ((Minecart) this.getBukkitEntity()).setDisplayBlockOffset(5);
        }
        return this;
    }


}
