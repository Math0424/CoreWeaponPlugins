package me.Math0424.Withered.Structures;

import me.Math0424.Withered.Files.FileSaver;
import me.Math0424.Withered.Util.WitheredUtil;
import me.Math0424.Withered.Withered;
import me.Math0424.CoreWeapons.Util.MyUtil;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class Structure implements ConfigurationSerializable {

    public static ArrayList<Structure> structures = new ArrayList<>();

    private final ArrayList<BlockState> removedBlocks = new ArrayList<>();
    private final ArrayList<BlockState> placedBlocks = new ArrayList<>();
    private boolean pasting = false;

    private Location chest;
    private String owner;
    private String name;
    private SchematicConverter converter;

    public Structure(Player p, Location chest, String name) {
        this.converter = new SchematicConverter(chest, name);
        this.owner = p.getName();
        this.chest = chest;
        this.name = name;
    }

    public void build() {
        if (!pasting) {
            pasting = true;
            structures.add(this);
            FileSaver.saveStructures();
            new BukkitRunnable() {
                int current = 0;

                @Override
                public void run() {
                    if (current >= converter.getBlocks().size()) {
                        cancel();
                        return;
                    }
                    if (!structures.contains(Structure.this) || !pasting || chest.getBlock().getType() != Material.ENDER_CHEST) {
                        cancel();
                        destroy();
                        return;
                    }
                    BlockState replace = converter.getBlocks().get(current);
                    BlockState old = replace.getLocation().getBlock().getState();
                    if (old.getType() != Material.AIR) {
                        removedBlocks.add(old);
                    }
                    if (replace.getType() == Material.STRUCTURE_VOID) {
                        replace.setType(Material.AIR);
                    }
                    replace.update(true, true);
                    placedBlocks.add(replace.getBlock().getState());

                    if (current % 10 == 1) {
                        replace.getLocation().getWorld().playEffect(replace.getLocation(), Effect.STEP_SOUND, replace.getType());
                    } else {
                        replace.getLocation().getWorld().playSound(replace.getLocation(), Sound.BLOCK_WOOD_PLACE, 1.5f, 1);
                    }

                    current++;
                }
            }.runTaskTimer(Withered.getPlugin(), 1, 1);
        }
    }

    public void instantBuild() {
        for (int i = 0; i < converter.getBlocks().size(); ++i) {
            BlockState replace = converter.getBlocks().get(i);
            BlockState old = replace.getLocation().getBlock().getState();
            if (old.getType() != Material.AIR) {
                if (replace.getType() != old.getType()) {
                    removedBlocks.add(old);
                }
                if (old.getType() != Material.AIR) {
                    replace.update(true);
                    placedBlocks.add(replace.getBlock().getState());
                }
            }
        }
    }

    public void deconstruct() {
        pasting = false;
        for (BlockState s : removedBlocks) {
            s.update(true);
        }
    }

    public void destroy() {
        pasting = false;
        structures.remove(this);
        int current = 0;
        for (BlockState s : placedBlocks) {
            current++;
            if (current % 10 == 0) {
                s.getWorld().playEffect(s.getLocation(), Effect.STEP_SOUND, s.getBlock().getType());
            }
            s.setType(Material.AIR);
            s.update(true);
        }
        deconstruct();
        chest.getBlock().breakNaturally();
        FileSaver.saveStructures();
    }

    public static Structure getStructure(Location loc) {
        for (Structure s : structures) {
            if (MyUtil.isSameBlockLocation(loc, s.getChest())) {
                return s;
            }
        }
        return null;
    }

    public static boolean isStructure(Location loc) {
        return getStructure(loc) != null;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Location getChest() {
        return chest;
    }

    public SchematicConverter getConverter() {
        return converter;
    }

    public String getName() {
        return name;
    }

    public Structure(Map<String, Object> map) {
        try {
            this.chest = (Location) map.get("chest");
            this.owner = (String) map.get("owner");
            this.name = (String) map.get("name");

            WitheredUtil.debug("Successfully loaded structure at " + chest.toString());
        } catch (Exception e) {
            e.printStackTrace();
            WitheredUtil.log(Level.SEVERE, "Failed to load structure at " + (map.get("chest")).toString());
        }
    }

    public static Structure deserialize(Map<String, Object> map) {
        Structure s = new Structure(map);
        if (s.chest.getBlock().getType() == Material.ENDER_CHEST) {
            s.converter = new SchematicConverter(s.chest, s.name);
            s.instantBuild();
            structures.add(s);
        }
        return s;
    }

    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("chest", chest);
        map.put("owner", owner);
        map.put("name", name);
        return map;
    }

}