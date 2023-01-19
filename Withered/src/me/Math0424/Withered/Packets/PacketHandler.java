package me.Math0424.Withered.Packets;

import io.netty.channel.Channel;
import me.Math0424.Withered.Entities.Mech.MechData;
import me.Math0424.Withered.Files.Changeable.Config;
import me.Math0424.Withered.Withered;
import me.Math0424.CoreWeapons.Packets.Reflection;
import me.Math0424.CoreWeapons.Packets.TinyProtocol;
import net.minecraft.server.v1_16_R3.*;
import net.minecraft.server.v1_16_R3.EntityIronGolem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;

public class PacketHandler {

    public static void changeToMech(Player p) {
        if (!MechData.getInMech().containsKey(p)) {
            return;
        }
        EntityIronGolem golem = getGolem(p);
        for (Player a : Bukkit.getOnlinePlayers()) {
            if (a != p) {
                ((CraftPlayer) a).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityDestroy(((CraftPlayer) p).getHandle().getId()));
                ((CraftPlayer) a).getHandle().playerConnection.sendPacket(new PacketPlayOutSpawnEntityLiving(golem));
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        ((CraftPlayer) a).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityMetadata(golem.getId(), golem.getDataWatcher(), true));
                    }
                }.runTaskLater(Withered.getPlugin(), 1);
            }
        }

    }

    public static void changeToPlayer(Player p) {
        for (Player a : Bukkit.getOnlinePlayers()) {
            if (a != p) {
                ((CraftPlayer) a).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityDestroy(((CraftPlayer) p).getHandle().getId()));
                ((CraftPlayer) a).getHandle().playerConnection.sendPacket(new PacketPlayOutNamedEntitySpawn(((CraftPlayer) p).getHandle()));
            }
        }
    }

    private static Field getField(Class clazz, String fieldName) throws NoSuchFieldException {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            Class superClass = clazz.getSuperclass();
            if (superClass == null) {
                throw e;
            } else {
                return getField(superClass, fieldName);
            }
        }
    }

    public static EntityIronGolem getGolem(Player p) {
        MechData mechData = MechData.getInMech().get(p);

        Location loc = p.getLocation();
        EntityIronGolem golem = new EntityIronGolem(EntityTypes.IRON_GOLEM, ((CraftPlayer) p).getHandle().getWorld());
        golem.setCustomNameVisible(true);
        golem.setCustomName(IChatBaseComponent.ChatSerializer.b(p.getName()));
        golem.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getPitch(), loc.getYaw());
        golem.setHealth((mechData.getMechHealth() / Config.MECHSUITHEALTH.getIntVal()) * 100);

        try {
            Field fieldUuid = getField(golem.getClass(), "id");
            fieldUuid.setAccessible(true);
            fieldUuid.set(golem, ((CraftPlayer) p).getHandle().getId());
        } catch (Exception e) {
        }
        return golem;
    }

    private static TinyProtocol protocol;
    private final Class<Object> nameSpawnClass = Reflection.getUntypedClass("{nms}.PacketPlayOutNamedEntitySpawn");
    private final Reflection.FieldAccessor<Integer> nameSpawnId = Reflection.getField(nameSpawnClass, int.class, 0);

    public PacketHandler() {
        if (protocol != null) {
            return;
        }
        protocol = new TinyProtocol(Withered.getPlugin()) {
            @Override
            public Object onPacketOutAsync(Player reciever, Channel channel, Object packet) {

                if (nameSpawnClass.isInstance(packet)) {
                    Integer id = nameSpawnId.get(packet);
                    for (Player p : MechData.getInMech().keySet()) {
                        if (p.getEntityId() == id) {
                            EntityIronGolem golem = getGolem(p);
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    sendPacket(reciever, new PacketPlayOutEntityMetadata(id, golem.getDataWatcher(), true));
                                }
                            }.runTaskLater(Withered.getPlugin(), 1);
                            return super.onPacketOutAsync(reciever, channel, new PacketPlayOutSpawnEntityLiving(golem));
                        }
                    }
                }
                return super.onPacketOutAsync(reciever, channel, packet);
            }
        };
    }


}
