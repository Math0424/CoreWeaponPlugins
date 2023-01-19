package me.Math0424.CoreHooks.ChestShop;

import com.Acrobot.ChestShop.Events.ItemParseEvent;
import com.Acrobot.ChestShop.Events.ItemStringQueryEvent;
import me.Math0424.CoreWeapons.Core.Container;
import me.Math0424.CoreWeapons.Guns.Gun.Gun;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class ChestShopHook implements Listener {

    private ItemStack current;

    @EventHandler
    public void ItemParseEvent(ItemParseEvent e) {
        if (current != null) {
            e.setItem(current.clone());
            current = null;
            return;
        }

        Container<Gun> g = Container.getContainerItem(Gun.class, e.getItem());
        if (g != null) {
            e.setItem(e.getItem());
        }

    }

    @EventHandler
    public void ItemStringQueryEvent(ItemStringQueryEvent e) {
        Container<Gun> g = Container.getContainerItem(Gun.class, e.getItem());
        if (g != null) {
            current = e.getItem();
            e.setItemString(ChatColor.stripColor(g.getObject().getName()));
        }
    }

}
