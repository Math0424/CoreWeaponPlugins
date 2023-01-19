package me.Math0424.GunCreator.GUI.Page.Pages;

import me.Math0424.CoreWeapons.Guns.Gun.GunType;
import me.Math0424.CoreWeapons.Util.ItemStackUtil;
import me.Math0424.CoreWeapons.Util.MyUtil;
import me.Math0424.GunCreator.GUI.MyGUI;
import me.Math0424.GunCreator.GUI.Page.Page;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GunTypeSelectionPage extends Page {

    public GunTypeSelectionPage(MyGUI gui, String name, ItemStack background) {
        super(gui, name, background);
        setup();
    }

    private void setup() {
        for(int i = 0; i < GunType.values().length; i++) {
            setPredicate(i, ItemStackUtil.createItemStack(Material.YELLOW_STAINED_GLASS,
                    ChatColor.RED + "GunType: " + MyUtil.capitalize(GunType.values()[i].toString())),
            this::setValue);
        }
    }

    private void setValue(int slot, Player player) {
        GunType type = GunType.values()[slot];
        getGui().getGun().setGunType(type);
        getGui().getPages()[0].open(player);
    }


}
