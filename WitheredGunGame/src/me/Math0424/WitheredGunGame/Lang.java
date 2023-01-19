package me.Math0424.WitheredGunGame;

import me.Math0424.CoreWeapons.Util.MyUtil;
import me.Math0424.WitheredGunGame.Util.WitheredUtil;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

public enum Lang {
	
	YOUCANNOTDOTHAT("util.cannotdothat", "You cannot do that!"),
	KILLSTREAK("util.killstreak", "&d{player} is on a killstreak of {kills}!"),

	ARENAFULL("arena.full", "&cCannot join full arena!"),
	ARENAENDING("arena.ending", "&cGame is ending!"),
	ARENANOPLAYERS("arena.minplayers", "&cNot enough players to continue!"),
	ARENAGAMEOVER("arena.gameover", "&e{winner} wins! game ending in 10 seconds"),
	ARENAMPVWINNER("arena.mvp", "&e{player} is MVP!"),
	ARENACOUNTDOWN("arena.countdown", "&e{time} seconds till game start."),

	ARENAPLAYERFINALKILL("arena.player.final", "&d{player} is on the last kill!"),
	ARENAPlAYERJOIN("arena.player.join", "&e{player} has joined the game!"),
	ARENAPLAYERLEAVE("arena.player.leave", "&e{player} has left the game!"),

	DEATHBULLET("death.bullet", "&2{killed} &6was shot by &2{killer} &6using a {gun}"),
	DEATHBULLETHEADSHOT("death.headshot", "&2{killed} &6was shot in the head by &2{killer} &6using a {gun}"),
	DEATHLASER("death.laser", "&2{killed} &6was fried by &2{killer}&6 using a laser!"),
	DEATHFIRE("death.flamethrower", "&2{killed} &6was fried by &2{killer}!"),
	DEATHACID("death.acid", "&2{killed} &6was melted by &2{killer} using a {gun}!"),

	DEATHGENERIC("death.generic", "&2{killed} &6died"),
	DEATHGENERICEXPLOSION("death.genericexplosion", "&2{killed} &6died to an explosion"),
	DEATHGENERICFALL("death.genericfall", "&2{killed} &6fell to their death"),
	DEATHGENERICFALLESCAPE("death.genericfallescape", "&2{killed} &6fell to their death while escaping {killer}");


	private final String location;
	private final String value;
	private static FileConfiguration LANG;
    
	Lang(String location, String value) {
		this.location = location;
		this.value = value;
	}
	
	@Override
	public String toString() {
		return ChatColor.translateAlternateColorCodes('&', LANG.getString(this.getLocation()));
	}

	public String getValue() {
		return this.value;
	}
	
	public static void setLang(FileConfiguration ymal) {
		LANG = ymal;
	}

	public String getLocation() {
		return this.location;
	}
	
	private static File langConfigFile;
    private static FileConfiguration langConfig;

	public static String getRandomValue(String toSearch) {
		try {
			List<String> langString = langConfig.getStringList(toSearch);
			if (langString.size() == 1) {
				return ChatColor.translateAlternateColorCodes('&', langString.get(0));
			}
			return ChatColor.translateAlternateColorCodes('&', langString.get(MyUtil.random(langString.size() - 1)));
		} catch (Exception e) {
			return "Lang desc not found '" + toSearch + "'";
		}

	}

    public static void load() {
    	langConfigFile = new File(WitheredGunGame.getPlugin().getDataFolder(), "Config/lang.yml");
    	if (!langConfigFile.exists()) {
    		langConfigFile.getParentFile().mkdirs();
            WitheredGunGame.getPlugin().saveResource("Config/lang.yml", false);
    	}
    	langConfig = new YamlConfiguration();
    	try {
    		langConfig.load(langConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
		for(Lang lang : Lang.values()) {
	        if (langConfig.getString(lang.getLocation()) == null) {
	        	langConfig.set(lang.getLocation(), lang.getValue());
	        	WitheredUtil.log(Level.INFO, ChatColor.RED + "Missing lang option '" + lang.getLocation() + "' setting to default option: '" + lang.getValue() + "'");
	        }
	    }
		Lang.setLang(langConfig);
	}
	
}
