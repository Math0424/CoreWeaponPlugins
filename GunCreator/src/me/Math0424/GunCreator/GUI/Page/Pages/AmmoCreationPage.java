package me.Math0424.GunCreator.GUI.Page.Pages;

import me.Math0424.CoreWeapons.Core.Container;
import me.Math0424.CoreWeapons.Guns.Ammo.Ammo;
import me.Math0424.CoreWeapons.Util.ItemStackUtil;
import me.Math0424.GunCreator.GUI.MyGUI;
import me.Math0424.GunCreator.GUI.Page.Page;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;

public class AmmoCreationPage extends Page {

    private String name = "MyCustomAmmo";
    private String ammoID = "MyCustomAmmo";
    private int ammoCount = 10;


    private Material material = Material.IRON_NUGGET;
    private int modelId = 201;

    public AmmoCreationPage(MyGUI gui, String name, ItemStack background) {
        super(gui, name, background);
        setup();
    }

    private void setup() {
        ammoID = getGui().getGun().getAmmoID();

        setPredicate(0, ItemStackUtil.createItemStack(Material.IRON_DOOR, ChatColor.GREEN + "Back to gun GUI"),
                (s, p) -> getGui().getPages()[0].open(p));


        setSignPredicate(2, ItemStackUtil.createItemStack(Material.NAME_TAG, ChatColor.WHITE + "Name",
                Collections.singletonList(ChatColor.RED + "Enter a string")),
                this::clickedName);

        setSignPredicate(3, ItemStackUtil.createItemStack(Material.NAME_TAG, ChatColor.WHITE + "AmmoCount",
                Collections.singletonList(ChatColor.RED + "Enter a integer"), ammoCount),
                this::clickedAmmoCount);

        setPredicate(5, ItemStackUtil.createItemStack(material, ChatColor.WHITE + "Material",
                Arrays.asList(ChatColor.YELLOW + "Drop item onto this slot", ChatColor.YELLOW + "to set the material")),
                this::droppedItem);

        setSignPredicate(6, ItemStackUtil.createItemStack(material, ChatColor.WHITE + "SetModelID",
                Collections.singletonList(ChatColor.WHITE + "Current: " + modelId), modelId),
                this::clickedModelId);

        ItemStack item = ItemStackUtil.createItemStack(material, name, 1, modelId);
        Ammo a = new Ammo(ammoID, ammoCount, ammoCount);
        Container.applyToItem(item, a);
        setPredicate(8, item, this::giveClickedItem);
    }

    private void clickedName(Player player, String[] l) {
        if (!l[0].isEmpty()) {
            name = ChatColor.translateAlternateColorCodes('&', l[0]);
            ammoID = ChatColor.stripColor(name);
            getGui().getGun().setAmmoID(ammoID);
        }
        open(player);
    }

    private void clickedAmmoCount(Player player, String[] l) {
        ammoCount = Math.max(1, integerParse(player, l[0]));
        open(player);
    }

    private void droppedItem(int slot, Player player) {
        if (player.getItemOnCursor().getType() != Material.AIR) {
            material = player.getItemOnCursor().getType();
            player.setItemOnCursor(null);
            open(player);
        }
    }

    private void clickedModelId(Player player, String[] l) {
        modelId = integerParse(player, l[0]);
        open(player);
    }

    private void giveClickedItem(int slot, Player player) {
        player.setItemOnCursor(getInventory().getItem(slot));
    }



    public void setName(String name) {
        this.ammoID = name;
    }

    @Override
    public void open(Player p) {
        setup();
        super.open(p);
    }

}
