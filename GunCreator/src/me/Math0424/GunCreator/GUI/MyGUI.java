package me.Math0424.GunCreator.GUI;

import me.Math0424.CoreWeapons.Core.Container;
import me.Math0424.CoreWeapons.Guns.Gun.Gun;
import me.Math0424.CoreWeapons.Util.ItemStackUtil;
import me.Math0424.GunCreator.GUI.Page.Page;
import me.Math0424.GunCreator.GUI.Page.Pages.*;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class MyGUI {

    private static final Map<Player, MyGUI> instances = new HashMap<>();

    private final Player player;
    private final ProtoGun gun;

    private final Page[] pages;

    private MyGUI(Player p) {
        this.player = p;
        pages = new Page[10];
        gun = new ProtoGun();
        setupPages();
    }

    private void openGUI() {
        player.playSound(player.getLocation(), Sound.ENTITY_HORSE_ARMOR, 1, 1);
        pages[0].open(player);
    }

    private void openGUI(Gun g) {
        player.playSound(player.getLocation(), Sound.ENTITY_HORSE_ARMOR, 1, 1);
        gun.setGun(g);
        pages[0].open(player);
    }

    private void openAmmo() {
        player.playSound(player.getLocation(), Sound.ENTITY_HORSE_ARMOR, 1, 1);
        pages[4].open(player);
    }

    private void setupPages() {
        ItemStack background = ItemStackUtil.createItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE, " ");
        pages[0] = new MainPage(this,"GunCreationMenu", background);
        pages[1] = new BulletTypeSelectionPage(this, "BulletTypeSelection", background);
        pages[2] = new GunTypeSelectionPage(this, "FireTypeSelection", background);
        pages[3] = new SoundTypeSelectionPage(this, "SoundTypeSelection", background);
        pages[4] = new AmmoCreationPage(this, "AmmoCreationPage", background);
        pages[5] = new QualityTypeSelectionPage(this, "QualityTypeSelection", background);
        pages[6] = new IncompatibleAttachmentSelectionPage(this, "IncompatibleAttachmentSelection", background);
    }

    public ProtoGun getGun() {
        return gun;
    }

    public Page[] getPages() {
        return pages;
    }

    //static classes below
    public static void inventoryClickedEvent(InventoryClickEvent e) {
        MyGUI gui = getPlayerInstance((Player)e.getWhoClicked());
        if (gui != null && e.getClickedInventory() != null) {
            for (Page page : gui.pages) {
                if (page != null) {
                    page.inventoryClickEvent(e);
                }
            }
        }
    }

    public static MyGUI getPlayerInstance(Player p) {
        return instances.get(p);
    }

    public static void resetGUI(Player p) {
        instances.remove(p);
    }

    public static void openGUI(Player p) {
        if (instances.containsKey(p)) {
            instances.get(p).openGUI();
        } else {
            instances.put(p, new MyGUI(p));
            openGUI(p);
        }
    }

    public static void openGUI(Player p, Container<Gun> g) {
        if (instances.containsKey(p)) {
            instances.get(p).openGUI(g.getObject());
        }else {
            var GUI = new MyGUI(p);
            instances.put(p, GUI);

            MainPage main = (MainPage)GUI.pages[0];
            if (g.getItemStack().getItemMeta().hasCustomModelData())
                main.modelID = g.getItemStack().getItemMeta().getCustomModelData();
            main.material = g.getItemStack().getType();

            openGUI(p, g);
        }
    }

    public static void openAmmoGUI(Player p) {
        if (instances.containsKey(p)) {
            instances.get(p).openAmmo();
        } else {
            instances.put(p, new MyGUI(p));
            openAmmoGUI(p);
        }
    }

}
