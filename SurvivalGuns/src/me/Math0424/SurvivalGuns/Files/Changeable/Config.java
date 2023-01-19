package me.Math0424.SurvivalGuns.Files.Changeable;

import me.Math0424.SurvivalGuns.Files.FileLoader;
import me.Math0424.SurvivalGuns.SurvivalGuns;
import org.bukkit.ChatColor;

import java.util.List;
import java.util.logging.Level;

public enum Config {

    //witheredAPI
    MAXPRIMARYGUNS("MaxPrimaryGuns", 1),
    MAXSECONDARYGUNS("MaxSecondaryGuns", 2),
    BLOCKPHYSICS("BlockPhysics", true),

    //resource pack
    RESOURCEPACK("ResourcePack", "https://www.dropbox.com/s/lpguq2rsgm091fr/Withered.zip?dl=1"),

    GIVERECIPIES("GiveRecipies", true),

    ENABLENUKES("NukeDamage", true),
    BLOCKDAMAGE("BlockDamage", true);

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
                SurvivalGuns.log(Level.SEVERE, "Missing config option '" + lang.getLocation() + "' setting to default option: " + lang.getValue());
            }
        }
    }

}
