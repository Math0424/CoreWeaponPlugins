package me.Math0424.Withered.Listeners;

import me.Math0424.Withered.Entities.Mech.MechData;
import me.Math0424.Withered.Files.Changeable.Lang;
import me.Math0424.Withered.Packets.PacketHandler;
import me.Math0424.CoreWeapons.DamageHandler.DamageExplainer;
import me.Math0424.CoreWeapons.DamageHandler.DamageUtil;
import me.Math0424.CoreWeapons.Sound.SoundSystem;
import me.Math0424.CoreWeapons.Sound.SoundCache;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.*;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.spigotmc.event.entity.EntityMountEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MechListeners implements Listener {

    public static HashMap<Player, Integer> doubleJumpTime = new HashMap<Player, Integer>();
    public static ArrayList<Player> slamReady = new ArrayList<Player>();
    public static HashMap<Player, Location> stepDistance = new HashMap<Player, Location>();

    public static void mechSuitTick() {
        for (Player p : MechData.getInMech().keySet()) {
            p.setLevel(MechData.getInMech().get(p).getMechHealth().intValue());

            if (!doubleJumpTime.containsKey(p)) {
                doubleJumpTime.put(p, 0);
            } else if (doubleJumpTime.get(p) == 100) {
                p.setAllowFlight(true);
            } else {
                doubleJumpTime.put(p, doubleJumpTime.get(p) + 1);
                p.setExp(((float) doubleJumpTime.get(p)) / 100);
                p.setAllowFlight(false);
            }
        }
    }

    @EventHandler
    public void playerToggleFlightEvent(PlayerToggleFlightEvent e) {
        if (MechData.getInMech().containsKey(e.getPlayer()) && !slamReady.contains(e.getPlayer()) && doubleJumpTime.get(e.getPlayer()) == 100) {
            e.setCancelled(true);
            Player p = e.getPlayer();
            doubleJumpTime.put(p, 0);
            slamReady.add(p);
            p.setAllowFlight(false);
            p.setVelocity(p.getLocation().getDirection().multiply(1.6d).setY(1.5d));
            p.getWorld().spawnParticle(Particle.SMOKE_NORMAL, p.getLocation(), 20, 1, 0, 1, 0);
            for (int i = 0; i < 5; i++) {
                p.getWorld().playSound(p.getLocation(), Sound.ENTITY_IRON_GOLEM_ATTACK, 3, 1);
            }
        }
    }

    @EventHandler
    public void playerMoveEvent(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (MechData.getInMech().containsKey(p)) {

            if (slamReady.contains(p) && p.isOnGround()) {
                slamReady.remove(p);
                SoundSystem.playSound(p.getLocation(), SoundCache.MECH_SLAM, 1f, 60);
                for (org.bukkit.entity.Entity ent : p.getLocation().getWorld().getNearbyEntities(p.getLocation(), 5, 5, 5)) {
                    if (ent != p) {
                        Vector direction = ent.getLocation().toVector().subtract(p.getLocation().toVector()).normalize();
                        ent.setVelocity(direction.multiply(4).add(new Vector(0, 1.5, 0)));
                        if (ent instanceof LivingEntity) {
                            DamageUtil.setDamage(5.0, (LivingEntity) ent, p, null, DamageExplainer.SLAM);
                        }
                    }
                }

                for (Block b : generateSlamSphere(p.getLocation(), 5)) {
                    if (b.getType() == Material.AIR) {
                        b.getWorld().spawnParticle(Particle.BLOCK_CRACK, b.getLocation(), 10, .5, .5, .5, Material.DIRT.createBlockData());
                    } else {
                        b.getWorld().spawnParticle(Particle.BLOCK_CRACK, b.getLocation(), 10, .5, .5, .5, b.getBlockData());
                    }
                }

            }

            if (p.isSprinting() && doubleJumpTime.get(p) == 100) {
                doubleJumpTime.put(p, 0);
                p.getWorld().playSound(p.getLocation(), Sound.BLOCK_WOODEN_PRESSURE_PLATE_CLICK_ON, 1, 3);
                p.addPotionEffect((new PotionEffect(PotionEffectType.SPEED, 100, 10, false, false, false)));
            }

            if (p.isOnGround() && !stepDistance.containsKey(p)) {
                stepDistance.put(p, p.getLocation());
            } else if (p.isOnGround() && stepDistance.get(p).distance(p.getLocation()) >= 2) {
                p.getWorld().playSound(p.getLocation(), Sound.ENTITY_IRON_GOLEM_STEP, 4, 1);
                stepDistance.remove(p);
            }

        }
    }

    @EventHandler
    public void entityDamageEvent(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player && MechData.getInMech().containsKey(e.getEntity())) {
            Player p = (Player) e.getEntity();
            MechData data = MechData.getInMech().get(p);

            if (e.getCause() == EntityDamageEvent.DamageCause.FALL) {
                e.setCancelled(true);
                return;
            }

            data.setMechHealth((float) (data.getMechHealth() - e.getDamage()));

            //TODO: fix to be version independent
            ((CraftWorld) p.getWorld()).getHandle().broadcastEntityEffect(((CraftEntity) p).getHandle(), (byte) 33);
            EntityIronGolem golem = PacketHandler.getGolem(p);
            for (Player a : Bukkit.getOnlinePlayers()) {
                ((CraftPlayer) a).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityMetadata(golem.getId(), golem.getDataWatcher(), true));
            }

            p.getWorld().playSound(p.getLocation(), Sound.ENTITY_IRON_GOLEM_HURT, 1, 1);
            e.setCancelled(true);

            if (data.getMechHealth() <= 50 && data.getMechHealth().intValue() % 5 == 0) {
                p.sendMessage(Lang.MECHINFO.convert(p, 2));
            }

            if (data.getMechHealth() <= 0) {
                data.removePlayer(false);
                p.getWorld().createExplosion(p.getLocation(), 5);
            }

        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void playerInteractEvent(PlayerInteractEvent e) {
        if (MechData.getInMech().containsKey(e.getPlayer()) && doubleJumpTime.get(e.getPlayer()) > 1) {
            if (e.getItem() != null && e.getItem().getType() == Material.GLISTERING_MELON_SLICE) {
                Player p = e.getPlayer();
                MechData.getInMech().get(e.getPlayer()).removePlayer();
                p.sendMessage(Lang.MECHINFO.convert(e.getPlayer(), 1));
            }
            e.setCancelled(true);
        }
    }


    //generic events
    @EventHandler
    public void playerSwapHandItemsEvent(PlayerSwapHandItemsEvent e) {
        if (MechData.getInMech().containsKey(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void entityMountEvent(EntityMountEvent e) {
        if (MechData.getInMech().containsKey(e.getEntity())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void inventoryClickEvent(InventoryClickEvent e) {
        if (MechData.getInMech().containsKey(e.getWhoClicked())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void playerDropItemEvent(PlayerDropItemEvent e) {
        if (MechData.getInMech().containsKey(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void inventoryDragEvent(InventoryDragEvent e) {
        if (MechData.getInMech().containsKey(e.getWhoClicked())) {
            e.setCancelled(true);
        }
    }

    private List<Block> generateSlamSphere(Location block, int radius) {
        List<Block> blocks = new ArrayList<Block>();
        int bx = block.getBlockX();
        int by = block.getBlockY();
        int bz = block.getBlockZ();
        for (int x = bx - radius; x <= bx + radius; x++) {
            for (int y = by - radius; y <= by + radius; y++) {
                for (int z = bz - radius; z <= bz + radius; z++) {
                    double distance = ((bx - x) * (bx - x) + ((bz - z) * (bz - z)) + ((by - y) * (by - y)));
                    if (distance < radius * radius && ((!(distance < ((radius - 1) * (radius - 1))) && y >= by && y <= by) || y == by - 1)) {
                        blocks.add(block.getWorld().getBlockAt(x, y, z));
                    }
                }
            }
        }
        return blocks;
    }


}
