package me.Math0424.WitheredControl.Guns.GunTypes;

import me.Math0424.CoreWeapons.Guns.Ammo.IAmmo;
import org.bukkit.Material;

public class FakeAmmo implements IAmmo {
    @Override
    public String name() {
        return "ammo";
    }

    @Override
    public Material material() {
        return Material.DIRT;
    }

    @Override
    public Integer id() {
        return 1;
    }

    @Override
    public Integer modelId() {
        return 1;
    }

    @Override
    public Integer maxStackSize() {
        return 64;
    }
}
