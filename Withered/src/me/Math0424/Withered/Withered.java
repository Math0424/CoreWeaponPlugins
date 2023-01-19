package me.Math0424.Withered;

import me.Math0424.Withered.Chests.LevelChest;
import me.Math0424.Withered.Commands.*;
import me.Math0424.Withered.Core.PlayerData;
import me.Math0424.Withered.Currency.CurrencyManager;
import me.Math0424.Withered.Entities.Cars.CarData;
import me.Math0424.Withered.Entities.Mech.MechData;
import me.Math0424.Withered.Entities.MobHandler;
import me.Math0424.Withered.Event.EventManager;
import me.Math0424.Withered.Files.Changeable.Config;
import me.Math0424.Withered.Files.FileLoader;
import me.Math0424.Withered.Files.FileSaver;
import me.Math0424.Withered.Gameplay.Cars.CarSerializable;
import me.Math0424.Withered.Gameplay.Cars.CarSpawnSerializable;
import me.Math0424.Withered.Gameplay.Cars.CarSpawner;
import me.Math0424.Withered.Gameplay.HeartBeat;
import me.Math0424.Withered.Inventory.InventoryManager;
import me.Math0424.Withered.Listeners.DeathListener;
import me.Math0424.Withered.Listeners.MainListener;
import me.Math0424.Withered.Listeners.MechListeners;
import me.Math0424.Withered.Listeners.PhysicsListener;
import me.Math0424.Withered.Loot.ItemSerializable;
import me.Math0424.Withered.Packets.PacketHandler;
import me.Math0424.Withered.Signs.SignData;
import me.Math0424.Withered.Structures.Structure;
import me.Math0424.Withered.Structures.StructureSerializable;
import me.Math0424.Withered.Teams.ScoreboardManager;
import me.Math0424.Withered.WitheredAPI.Serializable.*;
import me.Math0424.Withered.WitheredAPI.WitheredAPIListener;
import me.Math0424.CoreWeapons.CoreWeaponsAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Iterator;

public class Withered extends JavaPlugin {

    private static Withered plugin;

    public static Withered getPlugin() {
        return plugin;
    }

    public ArrayList<NamespacedKey> craftable = new ArrayList<>();

    public void onLoad() {
        plugin = this;

        if (!CoreWeaponsAPI.IsInitialized()) {
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        //serializable
        ConfigurationSerialization.registerClass(ItemSerializable.class);
        ConfigurationSerialization.registerClass(StructureSerializable.class);
        ConfigurationSerialization.registerClass(CarSerializable.class);
        ConfigurationSerialization.registerClass(CarSpawnSerializable.class);
        //WitheredAPI
        ConfigurationSerialization.registerClass(GunSerializable.class);
        ConfigurationSerialization.registerClass(GrenadeSerializable.class);
        ConfigurationSerialization.registerClass(ArmorSerializable.class);
        ConfigurationSerialization.registerClass(DeployableSerializable.class);
        ConfigurationSerialization.registerClass(AmmoSerializable.class);
        ConfigurationSerialization.registerClass(AttachmentSerializable.class);
        //DATA
        ConfigurationSerialization.registerClass(CarData.class);
        ConfigurationSerialization.registerClass(SignData.class);
        ConfigurationSerialization.registerClass(MechData.class);
        ConfigurationSerialization.registerClass(Structure.class);
        ConfigurationSerialization.registerClass(LevelChest.class);

        CoreWeaponsAPI.AddPluginToUpdateChecker("60958", this);

        FileLoader.LoadConfig();

        CoreWeaponsAPI.SetResourcePackPath(Config.RESOURCEPACK.getStrVal());
    }

    public void onEnable() {

        //Load config
        FileLoader.LoadFiles();

        //load world events
        new EventManager();
        new CarSpawner();

        System.out.println(ChatColor.YELLOW + "======================================================");
        System.out.println(ChatColor.YELLOW + "=     _       ___ __  __                       __    =");
        System.out.println(ChatColor.YELLOW + "=    | |     / (_) /_/ /_  ___  ________  ____/ /    =");
        System.out.println(ChatColor.YELLOW + "=    | | /| / / / __/ __ \\/ _ \\/ ___/ _ \\/ __  /     =");
        System.out.println(ChatColor.YELLOW + "=    | |/ |/ / / /_/ / / /  __/ /  /  __/ /_/ /      =");
        System.out.println(ChatColor.YELLOW + "=    |__/|__/_/\\__/_/ /_/\\___/_/   \\___/\\__,_/       =");
        System.out.println(ChatColor.YELLOW + "=                                                    =");
        System.out.println(ChatColor.YELLOW + "=  By: Math0424                                      =");
        System.out.println(ChatColor.YELLOW + "======================================================");

        Iterator<Recipe> recipies = Bukkit.recipeIterator();
        while (recipies.hasNext()) {
            Recipe r = recipies.next();
            if (r instanceof ShapelessRecipe) {
                craftable.add(((ShapelessRecipe) r).getKey());
            } else if (r instanceof ShapedRecipe) {
                craftable.add(((ShapedRecipe) r).getKey());
            }
        }

        for (Player p : Bukkit.getOnlinePlayers()) {
            CurrencyManager.updateCurrency(p);
            InventoryManager.updatePlayerInventory(p);
            ScoreboardManager.setMainTeam(p);
        }

        //listeners
        WitheredAPIListener apil = new WitheredAPIListener();
        MainListener manl = new MainListener();
        DeathListener deal = new DeathListener();
        PhysicsListener phyl = new PhysicsListener();
        MechListeners mecl = new MechListeners();

        this.getServer().getPluginManager().registerEvents(apil, this);
        this.getServer().getPluginManager().registerEvents(manl, this);
        this.getServer().getPluginManager().registerEvents(deal, this);
        this.getServer().getPluginManager().registerEvents(phyl, this);
        this.getServer().getPluginManager().registerEvents(mecl, this);

        new PacketHandler();
        new HeartBeat();

        //commands
        WitheredCommands wic = new WitheredCommands();
        this.getCommand("withered").setExecutor(wic);
        this.getCommand("withered").setTabCompleter(wic);

        WitheredSetupCommands wis = new WitheredSetupCommands();
        this.getCommand("witheredsetup").setExecutor(wis);
        this.getCommand("witheredsetup").setTabCompleter(wis);

        SquadCommands wsq = new SquadCommands();
        this.getCommand("squad").setExecutor(wsq);
        this.getCommand("squad").setTabCompleter(wsq);

        SuicideCommand suc = new SuicideCommand();
        this.getCommand("suicide").setExecutor(suc);

        LanguageCommand wlg = new LanguageCommand();
        this.getCommand("witheredLang").setExecutor(wlg);
        this.getCommand("witheredLang").setTabCompleter(wlg);

        for (World w : Bukkit.getWorlds()) {
            for (Entity e : w.getEntities()) {
                MobHandler.changeIfCustom(e);
            }
        }
    }

    public void onDisable() {
        if (!CoreWeaponsAPI.IsInitialized()) {
            return;
        }

        //SQLInterface.closeConnection();
        for (Structure s : Structure.structures) {
            s.deconstruct();
        }
        for (MechData d : MechData.getInMech().values()) {
            d.removePlayer();
        }
        for (Player p : Bukkit.getOnlinePlayers()) {
            PlayerData.getPlayerData(p).updatePlayerInvLocation();
            p.setResourcePack("");
        }
        FileSaver.saveResetLevel();
    }

}
