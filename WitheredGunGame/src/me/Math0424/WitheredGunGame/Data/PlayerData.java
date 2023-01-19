package me.Math0424.WitheredGunGame.Data;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class PlayerData implements IPlayerData {

    private me.Math0424.CoreWeapons.Data.PlayerData data;

    private int kills = 0;
    private int deaths = 0;

    private int killStreak = 0;
    private int highestKillStreak = 0;
    private int bulletsFired = 0;

    private int wins = 0;
    private int losses = 0;

    public PlayerData() {}

    public void addToKills() {
        kills++;
        killStreak++;
        if (killStreak > highestKillStreak) {
            highestKillStreak = killStreak;
        }
    }

    public void resetKillStreak() {
        killStreak = 0;
    }

    public void addToWins() {
        wins++;
    }

    public void addToBullets(int i) {
        bulletsFired += i;
    }

    public void addToDeaths() {
        killStreak = 0;
        deaths++;
    }

    public int getLosses() {
        return losses;
    }

    public void addToLosses() {
        losses++;
    }

    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getKillStreak() {
        return killStreak;
    }

    public int getHighestKillStreak() {
        return highestKillStreak;
    }

    public int getBulletsFired() {
        return bulletsFired;
    }

    public int getCurrentXp() {
        return data.getCurrentXP();
    }

    public int getGlobalXp() {
        return data.getLifetimeXP();
    }

    public int getLevel() {
        return data.getPlayerLevel();
    }

    public void addXp(int xp) {
        data.addToXp(xp);
    }

    public int getWins() {
        return wins;
    }

    public static PlayerData getPlayerData(Player p) {
        me.Math0424.CoreWeapons.Data.PlayerData data = me.Math0424.CoreWeapons.Data.PlayerData.getPlayerData(p.getUniqueId());
        PlayerData playerData = data.getComponent(PlayerData.class);
        if (playerData == null) {
            playerData = new PlayerData();
            data.addComponent(playerData);
        }
        playerData.data = data;
        return playerData;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("kills", this.kills);
        map.put("deaths", this.deaths);
        map.put("killStreak", this.killStreak);
        map.put("highestKillStreak", this.highestKillStreak);
        map.put("bulletsFired", this.bulletsFired);
        map.put("losses", this.losses);
        map.put("wins", this.wins);

        return map;
    }

    @Override
    public void deSerialize(Map<String, Object> map) {
        this.kills = ((Double) map.get("kills")).intValue();
        this.deaths = ((Double) map.get("deaths")).intValue();
        this.killStreak = ((Double) map.get("killStreak")).intValue();
        this.highestKillStreak = ((Double) map.get("highestKillStreak")).intValue();
        this.bulletsFired = ((Double) map.get("bulletsFired")).intValue();
        this.wins = ((Double) map.get("wins")).intValue();
        this.losses = ((Double) map.get("losses")).intValue();
    }

    public String toString() {
        return "Name: " + Bukkit.getPlayer(data.getPlayerUUID()) + "\n"
                + "Xp: " + getGlobalXp() + "\n"
                + "Kills: " + getKills() + "\n"
                + "Deaths: " + getDeaths() + "\n"
                + "HighestKillStreak: " + getHighestKillStreak() + "\n"
                + "Wins: " + getWins() + "\n"
                + "BulletsFired: " + getBulletsFired();
    }


}

