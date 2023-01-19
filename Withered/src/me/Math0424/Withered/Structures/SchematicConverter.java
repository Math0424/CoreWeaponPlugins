package me.Math0424.Withered.Structures;

import me.Math0424.Withered.Util.WitheredUtil;
import me.Math0424.Withered.Withered;
import me.Math0424.CoreWeapons.Util.MyUtil;
import net.minecraft.server.v1_16_R3.NBTCompressedStreamTools;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import org.bukkit.Axis;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.MultipleFacing;
import org.bukkit.block.data.Orientable;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;
import java.util.logging.Level;

public class SchematicConverter {

    private final ArrayList<BlockState> finalBlocks = new ArrayList<>();
    private boolean isFloating;
    private boolean isInGround;
    private boolean isReplacingInvalidBlocks;
    private boolean exists = true;

    public SchematicConverter(Location loc, String name) {
        try {

            File schematic = new File(Withered.getPlugin().getDataFolder(), "Schematics/" + name + ".schem");

            FileInputStream file = new FileInputStream(schematic);
            NBTTagCompound nbt = NBTCompressedStreamTools.a(file);

            short width = nbt.getShort("Width");
            short height = nbt.getShort("Height");
            short length = nbt.getShort("Length");
            byte[] blockDatas = nbt.getByteArray("BlockData");
            NBTTagCompound palette = nbt.getCompound("Palette");
            NBTTagCompound metadata = nbt.getCompound("Metadata");

            List<Integer> meta = new ArrayList<>();
            metadata.getKeys().forEach(rawState -> {
                Integer data = metadata.getInt(rawState);
                meta.add(data);
            });
            Map<Integer, BlockData> blocks = new HashMap<>();
            palette.getKeys().forEach(rawState -> {
                int id = palette.getInt(rawState);
                BlockData blockData = Bukkit.createBlockData(rawState);
                blocks.put(id, blockData);
            });
            file.close();

            Directional chestDirection = (Directional) loc.getBlock().getState().getBlockData();
            List<Integer> indexes = new ArrayList<>();
            List<Location> locations = new ArrayList<>();

            List<Material> replacable = Arrays.asList(
                    Material.TALL_GRASS,
                    Material.AIR,
                    Material.ROSE_BUSH,
                    Material.SUNFLOWER,
                    Material.DEAD_BUSH,
                    Material.CACTUS,
                    Material.GRASS,
                    Material.FERN,
                    Material.LARGE_FERN,
                    Material.DANDELION,
                    Material.ROSE_BUSH,
                    Material.DIRT,
                    Material.GRASS_BLOCK,
                    Material.SAND,
                    Material.SNOW);

            double air = 0;
            double solid = 0;

            for (int y = 0; y < height; ++y) {
                for (int x = 0; x < width; ++x) {
                    for (int z = length - 1; z >= 0; --z) {
                        int index = y * width * length + z * width + x;

                        Location location = null;
                        if (chestDirection.getFacing() == BlockFace.SOUTH) {
                            location = new Location(loc.getWorld(), x + meta.get(0) + loc.getX(), y + meta.get(2) + loc.getY(), z + meta.get(1) + loc.getZ());
                        } else if (chestDirection.getFacing() == BlockFace.NORTH) {
                            location = new Location(loc.getWorld(), -x - meta.get(0) + loc.getX(), y + meta.get(2) + loc.getY(), -z - meta.get(1) + loc.getZ());
                        } else if (chestDirection.getFacing() == BlockFace.WEST) {
                            location = new Location(loc.getWorld(), -z - meta.get(1) + loc.getX(), y + meta.get(2) + loc.getY(), x + meta.get(0) + loc.getZ());
                        } else if (chestDirection.getFacing() == BlockFace.EAST) {
                            location = new Location(loc.getWorld(), z + meta.get(1) + loc.getX(), y + meta.get(2) + loc.getY(), -x - meta.get(0) + loc.getZ());
                        }
                        Material material = location.getBlock().getType();

                        if (y == 0 && location.clone().subtract(0, 1, 0).getBlock().getType() == Material.AIR || location.clone().subtract(0, 1, 0).getBlock().getType() == Material.WATER) {
                            air++;
                        } else if (y == 0) {
                            solid++;
                        }

                        if (!MyUtil.isSameBlockLocation(location, loc)) {
                            indexes.add(index);
                            locations.add(location);

                            if (!replacable.contains(material)) {
                                isReplacingInvalidBlocks = true;
                            }
                        }
                    }
                }
            }

            if (solid == 0 || air / solid > .75) {
                isFloating = true;
            }

            solid = 0;

            for (int i = 0; i < locations.size(); ++i) {
                BlockData data = blocks.get((int) blockDatas[indexes.get(i)]).clone();

                if (Directional.class.isAssignableFrom(data.getClass())) {
                    Directional facing = (Directional) data;
                    if (chestDirection.getFacing() == BlockFace.NORTH) {
                        facing.setFacing(facing.getFacing().getOppositeFace());
                    } else if (chestDirection.getFacing() == BlockFace.WEST) {
                        if (facing.getFacing() == BlockFace.NORTH) {
                            facing.setFacing(BlockFace.EAST);
                        } else if (facing.getFacing() == BlockFace.SOUTH) {
                            facing.setFacing(BlockFace.WEST);
                        } else if (facing.getFacing() == BlockFace.EAST) {
                            facing.setFacing(BlockFace.SOUTH);
                        } else if (facing.getFacing() == BlockFace.WEST) {
                            facing.setFacing(BlockFace.NORTH);
                        }
                    } else if (chestDirection.getFacing() == BlockFace.EAST) {
                        if (facing.getFacing() == BlockFace.NORTH) {
                            facing.setFacing(BlockFace.WEST);
                        } else if (facing.getFacing() == BlockFace.SOUTH) {
                            facing.setFacing(BlockFace.EAST);
                        } else if (facing.getFacing() == BlockFace.EAST) {
                            facing.setFacing(BlockFace.NORTH);
                        } else if (facing.getFacing() == BlockFace.WEST) {
                            facing.setFacing(BlockFace.SOUTH);
                        }
                    }
                    data = facing;
                } else if (Orientable.class.isAssignableFrom(data.getClass())) {
                    Orientable facing = (Orientable) data;
                    if (chestDirection.getFacing() == BlockFace.EAST || chestDirection.getFacing() == BlockFace.WEST) {
                        if (facing.getAxis() == Axis.X) {
                            facing.setAxis(Axis.Z);
                        } else if (facing.getAxis() == Axis.Z) {
                            facing.setAxis(Axis.X);
                        }
                    }
                    data = facing;
                } else if (MultipleFacing.class.isAssignableFrom(data.getClass())) {
                    MultipleFacing facing = (MultipleFacing) data;
                    BlockFace[] faces = new BlockFace[facing.getFaces().size()];
                    facing.getFaces().toArray(faces);
                    if (chestDirection.getFacing() == BlockFace.NORTH) {
                        for (BlockFace face : faces) {
                            facing.setFace(face, false);
                            if (facing.getAllowedFaces().contains(face.getOppositeFace()))
                                facing.setFace(face.getOppositeFace(), true);
                        }
                    } else if (chestDirection.getFacing() == BlockFace.WEST) {
                        for (BlockFace face : faces) {
                            if (face == BlockFace.NORTH) {
                                facing.setFace(BlockFace.NORTH, false);
                                facing.setFace(BlockFace.EAST, true);
                            } else if (face == BlockFace.SOUTH) {
                                facing.setFace(BlockFace.SOUTH, false);
                                facing.setFace(BlockFace.WEST, true);
                            } else if (face == BlockFace.EAST) {
                                facing.setFace(BlockFace.EAST, false);
                                facing.setFace(BlockFace.SOUTH, true);
                            } else if (face == BlockFace.WEST) {
                                facing.setFace(BlockFace.WEST, false);
                                facing.setFace(BlockFace.NORTH, true);
                            }
                        }
                    } else if (chestDirection.getFacing() == BlockFace.EAST) {
                        for (BlockFace face : faces) {
                            if (face == BlockFace.NORTH) {
                                facing.setFace(BlockFace.NORTH, false);
                                facing.setFace(BlockFace.WEST, true);
                            } else if (face == BlockFace.SOUTH) {
                                facing.setFace(BlockFace.SOUTH, false);
                                facing.setFace(BlockFace.EAST, true);
                            } else if (face == BlockFace.EAST) {
                                facing.setFace(BlockFace.EAST, false);
                                facing.setFace(BlockFace.NORTH, true);
                            } else if (face == BlockFace.WEST) {
                                facing.setFace(BlockFace.WEST, false);
                                facing.setFace(BlockFace.SOUTH, true);
                            }
                        }
                    }
                    data = facing;
                }

                Block block = locations.get(i).getBlock();
                if (block.getType().isSolid()) {
                    solid++;
                }
                BlockData oldData = block.getBlockData();
                if (data.getMaterial() != Material.AIR) {
                    block.setBlockData(data, false);
                    finalBlocks.add(block.getState());
                    block.setBlockData(oldData, false);
                }
            }

            if (finalBlocks.size() != 0 && solid / finalBlocks.size() > .85) {
                isInGround = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            WitheredUtil.log(Level.SEVERE, "failed to load structure " + name);
            exists = false;
        }
    }

    public ArrayList<BlockState> getBlocks() {
        return finalBlocks;
    }

    public boolean isFloating() {
        return isFloating;
    }

    public boolean isInGround() {
        return isInGround;
    }

    public boolean isReplacingInvalidBlocks() {
        return isReplacingInvalidBlocks;
    }

    public boolean exists() {
        return exists;
    }
}
