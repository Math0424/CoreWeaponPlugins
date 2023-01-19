package me.Math0424.WitheredGunGame;

import me.Math0424.CoreWeapons.CoreWeaponsAPI;
import me.Math0424.WitheredGunGame.Arenas.Arena;
import me.Math0424.WitheredGunGame.Arenas.ArenaFFA;
import me.Math0424.WitheredGunGame.Arenas.ArenaPOINT;
import me.Math0424.WitheredGunGame.Arenas.ArenaTEAM;
import me.Math0424.WitheredGunGame.Commands.GunGameCommands;
import me.Math0424.WitheredGunGame.Commands.LeaveCommand;
import me.Math0424.WitheredGunGame.Data.LoadFiles;
import me.Math0424.WitheredGunGame.Guns.GunListeners;
import me.Math0424.WitheredGunGame.SignSpawner.SignData;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

public class WitheredGunGame extends JavaPlugin {
	
	private static WitheredGunGame plugin;
	
	public static WitheredGunGame getPlugin() {
        return plugin;
    }


	public void onLoad() {
		plugin = this;

		CoreWeaponsAPI.AddPluginToUpdateChecker("71891", this);

		Config.load();
		CoreWeaponsAPI.SetResourcePackPath(Config.RESOURCEPACK.getStrVal());
	}
	@Override
	public void onEnable() {
		plugin = this;

		if (!CoreWeaponsAPI.IsInitialized()) {
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}

		ConfigurationSerialization.registerClass(SignData.class);
		ConfigurationSerialization.registerClass(Arena.class);
		ConfigurationSerialization.registerClass(ArenaFFA.class);
		ConfigurationSerialization.registerClass(ArenaPOINT.class);
		ConfigurationSerialization.registerClass(ArenaTEAM.class);

		Lang.load();
		LoadFiles.load();
		new HeartBeat();
		
		MainListeners ml = new MainListeners();
		this.getServer().getPluginManager().registerEvents(ml, this);

		GunListeners gul = new GunListeners();
		gul.registerGuns();
		this.getServer().getPluginManager().registerEvents(gul, this);

		LeaveCommand lc = new LeaveCommand();
		this.getCommand("leave").setExecutor(lc);
		
		GunGameCommands mc = new GunGameCommands();
		this.getCommand("witheredgg").setExecutor(mc);
		this.getCommand("witheredgg").setTabCompleter(mc);

	}
	
	@Override
	public void onDisable() {
		
		for (Arena a : Arena.getArenas()) {
			a.stopGame();
		}

	}

}
