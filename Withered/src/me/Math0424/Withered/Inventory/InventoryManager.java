package me.Math0424.Withered.Inventory;

import me.Math0424.Withered.Chat.ChatManager;
import me.Math0424.Withered.Core.PlayerData;
import me.Math0424.Withered.Currency.CurrencyManager;
import me.Math0424.Withered.Entities.Mech.MechData;
import me.Math0424.Withered.Files.Changeable.Config;
import me.Math0424.Withered.Files.Changeable.Lang;
import me.Math0424.Withered.Teams.ScoreboardManager;
import me.Math0424.CoreWeapons.Util.InventoryUtil;
import me.Math0424.CoreWeapons.Util.ItemStackUtil;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class InventoryManager {

    private static final HashMap<Integer, ItemStack> unTouchables = new HashMap<>();
    private static final HashMap<Integer, InventoryClickedEventPredicate> actionables = new HashMap<>();

    private static ItemStack compass;
    private static ItemStack radio;
    private static ItemStack watch;

    static {
        setupUnTouchables();

        actionables.put(9, InventoryManager::clickedCompass);
        actionables.put(18, InventoryManager::clickedRadio);

        actionables.put(14, InventoryManager::clickedDrop14);
        actionables.put(15, InventoryManager::clickedDrop15);
        actionables.put(16, InventoryManager::clickedDrop16);

        actionables.put(22, InventoryManager::clickedDiamond);

        actionables.put(40, InventoryManager::clickedOffhand);
    }

    public static void setupUnTouchables() {
        compass = ItemStackUtil.createItemStack(Material.COMPASS, Lang.COMPASS.getValue(0), 1, 1);
        radio = ItemStackUtil.createItemStack(Material.ENDER_EYE, Lang.RADIO.getValue(0), 1, 100);
        watch = ItemStackUtil.createItemStack(Material.ENDER_EYE, Lang.WATCH.getValue(0), Collections.singletonList(Lang.WATCH.getValue(1)), 1, 1);
        for (int i = 9; i <= 35 - Config.EMPTYSLOTS.getIntVal(); i++) {
            unTouchables.put(i, ItemStackUtil.createItemStack(Material.GRAY_STAINED_GLASS_PANE, " "));
        }
        unTouchables.put(9, compass);
        unTouchables.put(18, radio);
        unTouchables.put(10, watch);
        unTouchables.put(27, ItemStackUtil.createItemStack(Material.MAP, Lang.INFO.getValue(0), Arrays.asList(Lang.INFO.getValue(1), Lang.INFO.getValue(2).replace("{primary}", Config.MAXPRIMARYGUNS.getIntVal().toString()), Lang.INFO.getValue(3).replace("{secondary}", Config.MAXSECONDARYGUNS.getIntVal().toString()))));

        unTouchables.put(14, ItemStackUtil.changeNameRemoveLore(CurrencyManager.getCurrency(), Lang.BASICWORDS.getValue(0) + " 50 " + Config.CURRENCYNAME.getStrVal()));
        unTouchables.put(15, ItemStackUtil.changeNameRemoveLore(CurrencyManager.getCurrency(), Lang.BASICWORDS.getValue(0) + " 100 " + Config.CURRENCYNAME.getStrVal()));
        unTouchables.put(16, ItemStackUtil.changeNameRemoveLore(CurrencyManager.getCurrency(), Lang.BASICWORDS.getValue(0) + " 200 " + Config.CURRENCYNAME.getStrVal()));

        unTouchables.put(17, CurrencyManager.getCurrency());

        unTouchables.put(39, ItemStackUtil.createItemStack(Material.AIR));
    }

    public static void updatePlayerInventory(Player p) {
        if (MechData.isInMech(p)) {
            return;
        }
        for (int i = 9; i <= 35; i++) {
            if ((p.getInventory().getItem(i) == null || p.getInventory().getItem(i).getType() == Material.AIR) && unTouchables.get(i) != null) {
                p.getInventory().setItem(i, unTouchables.get(i));
            }
        }
        CurrencyManager.updateCurrency(p);
        ItemManager.updateCompass(p, true);
        ScoreboardManager.updateHelmet(p);
    }

    private static void clickedCompass(InventoryClickEvent e) {
        e.setCancelled(true);
        if (e.getWhoClicked().getInventory().getItem(40) == null || e.getWhoClicked().getInventory().getItem(40).getType() == Material.AIR) {
            e.getWhoClicked().getInventory().setItem(40, e.getWhoClicked().getInventory().getItem(9).clone());
        }
        e.getWhoClicked().closeInventory();
    }

    private static void clickedOffhand(InventoryClickEvent e) {
        if (e.getCurrentItem() != null && ItemStackUtil.isSimilarNameType(e.getCurrentItem(), compass)) {
            e.setCancelled(true);
            e.setCurrentItem(ItemStackUtil.createItemStack(Material.AIR));
        }
    }

    private static void clickedRadio(InventoryClickEvent e) {
        e.setCancelled(true);
        ChatManager.rotateChannel((Player) e.getWhoClicked());
        e.getWhoClicked().sendMessage(Lang.RADIO.convert((Player) e.getWhoClicked(), 1).replace("{talktype}", PlayerData.getPlayerData((Player) e.getWhoClicked()).getTalkType().toString().toLowerCase()));
        ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.BLOCK_LEVER_CLICK, 1, 1);
        e.getWhoClicked().closeInventory();
    }

    private static void clickedDrop14(InventoryClickEvent e) {
        if (CurrencyManager.dropCurrency((Player) e.getWhoClicked(), 50)) {
            e.getWhoClicked().closeInventory();
        }
    }

    private static void clickedDrop15(InventoryClickEvent e) {
        if (CurrencyManager.dropCurrency((Player) e.getWhoClicked(), 100)) {
            e.getWhoClicked().closeInventory();
        }
    }

    private static void clickedDrop16(InventoryClickEvent e) {
        if (CurrencyManager.dropCurrency((Player) e.getWhoClicked(), 200)) {
            e.getWhoClicked().closeInventory();
        }
    }

    private static void clickedDiamond(InventoryClickEvent e) {
        //TODO: diamond info
    }

    public static void setInventory(Player p, Inventory inv) {
        InventoryUtil.setInventory(inv, p.getInventory());
    }

    public static ItemStack getCompass() {
        return compass.clone();
    }

    public static ItemStack getRadio() {
        return radio.clone();
    }

    public static ItemStack getWatch() {
        return watch.clone();
    }

    public static HashMap<Integer, ItemStack> getUnTouchables() {
        return unTouchables;
    }

    public static HashMap<Integer, InventoryClickedEventPredicate> getActionables() {
        return actionables;
    }
}

