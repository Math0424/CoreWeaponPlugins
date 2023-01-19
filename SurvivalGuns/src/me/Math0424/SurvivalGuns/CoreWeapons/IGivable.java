package me.Math0424.SurvivalGuns.CoreWeapons;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public abstract class IGivable {

    public static List<IGivable> givables = new ArrayList<>();

    public abstract String getName();
    public abstract ItemStack getItem();

    public IGivable() {
        givables.add(this);
    }

}
