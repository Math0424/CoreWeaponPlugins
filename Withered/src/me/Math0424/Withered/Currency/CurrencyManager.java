package me.Math0424.Withered.Currency;

import me.Math0424.Withered.Core.PlayerData;
import me.Math0424.Withered.Files.Changeable.Config;
import me.Math0424.Withered.Files.Changeable.Lang;
import me.Math0424.Withered.Util.WitheredUtil;
import me.Math0424.CoreWeapons.Util.ItemStackUtil;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class CurrencyManager {

    private static final ItemStack currency;

    static {
        currency = ItemStackUtil.createItemStack(Material.IRON_INGOT, Config.CURRENCYNAME.getStrVal(), Arrays.asList(Config.CURRENCYSTARTINGVALUE.getIntVal().toString()));
    }

    public static void updateCurrency(Player p) {
        PlayerData data = PlayerData.getPlayerData(p);
        ItemStack item = ItemStackUtil.setLore(getCurrency(), Arrays.asList(String.valueOf(data.getCash())));
        p.getInventory().setItem(17, item);
    }

    public static boolean dropCurrency(Player p, Integer amount) {
        PlayerData data = PlayerData.getPlayerData(p);
        if (data.getCash() >= amount) {
            data.setCash(data.getCash() - amount);
            Item i = p.getWorld().dropItem(p.getLocation().add(0, 1, 0), ItemStackUtil.setLore(getCurrency(), Arrays.asList(amount.toString())));
            i.setVelocity(p.getEyeLocation().getDirection());
            i.setPickupDelay(20);
            updateCurrency(p);

            p.playSound(p.getLocation(), Sound.ENTITY_ITEM_PICKUP, .5f, 1);
            p.sendMessage(Lang.CURRENCYBASICS.convert(p, 1).replace("{amount}", amount.toString()).replace("{currency}", Config.CURRENCYNAME.getStrVal()));

            return true;
        }
        return false;
    }

    public static boolean pickupCurrency(Player p, Item item) {
        if (ItemStackUtil.isSimilarNameType(item.getItemStack(), getCurrency())) {
            PlayerData data = PlayerData.getPlayerData(p);
            String s = item.getItemStack().getItemMeta().getLore().get(0);
            Integer amount = Integer.parseInt(s) * item.getItemStack().getAmount();
            data.addToCash(amount);
            item.setPickupDelay(20);
            item.remove();
            updateCurrency(p);

            WitheredUtil.debug(p.getName() + " picked up " + amount.toString() + " " + Config.CURRENCYNAME.getStrVal());

            p.playSound(p.getLocation(), Sound.ENTITY_ITEM_PICKUP, .5f, 1);
            p.sendMessage(Lang.CURRENCYBASICS.convert(p, 0).replace("{amount}", amount.toString()).replace("{currency}", Config.CURRENCYNAME.getStrVal()));

            return true;
        }
        return false;
    }

    public static ItemStack getCurrency() {
        return currency.clone();
    }
}
