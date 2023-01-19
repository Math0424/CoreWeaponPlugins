package me.Math0424.Withered.Core;

import me.Math0424.Withered.Chat.TalkType;
import me.Math0424.Withered.Files.Changeable.Config;
import me.Math0424.Withered.Files.Languages;
import me.Math0424.CoreWeapons.Util.InventoryUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;

public class PlayerData implements IPlayerData {

    private me.Math0424.CoreWeapons.Data.PlayerData data;


    private int bankCash = 0;
    private int cash = 0;

    private int kills = 0;
    private int deaths = 0;

    private int killStreak = 0;
    private int highestKillStreak = 0;
    private int bulletsFired = 0;
    private int grenadesFired = 0;
    private int wins = 0;

    private Inventory inventory = Bukkit.createInventory(null, 54);
    private Location location;

    private int resetLevel = 0;

    private Languages lang = Languages.ENGLISH;
    private TalkType talkType = TalkType.LOCAL;

    public PlayerData() {
    }

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

    public void addToGrenadesThrown() {
        grenadesFired++;
    }

    public void addToDeaths() {
        deaths++;
    }

    public void addToCash(int i) {
        cash += i;
    }

    public void subtractFromCash(int i) {
        cash -= i;
    }

    public void subtractFromBank(int i) {
        bankCash -= i;
    }

    public void addToBankCash(int i) {
        bankCash += i;
    }

    public void setResetLevel(int i) {
        this.resetLevel = i;
    }

    public int getResetLevel() {
        return resetLevel;
    }

    public int getBankCash() {
        return bankCash;
    }

    public int getCash() {
        return cash;
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

    public int getGrenadesFired() {
        return grenadesFired;
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

    public void setCash(int i) {
        cash = i;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void resetInventory() {
        this.inventory = Bukkit.createInventory(null, 54);
    }

    public void updatePlayerInvLocation() {
        this.inventory = Bukkit.getPlayer(data.getPlayerUUID()).getInventory();
        this.location = Bukkit.getPlayer(data.getPlayerUUID()).getLocation();
    }

    public Location getLocation() {
        return location;
    }

    public int getWins() {
        return wins;
    }

    public Languages getLang() {
        return lang;
    }

    public void setLang(Languages lang) {
        this.lang = lang;
    }

    public TalkType getTalkType() {
        return talkType;
    }

    public void setTalkType(TalkType type) {
        this.talkType = type;
    }

    public static PlayerData getPlayerData(Player p) {
        me.Math0424.CoreWeapons.Data.PlayerData data = me.Math0424.CoreWeapons.Data.PlayerData.getPlayerData(p.getUniqueId());
        PlayerData playerData = data.getComponent(PlayerData.class);
        if (playerData == null) {
            playerData = new PlayerData();

            playerData.data = data;
            playerData.location = p.getWorld().getSpawnLocation();
            playerData.cash = Config.CURRENCYSTARTINGVALUE.getIntVal();

            data.addComponent(playerData);
        }
        playerData.data = data;
        return playerData;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("bankCash", this.bankCash);
        map.put("cash", this.cash);
        map.put("kills", this.kills);
        map.put("deaths", this.deaths);
        map.put("killStreak", this.killStreak);
        map.put("highestKillStreak", this.highestKillStreak);
        map.put("bulletsFired", this.bulletsFired);
        map.put("grenadesFired", this.grenadesFired);
        map.put("wins", this.wins);
        map.put("location", this.location.serialize());
        map.put("resetLevel", this.resetLevel);
        map.put("lang", this.lang.toString());
        map.put("talkType", this.talkType.toString());

        map.put("inventory", InventoryUtil.toString(this.inventory));

        return map;
    }

    @Override
    public void deSerialize(Map<String, Object> map) {
        this.bankCash = ((Double) map.get("bankCash")).intValue();
        this.cash = ((Double) map.get("cash")).intValue();
        this.kills = ((Double) map.get("kills")).intValue();
        this.deaths = ((Double) map.get("deaths")).intValue();
        this.killStreak = ((Double) map.get("killStreak")).intValue();
        this.highestKillStreak = ((Double) map.get("highestKillStreak")).intValue();
        this.bulletsFired = ((Double) map.get("bulletsFired")).intValue();
        this.grenadesFired = ((Double) map.get("grenadesFired")).intValue();
        this.wins = ((Double) map.get("wins")).intValue();
        this.location = Location.deserialize((Map<String, Object>) map.get("location"));
        this.resetLevel = ((Double) map.get("resetLevel")).intValue();
        this.lang = Languages.valueOf((String) map.get("lang"));
        this.talkType = TalkType.valueOf((String) map.get("talkType"));

        this.inventory = InventoryUtil.fromString((String) map.get("inventory"));
    }

    public String toString() {
        return "Name: " + Bukkit.getPlayer(data.getPlayerUUID()).getName() + "\n"
                + "Xp: " + getGlobalXp() + "\n"
                + "Kills: " + getKills() + "\n"
                + "Deaths: " + getDeaths() + "\n"
                + "HighestKillStreak: " + getHighestKillStreak() + "\n"
                + "Wins: " + getWins() + "\n"
                + "BulletsFired: " + getBulletsFired() + "\n"
                + "GrenadesThrown: " + getGrenadesFired() + "\n";
    }


}

