package me.Math0424.SurvivalGuns.Listeners;

import me.Math0424.CoreWeapons.DamageHandler.CoreDamage;
import me.Math0424.CoreWeapons.DamageHandler.DamageExplainer;
import me.Math0424.CoreWeapons.Data.PlayerData;
import me.Math0424.CoreWeapons.Guns.Gun.Gun;
import me.Math0424.CoreWeapons.Util.MyUtil;
import me.Math0424.SurvivalGuns.Files.FileLoader;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.List;

public class DeathListener implements Listener {

    @EventHandler
    public void playerDeathEvent(PlayerDeathEvent e) {
        Player p = e.getEntity();

        CoreDamage damage = CoreDamage.getLastDamage(p);
        if (damage != null) {

            if (damage.getCause().hasNoAttacker()) {
                e.setDeathMessage(getDeathMessage(p, damage.getCause()));
            } else {
                if (damage.getGun() == null) {
                    e.setDeathMessage(getKillerMessage(p, damage.getDamager(), null, damage.getCause()));
                } else {
                    e.setDeathMessage(getKillerMessage(p, damage.getDamager(), damage.getGun().getObject(), damage.getCause()));
                }
            }
        }
    }

    private String getDeathMessage(LivingEntity dead, DamageExplainer damage) {
        CoreDamage.clearLastDamage(dead);
        String returned = getRandomValue(damage.getName());
        return returned.replace("{dead}", dead.getName());
    }

    private String getKillerMessage(LivingEntity dead, Entity killer, Gun g, DamageExplainer damage) {
        if (killer instanceof Player p) {
            PlayerData.getPlayerData(p.getUniqueId()).addTo("survivalguns-player-kills", 1);
        }
        CoreDamage.clearLastDamage(dead);
        String returned = getRandomValue(damage.getName());
        if (g != null) {
            returned = returned.replace("{gun}", g.getName());
        }
        return returned.replace("{dead}", dead.getName()).replace("{killer}", killer.getName());
    }

    public static String getRandomValue(String toSearch) {
        try {
            List<String> langString = FileLoader.lang.getStringList(toSearch);
            if (langString.size() == 1) {
                return ChatColor.translateAlternateColorCodes('&', langString.get(0));
            }
            return ChatColor.translateAlternateColorCodes('&', langString.get(MyUtil.random(langString.size() - 1)));
        } catch (Exception e) {
            return "Lang desc not found '" + toSearch + "'";
        }
    }

}
