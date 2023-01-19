package me.Math0424.WitheredGunGame.Arenas;

import me.Math0424.CoreWeapons.DamageHandler.DamageExplainer;
import me.Math0424.CoreWeapons.Guns.QualityEnum;
import me.Math0424.CoreWeapons.Managers.GunManager;
import me.Math0424.WitheredGunGame.Data.PlayerData;
import me.Math0424.WitheredGunGame.Guns.GunListeners;
import me.Math0424.WitheredGunGame.Lang;
import me.Math0424.CoreWeapons.DamageHandler.CoreDamage;
import me.Math0424.CoreWeapons.Guns.Gun.Gun;
import me.Math0424.WitheredGunGame.WitheredGunGame;
import org.bukkit.*;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Arena implements ConfigurationSerializable {

    private static final ArrayList<Arena> arenas = new ArrayList<Arena>();

    protected Scoreboard scoreboard = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
    protected Map<Player, Integer> inGame = new HashMap<>();

    protected Map<Player, Integer> dead = new HashMap<>();

    protected HashMap<Player, Location> enterLocation = new HashMap<>();
    protected Phase phase = Phase.LOBBY;
    protected int startingSeconds = 60;

    //serializable
    protected ArenaType type;
    protected String name;
    protected Location lobbyLoc;

    protected Integer maxPlayers, minPlayers;
    protected List<Location> playerSpawns = new ArrayList<>();

    public Arena(String name, ArenaType type, int minPlayers, int maxPlayers) {
        this.name = name;
        this.type = type;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        arenas.add(this);
    }

    protected Arena() {
    }

    public Team createTeam(String s, ChatColor c, boolean friendlyFire) {
        scoreboard.registerNewTeam(s);
        Team t = scoreboard.getTeam(s);
        t.setCanSeeFriendlyInvisibles(false);
        t.setAllowFriendlyFire(friendlyFire);
        t.setColor(c);
        t.setPrefix(c.toString());
        return t;
    }

    public Team getTeam(Entity e) {
        for (Team t : scoreboard.getTeams()) {
            if (t.getEntries().contains(e.getName())) {
                return t;
            }
        }
        return null;
    }

    public void setDead(Player p, int i) {
        dead.put(p, i);
        updateGun(p);
        p.setHealth(20);
        GunManager.addToDisableFire(p, i);
        playerDied(p);
        for (Player o : inGame.keySet()) {
            o.hidePlayer(WitheredGunGame.getPlugin(), p);
        }
    }

    public void updateGun(Player p) {
        if (inGame.get(p) == null || phase != Phase.RUNNING || dead.containsKey(p)) {
            p.getInventory().setItem(0, null);
            return;
        }
        GunManager.addToDisableFire(p, 10);
        Gun g = GunListeners.getGuns().get(inGame.get(p));
        if (g != null) {
            p.getInventory().setItem(0, Gun.getGunItemStack(g, QualityEnum.NEW));
        }
    }

    public void playerDeath(Player p) {
        if (inGame.containsKey(p)) {
            PlayerData.getPlayerData(p).addToDeaths();
            CoreDamage damage = CoreDamage.getLastDamage(p);
            for (PotionEffect effect : p.getActivePotionEffects()){
                p.removePotionEffect(effect.getType());
            }

            //fall damage detection
            EntityDamageEvent.DamageCause cause = p.getLastDamageCause().getCause();
            if (cause == EntityDamageEvent.DamageCause.FALL) {
                if (damage != null && damage.getDamager() instanceof Player) {
                    addToKills((Player) damage.getDamager());
                    sendMessage(Lang.DEATHGENERICFALLESCAPE.toString().replace("{killer}", getTeam(damage.getDamager()).getColor()+damage.getDamager().getName()).replace("{killed}", getTeam(damage.getDamaged()).getColor()+damage.getDamaged().getName()));
                } else {
                    sendMessage(Lang.DEATHGENERICFALL.toString().replace("{killed}", getTeam(p).getColor() + p.getName()));
                }
                setDead(p, 100);
                return;
            }

            if (damage != null && !damage.getCause().hasNoAttacker()) {
                sendKillerMessage(p, damage.getDamager(), damage.getGun(), damage.getCause());
            }
            setDead(p, 100);
        }
    }

    private void sendKillerMessage(LivingEntity dead, Entity killer, Gun g, DamageExplainer damage) {
        if (killer instanceof Player) {
            addToKills((Player) killer);
            PlayerData.getPlayerData((Player) killer).addXp(50);

        }
        CoreDamage.clearLastDamage(dead);
        String returned = Lang.getRandomValue("deaths." + damage.getName());
        if (g != null) {
            g.addToKills();
            returned = returned.replace("{gun}", g.getName());
        }
        for (Player p : inGame.keySet()) {
            p.sendMessage(returned.replace("{dead}", dead.getName()).replace("{killer}", killer.getName()));
        }
    }

    public void sendMessage(String m, Sound s) {
        for (Player p : inGame.keySet()) {
            p.sendMessage(m);
            p.playSound(p.getLocation(), s, 10, 1);
        }
    }

    public void sendMessage(String m) {
        for (Player p : inGame.keySet()) {
            p.sendMessage(m);
        }
    }

    public void setHelmet(Player p, Color color, String name) {
        ItemStack item = new ItemStack(Material.LEATHER_HELMET);
        LeatherArmorMeta meta2 = (LeatherArmorMeta) item.getItemMeta();
        meta2.setColor(color);
        meta2.setDisplayName(name);
        meta2.setUnbreakable(true);
        meta2.removeItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        item.setItemMeta(meta2);
        item.setItemMeta(meta2);
        p.getInventory().setHelmet(item);
    }

    //Overrides
    public abstract void addPlayer(Player p, boolean silent);
    public abstract void removePlayer(Player p, boolean silent);

    public abstract void playerDied(Player p);
    public abstract void playerRespawned(Player p);

    public abstract void startEndGame();
    public abstract void checkEndGame();

    public abstract void startStartGame();
    public abstract void checkStartGame();

    public abstract void stopGame();

    public abstract void spawnPlayerInArena(Player p, boolean silent);

    public abstract void addToKills(Player p);

    //statics
    public static ArrayList<Arena> getArenas() {
        return arenas;
    }

    public static Arena getArena(Player p) {
        for (Arena a : arenas) {
            if (a.inGame.containsKey(p)) {
                return a;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public ArenaType getType() {
        return type;
    }

    public Location getLobbyLoc() {
        return lobbyLoc;
    }

    public Map<Player, Integer> getInGame() {
        return inGame;
    }

    public Integer getMaxPlayers() {
        return maxPlayers;
    }

    public List<Location> getPlayerSpawns() {
        return playerSpawns;
    }

    public void setLobbyLoc(Location lobbyLoc) {
        this.lobbyLoc = lobbyLoc;
    }

    public void setStartingSeconds(int startingSeconds) {
        this.startingSeconds = startingSeconds;
    }

    public static Arena getArenaByName(String s) {
        for (Arena a : arenas) {
            if (a.name.equals(s)) {
                return a;
            }
        }
        return null;
    }

    public Map<Player, Integer> getDead() {
        return dead;
    }

    public Phase getPhase() {
        return phase;
    }

    public enum Phase {
        LOBBY,
        STARTING,
        RUNNING,
        ENDING,
    }

}
