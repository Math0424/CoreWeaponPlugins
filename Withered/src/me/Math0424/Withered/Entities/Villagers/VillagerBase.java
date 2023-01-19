package me.Math0424.Withered.Entities.Villagers;

import com.google.common.collect.ImmutableSet;
import me.Math0424.Withered.Entities.Mech.MechData;
import me.Math0424.Withered.Files.FileSaver;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.entity.Player;

public abstract class VillagerBase extends EntityVillager {

    private Location home;

    protected VillagerBase(World world, VillagerType type) {
        super(EntityTypes.VILLAGER, ((CraftWorld) world).getHandle(), type);
        this.navigation.d(false);

        this.setCustomNameVisible(true);
        this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.0);

    }

    public VillagerBase(Location loc) {
        this(loc.getWorld(), VillagerType.PLAINS);
        //((CraftWorld)loc.getWorld()).getHandle().getBiome(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        spawn(loc);
    }

    public VillagerBase(World world) {
        this(world, VillagerType.PLAINS);
    }

    //player right click
    public EnumInteractionResult b(EntityHuman entityhuman, EnumHand enumhand) {
        if (!MechData.isInMech((Player) (entityhuman.getBukkitEntity()))) {
            trade((Player) entityhuman.getBukkitEntity());
            this.setTradingPlayer(entityhuman);
        }
        return EnumInteractionResult.SUCCESS;
    }

    public void die(DamageSource damagesource) {
        death();
        this.getBukkitEntity().remove();
        FileSaver.saveStaticMobData();
    }

    public void tick() {
        super.tick();
        if (ticksLived < 5 || home == null) {
            home = this.getBukkitEntity().getLocation();
        } else if (ticksLived > 6 && ticksLived % 20 == 0) {
            if (this.getBukkitEntity().getLocation().distance(home) > 3) {
                this.getBukkitEntity().teleport(home);
            }
        }
    }

    public void a(BehaviorController<EntityVillager> behaviorController) {
        behaviorController.a(Activity.CORE, Behaviors.a(this.getVillagerData().getProfession(), 1));
        behaviorController.a(ImmutableSet.of(Activity.CORE));
        behaviorController.a(Activity.IDLE, Behaviors.e(this.getVillagerData().getProfession(), 1));
        behaviorController.a(Activity.IDLE);
    }

    public boolean damageEntity(DamageSource damagesource, float f) {
        if (damagesource.getEntity() instanceof EntityHuman) {
            EntityHuman p = (EntityHuman) damagesource.getEntity();
            if (p.isCreativeAndOp()) {
                super.damageEntity(damagesource, f);
            }
            return false;
        }
        return false;
    }

    //generic code remover
    public void inactiveTick() {

    }

    protected void initPathfinder() {

    }

    public void onLightningStrike(EntityLightning entitylightning) {

    }

    //item pickup disabled
    protected void b(EntityItem entityitem) {

    }

    //no sleeping :(
    public void entitySleep(BlockPosition blockposition) {

    }

    public VillagerBase spawn(Location loc) {
        this.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        ((CraftWorld) loc.getWorld()).getHandle().addEntity(clazz());
        return this;
    }


    public abstract VillagerBase clazz();

    public abstract void death();

    public abstract void trade(Player player);

}
