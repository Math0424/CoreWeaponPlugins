package me.Math0424.CoreHooks;

import me.Math0424.CoreHooks.Bukkit.BukkitHook;
import me.Math0424.CoreHooks.ChestShop.ChestShopHook;
import me.Math0424.CoreHooks.GriefPrevention.GriefPreventionHook;
import me.Math0424.CoreHooks.Lands.LandsHook;
import me.Math0424.CoreHooks.Towny.TownyHook;
import me.Math0424.CoreHooks.WorldGuard.WorldGuardHook;
import me.Math0424.CoreWeapons.CoreWeaponsAPI;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main plugin;

    public static Main getPlugin() {
        return plugin;
    }

    boolean hasWorldEdit = false;

    @Override
    public void onLoad() {
        plugin = this;

        CoreWeaponsAPI.AddPluginToUpdateChecker("71523", this);

        if (Bukkit.getServer().getPluginManager().getPlugin("CoreWeapons") != null) {

            if (Bukkit.getServer().getPluginManager().getPlugin("WorldGuard") != null && Bukkit.getServer().getPluginManager().getPlugin("WorldEdit") != null) {
                hasWorldEdit = true;
                WorldGuardHook.loadWorldGuardHooks();
            }

        }
    }

    @EventHandler
    public void onEnable() {
        if (Bukkit.getServer().getPluginManager().getPlugin("CoreWeapons") != null) {

            if (!CoreWeaponsAPI.IsInitialized()) {
                Bukkit.getPluginManager().disablePlugin(this);
                return;
            }
            BukkitHook buk = new BukkitHook();
            this.getServer().getPluginManager().registerEvents(buk, this);

            if (hasWorldEdit) {
                WorldGuardHook worh = new WorldGuardHook();
                WorldGuardHook.loadContainer();
                this.getServer().getPluginManager().registerEvents(worh, this);
            }

            if (Bukkit.getPluginManager().getPlugin("GriefPrevention") != null) {
                GriefPreventionHook hook = new GriefPreventionHook();
                this.getServer().getPluginManager().registerEvents(hook, this);
            }

            if (Bukkit.getPluginManager().getPlugin("Towny") != null) {
                TownyHook hook = new TownyHook();
                this.getServer().getPluginManager().registerEvents(hook, this);
            }

            if (Bukkit.getPluginManager().getPlugin("ChestShop") != null) {
                ChestShopHook hook = new ChestShopHook();
                this.getServer().getPluginManager().registerEvents(hook, this);
            }

            if (Bukkit.getPluginManager().getPlugin("Lands") != null) {
                LandsHook hook = new LandsHook();
                LandsHook.init();
                this.getServer().getPluginManager().registerEvents(hook, this);
            }

            /*if (Bukkit.getPluginManager().getPlugin("VehiclesPlusPro") != null) {
                VehiclesPlusProHooks hook = new VehiclesPlusProHooks();
                this.getServer().getPluginManager().registerEvents(hook, this);
            }*/

            /*if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {

                WitheredAPI_PAPIHook wapi = new WitheredAPI_PAPIHook();
                wapi.register();

                if (Bukkit.getPluginManager().getPlugin("Withered") != null) {
                    WitheredPAPIHook with = new WitheredPAPIHook();
                    with.register();
                }
            }*/

        }
    }

}
