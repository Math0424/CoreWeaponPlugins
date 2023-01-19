package me.Math0424.GunCreator.GUI.Page;

import me.Math0424.GunCreator.GUI.MyGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class Page {

    private final String name;
    private final MyGUI gui;
    private final ItemStack background;

    private final HashMap<Integer, ActionInventoryEventPredicate> actionPredicates = new HashMap<>();
    private final HashMap<Integer, ActionSignEventPredicate> signPredicates = new HashMap<>();

    private final HashMap<Integer, ItemStack> items = new HashMap<>();

    private Inventory inventory;

    public Page(MyGUI gui, String name, ItemStack background) {
        this.name = name;
        this.gui = gui;
        this.background = background;
    }

    private int highest;
    public void setPredicate(int i, ItemStack item, ActionInventoryEventPredicate e) {
        if (i < 54) {
            if (i > highest)
                highest = i;
            actionPredicates.put(i, e);
            items.put(i, item);
            reSizeInventory();
        }
    }

    public void setSignPredicate(int i, ItemStack item, ActionSignEventPredicate e) {
        if (i < 54) {
            if (i > highest)
                highest = i;
            signPredicates.put(i, e);
            items.put(i, item);
        }
    }

    private void reSizeInventory() {
        inventory = Bukkit.createInventory(null, (highest+9) - (highest%9), name);
        generateInventory();
    }

    private void generateInventory() {
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, items.getOrDefault(i, background));
        }
    }

    public void inventoryClickEvent(InventoryClickEvent e) {
        if (e.getView().getTitle().equals(name) && e.getClickedInventory().getType() != InventoryType.PLAYER) {
            e.setCancelled(true);
            if (actionPredicates.containsKey(e.getSlot()) && actionPredicates.get(e.getSlot()) != null) {
                actionPredicates.get(e.getSlot()).run(e.getSlot(), (Player)e.getWhoClicked());
            } else if(signPredicates.containsKey(e.getSlot()) && signPredicates.get(e.getSlot()) != null) {
                new SignInput((Player) e.getWhoClicked(), signPredicates.get(e.getSlot()));
              }
        }
    }

    public void open(Player p) {
        p.openInventory(inventory);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public MyGUI getGui() {
        return gui;
    }

    protected int integerParse(Player p, String s) {
        if (s.isEmpty()) {
            return 0;
        }
        try {
            return Math.max(0, Integer.parseInt(s));
        } catch (Exception ignored) {
            p.sendMessage(ChatColor.RED + "'" + s + "' is not a positive integer!");
            return 0;
        }
    }

    protected double doubleParse(Player p, String s) {
        if (s.isEmpty()) {
            return 0;
        }
        try {
            return Math.max(0.001, Double.parseDouble(s));
        } catch (Exception ignored) {
            p.sendMessage(ChatColor.RED + "'" + s + "' is not a positive double!");
            return -1.0;
        }
    }

    protected boolean booleanParse(Player p, String s) {
        if (s.isEmpty()) {
            return true;
        }
        try {
            return Boolean.parseBoolean(s);
        } catch (Exception ignored) {
            p.sendMessage(ChatColor.RED + "'" + s + "' is not a 'true'/'false' value");
            return false;
        }
    }
}
