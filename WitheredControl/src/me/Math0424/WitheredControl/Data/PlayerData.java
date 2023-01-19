package me.Math0424.WitheredControl.Data;

import me.Math0424.WitheredControl.Util.WitheredUtil;
import me.Math0424.WitheredControl.WitheredControl;
import org.bukkit.ChatColor;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.HashMap;
import java.util.Map;

public class PlayerData implements ConfigurationSerializable {
	
	private String name = "Unknown";
	private Integer deaths = 0;
	private Integer kills = 0;
	private Integer killStreak = 0;
	private Integer highestKillStreak = 0;
	private Integer money = 100;
	private Integer bank = 0;
	private Integer bulletsFired = 0;
	private Integer grenadesThrown = 0;
	private Integer wins = 0;
	private Integer resetLevel = 0;
	
	public PlayerData(String name) {
		this.name = name;
	}

	public void addToDeaths() {
		killStreak = 0;
		deaths++;
	}
	
	public void addToKills() {
		killStreak++;
		kills++;
		if (killStreak > highestKillStreak) {
			highestKillStreak = killStreak;
		}
	}
	
	public void addToWins() {
		wins++;
	}
	
	public void addToBullets() {
		bulletsFired++;
	}
	
	public void addToGrenades() {
		grenadesThrown++;
	}
	
	public void subtractMoney(int x) {
		money -= x;
	}
	
	public void addMoney(int x) {
		money += x;
	}
	
	public void setMoney(int x) {
		money = x;
	}
	
	public void subtractBankMoney(int x) {
		bank -= x;
	}
	
	public void addBankMoney(int x) {
		bank += x;
	}
	
	public void setBankMoney(int x) {
		bank = x;
	}
	
	public int getBankMoney() {
		return bank;
	}
	
	public void setResetLevel(int i) {
		resetLevel = i;
	}
	
	public String getName() {
		return name;
	}

	public Integer getDeaths() {
		return deaths;
	}

	public Integer getKills() {
		return kills;
	}

	public Integer getKillStreak() {
		return killStreak;
	}

	public Integer getHighestKillStreak() {
		return highestKillStreak;
	}

	public Integer getMoney() {
		return money;
	}

	public Integer getBulletsFired() {
		return bulletsFired;
	}

	public Integer getGrenadesThrown() {
		return grenadesThrown;
	}

	public Integer getWins() {
		return wins;
	}

	public Integer getResetLevel() {
		return resetLevel;
	}
	
	public static PlayerData getData(String p) {
		for (PlayerData data : WitheredControl.getPlugin().playerData) {
			if (data.getName().equalsIgnoreCase(p)) {
				return data;
			}
		}
		return null;
	}
	
	public PlayerData(Map<String, Object> map) {
		try {
			this.name = (String) map.get("name");
			this.kills = (Integer) map.get("kills");
			this.deaths = (Integer) map.get("deaths");
			this.wins = (Integer) map.get("wins");
			this.money = (Integer) map.get("money");
			this.bank = (Integer) map.get("bank");
			this.killStreak = (Integer) map.get("killStreak");
			this.highestKillStreak = (Integer) map.get("highestKillStreak");
			this.bulletsFired = (Integer) map.get("bulletsFired");
			this.grenadesThrown = (Integer) map.get("grenadesThrown");
			this.resetLevel = (Integer) map.get("resetLevel");
		} catch (Exception e) {
			WitheredUtil.info(ChatColor.RED + "Failed to load playerData");
			e.printStackTrace();
		}
	}
	
	public static PlayerData deserialize(Map<String, Object> map) {
		PlayerData p = new PlayerData(map);
		WitheredControl.getPlugin().playerData.add(p);
        return p;
    }
	
	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<>();
		map.put("name", name);
		map.put("deaths", deaths);
		map.put("wins", wins);
		map.put("kills", kills);
		map.put("killStreak", killStreak);
		map.put("money", money);
		map.put("bank", bank);
		map.put("highestKillStreak", highestKillStreak);
		map.put("bulletsFired", bulletsFired);
		map.put("grenadesThrown", grenadesThrown);
		map.put("resetLevel", resetLevel);
		return map;
	}
	
}
