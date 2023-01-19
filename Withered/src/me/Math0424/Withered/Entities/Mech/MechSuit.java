package me.Math0424.Withered.Entities.Mech;

import me.Math0424.Withered.Core.Steady;
import me.Math0424.Withered.Entities.MobHandler;
import me.Math0424.Withered.Event.Events.Global.EndgameDiamondEvent;
import me.Math0424.Withered.Files.Changeable.Config;
import me.Math0424.Withered.Files.Changeable.Lang;
import me.Math0424.Withered.Files.FileSaver;
import me.Math0424.Withered.Util.WitheredUtil;
import me.Math0424.Withered.Withered;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class MechSuit extends EntityIronGolem {

    private MechData mechData;

    public MechSuit(org.bukkit.World world) {
        super(EntityTypes.IRON_GOLEM, ((CraftWorld) world).getHandle());

        MechSuit m = this;
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!MobHandler.getMechs().containsKey(m.getUniqueIDString())) {
                    MechData data = new MechData(m);
                    MobHandler.getMechs().put(m.getUniqueIDString(), data);
                    mechData = data;
                    WitheredUtil.debug("Made new MechData for " + m.getUniqueIDString());
                    m.setHealth(mechData.getMechHealth());
                } else {
                    mechData = MobHandler.getMechs().get(m.getUniqueIDString());
                    WitheredUtil.debug("Loaded old MechData for " + m.getUniqueIDString());
                    m.setHealth(mechData.getMechHealth());
                }
                FileSaver.saveMobData();
            }
        }.runTaskLater(Withered.getPlugin(), 1);

        this.setCustomName(IChatBaseComponent.ChatSerializer.b("MechSuit"));

        this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.25D);
        this.getAttributeInstance(GenericAttributes.KNOCKBACK_RESISTANCE).setValue(1.0D);
        this.getAttributeInstance(GenericAttributes.MAX_HEALTH).setValue(Config.MECHSUITHEALTH.getIntVal());
        this.setHealth(Config.MECHSUITHEALTH.getIntVal());
    }

    //interaction
    boolean interacted = false;

    protected EnumInteractionResult b(EntityHuman entityhuman, EnumHand enumhand) {
        Player p = ((Player) (entityhuman.getBukkitEntity()));
        mechData.mechSuit = this;
        if (EndgameDiamondEvent.isDiamondHolder(p)) {
            p.sendMessage(Lang.MECHENTERING.convert(p, 2));
            return EnumInteractionResult.FAIL;
        }
        if (!interacted && !MechData.isInMech(p)) {
            interacted = true;
            p.sendMessage(Lang.MECHENTERING.convert(p, 0));
            new Steady() {
                public void moved(Player p) {
                    interacted = false;
                    p.sendMessage(Lang.MECHENTERING.convert(p, 1));
                }

                public void ticked(int timeRemaining) {

                }

                public void complete(Player p) {
                    interacted = false;
                    mechData.setPlayer(p);
                }
            }.runTimer(p, 20 * 5);
        }
        return EnumInteractionResult.SUCCESS;
    }

    public void die(DamageSource damagesource) {
        MobHandler.removeFromMechs(this);
        this.getBukkitEntity().getWorld().createExplosion(this.getBukkitEntity().getLocation().add(0, 1, 0), 5);
    }

    public void movementTick() {
        org.bukkit.Location headLoc = this.getBukkitEntity().getLocation().toVector().add(this.getBukkitEntity().getLocation().getDirection().multiply(-0.5)).toLocation(this.getBukkitEntity().getWorld());
        if (this.getHealth() < Config.MECHSUITHEALTH.getIntVal() * .25) {
            this.getBukkitEntity().getWorld().spawnParticle(org.bukkit.Particle.REDSTONE, headLoc.getX(), headLoc.getY() + 2, headLoc.getZ(), 1, new org.bukkit.Particle.DustOptions(Color.BLACK, 2f));
        }
        super.movementTick();
    }

    public EntityIronGolem.CrackLevel l() {
        return EntityIronGolem.CrackLevel.a(this.getHealth() / Config.MECHSUITHEALTH.getIntVal());
    }

    protected void initPathfinder() {
    }

    public MechSuit spawn(Location loc) {
        this.dead = false;
        this.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        ((CraftWorld) loc.getWorld()).getHandle().addEntity(this);
        return this;
    }

}
