package me.Math0424.Withered.Files.Changeable;

import me.Math0424.Withered.Files.FileLoader;
import me.Math0424.Withered.Util.WitheredUtil;
import org.bukkit.ChatColor;

import java.util.List;
import java.util.logging.Level;

public enum Config {

    //chat
    //TODO: support papi
    CHATFORMAT("ChatFormat", "&7[[type]] [player]&6[squad]&7: [message]"),
    LOCALCHATRANGE("LocalChatRange", 200),

    //crates
    GLOBALEVENTTIME("GlobalEventTime", 60),
    LOCALEVENTTIME("LocalEventTime", 10),
    GLOBALEVENTMINPLAYERS("GlobalEventMinPlayers", 3),
    WEAPONSCACHELEVEL("WeaponsCacheLevel", 25),
    DROPCRATELEVEL("DropCrateLevel", 50),
    DROPCRATESPAWNTIME("DropCrateSpawnTime", 10),

    ENDGAMEDIAMONDDAY("EndGameDiamondDay", 1),

    //signs
    DEFAULTSPAWNGUN("DefaultSpawnGun", "P226"),
    DEFAULTSPAWNITEM("DefaultSpawnItem", "Rations"),

    //chests
    CHESTPOPULATIONRATE("ChestPopulationTime", 20),
    CHESTNEWCHANCE("ChestNewGunChance", 3),
    CHESTFAIRCHANCE("ChestFairGunChance", 10),
    CHESTUSEDCHANCE("ChestUsedGunChance", 30),
    CHESTWORNCHANCE("ChestWornGunChance", 60),
    CHESTPOP("ChestPop", true),

    //world
    DESTRUCRIBLEWORLD("DestructibleWorld", true),
    SPAWNPROTECTDISTANCE("SpawnProtectSize", 50),
    KEEPPLAYERINVENTORYONRESET("KeepPlayerInventoryOnReset", true),
    MAXCARSPAWNS("MaxCarSpawns", 20),

    //witheredAPI
    MAXPRIMARYGUNS("MaxPrimaryGuns", 1),
    MAXSECONDARYGUNS("MaxSecondaryGuns", 2),

    //squad
    SQUADFRIENDLYFIREPUNISH("SquadFriendlyFirePunish", true),
    SQUADFRIENDLYFIRE("SquadFriendlyFire", true),

    //currency
    CURRENCYNAME("CurrencyName", "&2Money&r"),
    CURRENCYSTARTINGVALUE("StartingValue", 100),

    //gameplay
    EMPTYSLOTS("EmptySlots", 2),
    ZOMBIESMODE("ZombiesMode", false),
    DEATHBYWATER("DeathByWater", true),
    NAMETAGVISIBILITY("NameTagVisibility", false),

    //other
    DEBUGMODE("DebugMode", false),
    DISABLECARSPAWNS("DisableCarSpawns", false),
    SERVERLANG("ServerLang", "english"),
    CARSUSEROADBLOCK("CarsUseRoadBlocks", true),

    //mech suit configs
    MECHSUITHEALTH("MechSuitHealth", 500),
    MECHSUITPRIMARYGUN("MechSuitPrimaryGun", "M240"),
    MECHSUITSECONDARYGUN("MechSutiSecondaryGun", "RocketLauncher"),

    //resource pack
    RESOURCEPACK("ResourcePack", "https://www.dropbox.com/s/lpguq2rsgm091fr/Withered.zip?dl=1");


    private final String location;
    private final Object value;

    Config(String location, Object val) {
        this.location = location;
        this.value = val;
    }

    public String getStrVal() {
        return ChatColor.translateAlternateColorCodes('&', FileLoader.config.getObject(this.getLocation(), String.class));
    }

    public Integer getIntVal() {
        return FileLoader.config.getObject(this.getLocation(), Integer.class);
    }

    public Boolean getBoolVal() {
        return FileLoader.config.getObject(this.getLocation(), Boolean.class);
    }

    public List<String> getStringArrayVal() {
        return FileLoader.config.getObject(this.getLocation(), List.class);
    }

    public Object getValue() {
        return value;
    }

    public String getLocation() {
        return this.location;
    }

    public static void load() {
        for (Config lang : Config.values()) {
            if (FileLoader.config.getString(lang.getLocation()) == null) {
                FileLoader.config.set(lang.getLocation(), lang.getValue());
                WitheredUtil.log(Level.SEVERE, "Missing config option '" + lang.getLocation() + "' setting to default option: " + lang.getValue());
            }
        }
    }

}
