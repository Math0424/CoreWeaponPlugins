package me.Math0424.GunCreator.GUI.Page.Pages;

import me.Math0424.CoreWeapons.Guns.Attachments.AttachmentModifier;
import me.Math0424.CoreWeapons.Util.ItemStackUtil;
import me.Math0424.CoreWeapons.Util.MyUtil;
import me.Math0424.GunCreator.GUI.MyGUI;
import me.Math0424.GunCreator.GUI.Page.Page;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class IncompatibleAttachmentSelectionPage extends Page {

    public IncompatibleAttachmentSelectionPage(MyGUI gui, String name, ItemStack background) {
        super(gui, name, background);
        setup();
    }

    private void setup() {
        setPredicate(0, ItemStackUtil.createItemStack(Material.ARROW, "Back"), this::back);

        for(int i = 0; i < AttachmentModifier.values().length; i++) {
            AttachmentModifier mod = AttachmentModifier.values()[i];
            if (getGui().getGun().getIncompatibleAttachments().contains(mod)) {
                setPredicate(i+1, ItemStackUtil.createItemStack(Material.RED_STAINED_GLASS, ChatColor.WHITE + MyUtil.capitalize(AttachmentModifier.values()[i].toString())), this::setValue);
            } else {
                setPredicate(i+1, ItemStackUtil.createItemStack(Material.GREEN_STAINED_GLASS, ChatColor.WHITE + MyUtil.capitalize(AttachmentModifier.values()[i].toString())), this::setValue);
            }
        }
    }

    private void back(int slot, Player player) {
        getGui().getPages()[0].open(player);
    }

    private void setValue(int slot, Player player) {
        getGui().getGun().getIncompatibleAttachments().add(AttachmentModifier.values()[slot-1]);
        setup();
        getGui().getPages()[6].open(player);
    }


}
