package me.Math0424.WitheredControl;

import me.Math0424.WitheredControl.Arenas.Arena;
import me.Math0424.WitheredControl.Arenas.ArenaFFA;
import me.Math0424.WitheredControl.Arenas.ArenaPOINT;
import me.Math0424.WitheredControl.Arenas.ArenaTEAM;
import me.Math0424.WitheredControl.Commands.ControlCommands;
import me.Math0424.WitheredControl.Commands.LeaveCommand;
import me.Math0424.WitheredControl.Data.LoadFiles;
import me.Math0424.WitheredControl.Data.PlayerData;
import me.Math0424.WitheredControl.Data.SaveFiles;
import me.Math0424.WitheredControl.Guns.GunListeners;
import me.Math0424.WitheredControl.SignSpawner.SignData;
import me.Math0424.WitheredControl.SignSpawner.SignListener;
import me.Math0424.WitheredControl.Util.WitheredUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.concurrent.Callable;

public class WitheredControl extends JavaPlugin {
	
	private static WitheredControl plugin;
	
	public static WitheredControl getPlugin() {
        return plugin;
    }
	
	public ArrayList<PlayerData> playerData = new ArrayList<PlayerData>();
	
	public void onEnable() {
		plugin = this;
		
		setServerVersion();

		ConfigurationSerialization.registerClass(PlayerData.class);
		ConfigurationSerialization.registerClass(SignData.class);
		ConfigurationSerialization.registerClass(Arena.class);
		ConfigurationSerialization.registerClass(ArenaFFA.class);
		ConfigurationSerialization.registerClass(ArenaPOINT.class);
		ConfigurationSerialization.registerClass(ArenaTEAM.class);

		Lang.load();
		Config.load();
		new LoadFiles();
		new HeartBeat();

		MainListeners ml = new MainListeners();
		this.getServer().getPluginManager().registerEvents(ml, this);

		GunListeners gul = new GunListeners();
		gul.registerGuns();
		this.getServer().getPluginManager().registerEvents(gul, this);

		LeaveCommand lc = new LeaveCommand();
		this.getCommand("leave").setExecutor(lc);

		ControlCommands mc = new ControlCommands();
		this.getCommand("witheredcontrol").setExecutor(mc);
		this.getCommand("witheredcontrol").setTabCompleter(mc);

		SignListener sil = new SignListener();
		sil.startSignTimer();
		
		Metrics m = new Metrics(this);
		m.addCustomChart(new Metrics.SimplePie("version", new Callable<String>() {
	        @Override
	        public String call() throws Exception {
	            return "Withered: Control";
	        }
	    }));
		
		checkupdates.start();
		
	}
	
	public void onDisable() {
		getServer().getScheduler().cancelTasks(this);
		new SaveFiles();
	}
	
	Thread checkupdates = new Thread(){
        public void run(){
        	URL url = null;
        	URLConnection conn = null;
            try {
                url = new URL("https://api.spigotmc.org/legacy/update.php?resource=60958");
                conn = url.openConnection();
                String version = new BufferedReader(new InputStreamReader(conn.getInputStream())).readLine();
                String pluginVersion = WitheredControl.getPlugin().getDescription().getVersion();
                
                Integer versionNumber = Integer.parseInt(version.replace(".", ""));
                Integer pluginVersionNumber = Integer.parseInt(pluginVersion.replace(".", ""));
                
                if(versionNumber.equals(pluginVersionNumber)) {
                    WitheredUtil.info(ChatColor.GREEN + "You are running on the latest version");
                } else if(versionNumber < pluginVersionNumber) {
                	WitheredUtil.info(ChatColor.GREEN + "You are running on a snapshot version!");
                } else {
                	WitheredUtil.info(ChatColor.RED + "There is an update! latest version: " + version + " you're still on version: " + pluginVersion);
                	WitheredUtil.info(ChatColor.RED + "Download it at https://www.spigotmc.org/resources/withered.60958/");
                }
            } catch (Exception e) {
            	WitheredUtil.info(ChatColor.RED + "Error while checking for updates.");
            	e.printStackTrace();
            }
        }
	};
	
	public void setServerVersion() {
		String version = Bukkit.getServer().getClass().getPackage().getName();
		version = version.substring(version.lastIndexOf("."));
		switch(version) {
		case "v1_14_R1":
			break;
		default:
			WitheredUtil.info(ChatColor.RED + "Invalid server version!");
			Bukkit.getPluginManager().disablePlugin(this);
			break;
		}
	}
	
}
