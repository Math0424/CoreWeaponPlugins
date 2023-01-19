package me.Math0424.GunCreator.GUI.Page.Pages;

import me.Math0424.CoreWeapons.Guns.QualityEnum;
import me.Math0424.CoreWeapons.Util.ItemStackUtil;
import me.Math0424.CoreWeapons.Util.MyUtil;
import me.Math0424.GunCreator.GUI.MyGUI;
import me.Math0424.GunCreator.GUI.Page.Page;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class QualityTypeSelectionPage extends Page {

    public QualityTypeSelectionPage(MyGUI gui, String name, ItemStack background) {
        super(gui, name, background);
        setup();
    }

    private void setup() {
        for(int i = 0; i < QualityEnum.values().length; i++) {
            setPredicate(i, ItemStackUtil.createItemStack(Material.YELLOW_STAINED_GLASS,
                    ChatColor.RED + "Quality: " + MyUtil.capitalize(QualityEnum.values()[i].toString())),
            this::setValue);
        }
    }

    private void setValue(int slot, Player player) {
        QualityEnum type = QualityEnum.values()[slot];
        getGui().getGun().setQualityEnum(type);
        getGui().getPages()[0].open(player);
    }


}
