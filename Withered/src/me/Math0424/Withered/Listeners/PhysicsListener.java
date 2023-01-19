package me.Math0424.Withered.Listeners;

import me.Math0424.Withered.Files.Changeable.BlockConfig;
import me.Math0424.Withered.Files.Changeable.Config;
import me.Math0424.Withered.Withered;
import me.Math0424.CoreWeapons.DamageHandler.DamageExplainer;
import me.Math0424.CoreWeapons.DamageHandler.DamageUtil;
import me.Math0424.CoreWeapons.Util.MyUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class PhysicsListener implements Listener {

    @EventHandler
    public void blockExplodeEvent(EntityExplodeEvent e) {
        if (!Config.DESTRUCRIBLEWORLD.getBoolVal()) {
            e.blockList().clear();
            return;
        }

        ArrayList<Location> blocksList = new ArrayList<Location>();
        int y = e.getEntity().getLocation().getBlockY();
        for (Block b : e.blockList()) {
            if (e.blockList().size() > 50) {
                if (MyUtil.random(3) == 0) {
                    toFallingBlock(b, 2);
                }
            }
            blocksList.add(b.getLocation());
        }

        //generate the tower above
        new BukkitRunnable() {
            int current = y;
            int air = 15;
            int stone = 0;

            @Override
            public void run() {
                Location check = new Location(e.getEntity().getLocation().getWorld(), e.getEntity().getLocation().getBlockX(), current + 5, e.getEntity().getLocation().getBlockZ());
                Material mat = check.getBlock().getType();
                if (mat != Material.AIR) {
                    air = 15;
                }
                if (mat == Material.STONE
                        || mat == Material.ANDESITE
                        || mat == Material.DIORITE
                        || mat == Material.GRANITE
                        || mat == Material.DIRT) {
                    stone++;
                }

                ArrayList<Location> remove = new ArrayList<>();
                for (int i = 0; i < blocksList.size(); i++) {
                    Location l = blocksList.get(i);
                    Location loc = new Location(l.getWorld(), l.getX() - .5, current, l.getZ() - .5);
                    Block b = loc.getBlock();
                    if (BlockConfig.nonBreakable.contains(b.getType())) {
                        remove.add(l);
                    } else {
                        toFallingBlock(b, 10);
                    }
                }
                blocksList.removeAll(remove);
                current++;
                air--;
                if (current > 255 || air < 0 || stone > 5) {
                    cancel();
                }
            }
        }.runTaskTimer(Withered.getPlugin(), 0, 1);

    }

    @EventHandler
    public void entityChangeBlockEvent(EntityChangeBlockEvent e) {
        if (e.getEntity() instanceof FallingBlock) {
            e.getEntity().getLocation().getWorld().playSound(e.getEntity().getLocation(), Sound.BLOCK_WOOD_PLACE, 1, 1);
            for (Entity ent : e.getEntity().getNearbyEntities(1, 1, 1)) {
                if (ent instanceof LivingEntity) {
                    if (ent instanceof Player) {
                        Player p = (Player) ent;
                        DamageUtil.setDamage((double) 5, p, e.getEntity(), null, DamageExplainer.BLOCKCRUSH);
                    } else {
                        ((LivingEntity) ent).damage(10);
                    }
                }
            }
            if (MyUtil.random(7) != 1) {
                e.setCancelled(true);
            }
        }
    }

    private void toFallingBlock(Block b, int dampener) {
        if (b.isLiquid()) {
            b.setType(Material.AIR);
            return;
        }
        if (!BlockConfig.nonBreakable.contains(b.getType()) && !b.isEmpty()) {
            float x = (float) ((Math.random() * ((1.0 - -1.0) + 1)) + -1.0) / dampener;
            float y = (float) ((Math.random() * ((1.0 - -1.0) + 1)) + -1.0) / dampener;
            float z = (float) ((Math.random() * ((1.0 - -1.0) + 1)) + -1.0) / dampener;
            FallingBlock fallingBlock = b.getWorld().spawnFallingBlock(b.getLocation(), b.getType().createBlockData());
            fallingBlock.setDropItem(false);
            b.breakNaturally();
            fallingBlock.setVelocity(new Vector(x, y, z));
        }
    }

    //physics things
    private ArrayList<Block> isAreaUnstable(Block start) {
        ArrayList<Block> active = new ArrayList<>();
        ArrayList<Block> solid = new ArrayList<>();
        active.add(start);
        int maxChecks = 10000;
        while (active.size() > 0) {
            Block b = active.get(0);
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    for (int y = -1; y <= 1; y++) {
                        if (Math.abs(x) != Math.abs(z) && y != 0) {
                            Block testB = b.getLocation().add(x, y, z).getBlock();
                            if (!testB.isEmpty() && !testB.isLiquid()) {
                                if (x == 0 && z == 0 && isGround(testB)) {
                                    return null;
                                } else {
                                    active.add(testB);
                                }
                            }
                        }
                    }
                }
            }
            solid.add(b);
            active.remove(0);
            maxChecks--;
            if (maxChecks == 0) {
                return null;
            }
        }
        return solid;
    }

    private void collapseArea(ArrayList<Block> blocks) {
        int lowestY = 255;
        int highestY = 0;
        for (Block b : blocks) {
            if (b.getLocation().getBlockY() < lowestY) {
                lowestY = b.getLocation().getBlockY();
            }
            if (b.getLocation().getBlockY() > highestY) {
                highestY = b.getLocation().getBlockY();
            }
        }
        int finalLowestY = lowestY;
        int finalHighestY = highestY;
        new BukkitRunnable() {
            int current = finalLowestY;

            @Override
            public void run() {
                for (Block b : blocks) {
                    if (b.getLocation().getBlockY() == current) {
                        toFallingBlock(b, 10);
                    }
                }
                current++;
                if (current > finalHighestY) {
                    cancel();
                }
            }
        }.runTaskTimer(Withered.getPlugin(), 0, 1);
    }

    private boolean isGround(Block b) {
        int checkDist = 10;
        double stone = 0;
        Location start = b.getLocation();
        for (int i = checkDist; i >= 0; --i) {
            Material mat = start.clone().subtract(b.getX(), b.getY() + i, b.getZ()).getBlock().getType();
            if (mat == Material.STONE
                    || mat == Material.ANDESITE
                    || mat == Material.DIORITE
                    || mat == Material.GRANITE
                    || mat == Material.DIRT
                    || mat == Material.SNOW
                    || mat == Material.SAND
                    || mat == Material.SANDSTONE
                    || mat == Material.COBBLESTONE
                    || mat == Material.GRAVEL) {
                stone++;
            }
        }
        return stone / checkDist >= .8;
    }


}
