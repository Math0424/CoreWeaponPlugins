package me.Math0424.Withered.Chests;

import me.Math0424.Withered.Files.Changeable.Config;
import me.Math0424.Withered.Loot.LootItem;
import me.Math0424.Withered.Util.WitheredUtil;
import me.Math0424.Withered.WitheredAPI.Serializable.AmmoSerializable;
import me.Math0424.CoreWeapons.Core.Container;
import me.Math0424.CoreWeapons.Guns.Gun.Gun;
import me.Math0424.CoreWeapons.Guns.QualityEnum;
import me.Math0424.CoreWeapons.Util.MyUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.data.Directional;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class LevelChest implements ConfigurationSerializable {

    public static ArrayList<LevelChest> chests = new ArrayList<>();

    private Integer level;
    private Location loc;

    private Boolean forceNonPop = false;
    private String customName;

    public LevelChest(Location loc, Integer level) {
        this.level = level;
        this.loc = loc;
    }

    public void register() {
        chests.add(this);
    }

    public void populate() {
        populate(true, Config.CHESTNEWCHANCE.getIntVal(), Config.CHESTFAIRCHANCE.getIntVal(), Config.CHESTUSEDCHANCE.getIntVal(), Config.CHESTWORNCHANCE.getIntVal());
    }

    public void populate(boolean clear) {
        populate(clear, Config.CHESTNEWCHANCE.getIntVal(), Config.CHESTFAIRCHANCE.getIntVal(), Config.CHESTUSEDCHANCE.getIntVal(), Config.CHESTWORNCHANCE.getIntVal());
    }

    public void populate(boolean clear, int newChance, int fairChance, int usedChance, int wornChance) {
        Block b = loc.getBlock();
        if (loc.getBlock().getLocation().add(0, -1, 0).getBlock().getType() == Material.AIR) {
            return;
        }
        b.setType(Material.CHEST);
        Directional orientable = (Directional) b.getBlockData();
        switch (MyUtil.random(3)) {
            case 0:
                orientable.setFacing(BlockFace.NORTH);
                break;
            case 1:
                orientable.setFacing(BlockFace.SOUTH);
                break;
            case 2:
                orientable.setFacing(BlockFace.WEST);
                break;
            case 3:
                orientable.setFacing(BlockFace.EAST);
                break;
        }
        b.setBlockData(orientable);
        Chest c = (Chest) b.getState();
        addItems(c, clear, newChance, fairChance, usedChance, wornChance);
    }

    private void addItems(Chest c, boolean clear, int newChance, int fairChance, int usedChance, int wornChance) {
        if (customName != null) {
            c.setCustomName(customName);
        } else {
            c.setCustomName("Chest level " + level);
        }
        if (clear) {
            c.getBlockInventory().clear();
        }
        Inventory inv = c.getBlockInventory();

        int timeAttempted = 0;

        List<ItemStack> chestLoot = new ArrayList<ItemStack>();

        int minLootAmount = MyUtil.random(5) + MyUtil.random((level / 5) + 1) + 1;

        while (timeAttempted < 100 && chestLoot.size() < minLootAmount) {
            timeAttempted++;
            LootItem loot = LootItem.getLootItems().get(MyUtil.random(LootItem.getLootItems().size() - 1));
            if (loot.getLevel() <= level) {
                int chance = MyUtil.random(100000) + 1;
                if (chance <= (100000 * loot.getChance())) {
                    Gun gun = Container.getContainerItem(Gun.class, loot.getItem());
                    if (gun != null) {
                        AmmoSerializable ammo = AmmoSerializable.getByName(gun.getAmmoId());

                        if (ammo != null) {
                            for (int i = 0; i <= MyUtil.random(3); i++) {
                                ItemStack ammoStack = ammo.baseClass.getItemStack();
                                ammoStack.setAmount(ammoStack.getMaxStackSize());
                                chestLoot.add(ammoStack);
                            }
                        }

                        int qualityChance = MyUtil.random(100);
                        QualityEnum quality;
                        if (qualityChance < newChance) {
                            quality = QualityEnum.NEW;
                        } else if (qualityChance < fairChance) {
                            quality = QualityEnum.FAIR;
                        } else if (qualityChance < usedChance) {
                            quality = QualityEnum.USED;
                        } else if (qualityChance < wornChance) {
                            quality = QualityEnum.WORN;
                        } else {
                            quality = QualityEnum.POOR;
                        }

                        chestLoot.add(Gun.getGunItemStack(gun, quality));

                    } else {
                        ItemStack item = loot.getItem();
                        if (loot.getMaxAmount() > 1) {
                            item.setAmount(Math.max(1, MyUtil.random(loot.getMaxAmount())));
                        }
                        chestLoot.add(item);
                    }
                }
            }
        }

        for (int x = 0; x < chestLoot.size(); ++x) {
            inv.setItem(MyUtil.random(c.getBlockInventory().getSize() - 1), chestLoot.get(x));
        }

    }

    public Integer getLevel() {
        return level;
    }

    public Location getLocation() {
        return loc.clone();
    }

    public Boolean getForceNonPop() {
        return forceNonPop;
    }

    public void setForceNonPop(Boolean forceNonPop) {
        this.forceNonPop = forceNonPop;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    public static LevelChest getLevelChest(Location loc) {
        for (LevelChest c : chests) {
            if (loc.distance(c.getLocation()) == 0) {
                return c;
            }
        }
        return null;
    }

    public static boolean removeChest(Location loc) {
        LevelChest l = getLevelChest(loc);
        if (l != null) {
            chests.remove(l);
            return true;
        }
        return false;
    }

    public LevelChest(Map<String, Object> map) {
        try {
            this.level = (Integer) map.get("level");
            this.loc = (Location) map.get("loc");

            WitheredUtil.debug("Loaded levelChest " + this.getLocation().toString());
        } catch (Exception e) {
            e.printStackTrace();
            WitheredUtil.log(Level.SEVERE, "Failed to load a levelChest!");
        }
    }

    public static LevelChest deserialize(Map<String, Object> map) {
        LevelChest c = new LevelChest(map);
        chests.add(c);
        return c;
    }

    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("loc", this.loc);
        map.put("level", this.level);
        return map;
    }

}
