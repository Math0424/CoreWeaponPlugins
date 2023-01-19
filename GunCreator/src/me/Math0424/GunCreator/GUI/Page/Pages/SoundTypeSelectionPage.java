package me.Math0424.GunCreator.GUI.Page.Pages;

import me.Math0424.CoreWeapons.Sound.SoundCache;
import me.Math0424.CoreWeapons.Util.ItemStackUtil;
import me.Math0424.CoreWeapons.Util.MyUtil;
import me.Math0424.GunCreator.GUI.MyGUI;
import me.Math0424.GunCreator.GUI.Page.Page;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SoundTypeSelectionPage extends Page {

    public SoundTypeSelectionPage(MyGUI gui, String name, ItemStack background) {
        super(gui, name, background);
        setup();
    }

    private void setup() {
        setSignPredicate(0, ItemStackUtil.createItemStack(Material.PAPER, "Custom sound"), this::setCustomValue);
        for(int i = 0; i < SoundCache.values().length; i++) {
            setPredicate(i+1, ItemStackUtil.createItemStack(Material.YELLOW_STAINED_GLASS, ChatColor.RED + "FireType: " + MyUtil.capitalize(SoundCache.values()[i].toString())),
            this::setValue);
        }
    }

    private void setCustomValue(Player player, String[] l) {
        for (String s : l) {
            getGui().getGun().setFireSoundID(s);
            getGui().getPages()[0].open(player);
            break;
        }
    }

    private void setValue(int slot, Player player) {
        SoundCache type = SoundCache.values()[slot-1];
        getGui().getGun().setFireSoundID(type.getSoundID());
        getGui().getPages()[0].open(player);
    }


}
