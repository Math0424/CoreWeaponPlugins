package me.Math0424.WitheredControl;

import me.Math0424.WitheredControl.Util.WitheredUtil;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public enum Lang {
	
	YOUCANNOTDOTHAT("util.cannotdothat", "You cannot do that!"),
	KILLSTREAK("util.killstreak", "&d{player} is on a killstreak of {kills}!"),
	
	ARMORSPEEDBOOTSBROKEN("armor.speedboots.broken", "Your speedBoots are out of fuel!"),
	ARMORSPEEDBOOTSLOW("armor.speedboots.runningLow", "Your speedBoots are running low on fuel!"),
	
	GUNCANNOTFIREWHILERELAOD("gun.cannotfirereload", "Cannot fire while reloading!"),
	GUNRELOADING("gun.reloading", "Reloading..."),
	
	STATSSHOWPLAYERDATA("stats.showplayerdata", "Showing player data for "),
	STATSKILLS("stats.kills", "Kills"),
	STATSDEATHS("stats.deaths", "Deaths"),
	STATSHIGHESTKILLS("stats.hishestkills", "HighestKillStreak"),
	STATSBULLETSFIRED("stats.bulletsfired", "BulletsFired"),
	STATSGAMEWON("stats.gameswon", "Games won"),
	
	DEATHBULLET("death.bullet", "&2{killed} &6was shot by &2{killer} &6using a {gun}"),
	DEATHBULLETHEADSHOT("death.bulletheadshot", "&2{killed} &6was shot in the head by &2{killer} &6using a {gun}"),
	DEATHGENERICEXPLOSION("death.genericexplosion", "&2{killed} &6died to an explosion"),
	DEATHGENERICFALL("death.genericfall", "&2{killed} &6fell to their death"),
	DEATHGENERICFALLESCAPE("death.genericfallescape", "&2{killed} &6fell to their death while escaping {killer}"),
	DEATHGENERIC("death.generic", "&2{killed} &6died"),
	DEATHLASER("death.laser", "&2{killed} &6was fried by &2{killer}&6 using a laser!"),
	DEATHFIRE("death.flamethrower", "&2{killed} &6was fried by &2{killer}!"),
	DEATHACID("death.acid", "&2{killed} &6was melted by &2{killer} using a {gun}!");

	private String location;
	private String value;
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
	
	public void setValue(String s) {
		this.setValue(s);
	}
	
	public String getLocation() {
		return this.location;
	}
	
	private static File langConfigFile;
    private static FileConfiguration langConfig;
    
    public static void load() {
    	langConfigFile = new File(WitheredControl.getPlugin().getDataFolder(), "Config/lang.yml");
    	if (!langConfigFile.exists()) {
    		langConfigFile.getParentFile().mkdirs();
			WitheredControl.getPlugin().saveResource("Config/lang.yml", false);
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
	        	WitheredUtil.info(ChatColor.RED + "Missing lang option '" + lang.getLocation() + "' setting to default option: '" + lang.getValue() + "'");
	        }
	    }
		Lang.setLang(langConfig);
	}
	
}
