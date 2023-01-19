package me.Math0424.Withered.Gameplay.VillagerManagers;

import me.Math0424.Withered.Core.PlayerData;
import me.Math0424.Withered.Core.Steady;
import me.Math0424.Withered.Currency.CurrencyManager;
import me.Math0424.Withered.Files.Changeable.Config;
import me.Math0424.Withered.Files.Changeable.Lang;
import me.Math0424.CoreWeapons.Util.ItemStackUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;

public class BankerManager {

    private static final int increment = Config.CURRENCYSTARTINGVALUE.getIntVal() * 2;

    public static Inventory createInventory(Player p) {
        Inventory inv = Bukkit.createInventory(null, 9, "Banker");
        for (int i = 0; i < inv.getSize(); i++) {
            inv.setItem(i, ItemStackUtil.createItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE, " "));
        }
        PlayerData d = PlayerData.getPlayerData(p);

        inv.setItem(0, ItemStackUtil.createItemStack(Material.IRON_INGOT, Lang.BANKERINV.convert(p), Arrays.asList(String.valueOf(d.getBankCash()))));
        inv.setItem(3, ItemStackUtil.createItemStack(Material.IRON_INGOT, Lang.BANKERINV.convert(p, 1), Arrays.asList(Lang.BANKERINV.convert(p, 1) + " " + increment + " " + Config.CURRENCYNAME.getStrVal())));
        inv.setItem(5, ItemStackUtil.createItemStack(Material.IRON_INGOT, Lang.BANKERINV.convert(p, 2), Arrays.asList(Lang.BANKERINV.convert(p, 2) + " " + increment + " " + Config.CURRENCYNAME.getStrVal())));

        return inv;
    }

    public static void inventoryClickEvent(InventoryClickEvent e) {
        if (e.getClickedInventory() != null && e.getView().getTitle().equals("Banker")) {
            e.setCancelled(true);
            int slot = e.getSlot();
            Player p = (Player) e.getWhoClicked();
            PlayerData d = PlayerData.getPlayerData(p);
            if (slot == 5 && d.getBankCash() >= increment) {
                d.addToCash(increment);
                d.subtractFromBank(increment);
                updateMoneyInBank(p, e.getClickedInventory());
                CurrencyManager.updateCurrency(p);
                p.playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1, 1);
            } else if (slot == 3 && d.getCash() - increment >= 0) {
                p.sendMessage(Lang.BANKERDEPOSIT.convert(p));
                new Steady() {
                    public void moved(Player p) {
                        p.sendMessage(Lang.BANKERDEPOSIT.convert(p, 1));
                    }

                    public void ticked(int timeRemaining) {

                    }

                    public void complete(Player p) {
                        d.addToBankCash(increment);
                        d.subtractFromCash(increment);
                        p.sendMessage(Lang.BANKERDEPOSIT.convert(p, 2));
                        CurrencyManager.updateCurrency(p);
                        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                    }
                }.runTimer(p, (20 * 5));
                p.playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1, 1);
                p.closeInventory();
            }
        }
    }

    private static void updateMoneyInBank(Player p, Inventory inv) {
        PlayerData d = PlayerData.getPlayerData(p);
        inv.setItem(0, ItemStackUtil.createItemStack(Material.IRON_INGOT, Lang.BANKERINV.convert(p), Arrays.asList(String.valueOf(d.getBankCash()))));
    }


}
