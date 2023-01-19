package me.Math0424.Withered.Listeners;

import me.Math0424.Withered.Core.PlayerData;
import me.Math0424.Withered.Currency.CurrencyManager;
import me.Math0424.Withered.Event.EventManager;
import me.Math0424.Withered.Event.Events.Global.EndgameDiamondEvent;
import me.Math0424.Withered.Files.Changeable.Config;
import me.Math0424.Withered.Files.Changeable.Lang;
import me.Math0424.Withered.Inventory.InventoryManager;
import me.Math0424.CoreWeapons.DamageHandler.DamageExplainer;
import me.Math0424.CoreWeapons.DamageHandler.CoreDamage;
import me.Math0424.CoreWeapons.Guns.Gun.Gun;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class DeathListener implements Listener {

    static List<Material> removeMaterials = Arrays.asList(
            Material.COMPASS, Material.GRAY_STAINED_GLASS_PANE,
            Material.LEATHER_HELMET, Material.DIAMOND, Material.ENDER_EYE,
            Material.IRON_INGOT
    );


    @EventHandler
    public void playerDeathEvent(PlayerDeathEvent e) {
        Player p = e.getEntity();

        EntityDamageEvent.DamageCause cause = p.getLastDamageCause().getCause();
        CoreDamage damage = CoreDamage.getLastDamage(p);

        List<ItemStack> list = e.getDrops();
        list.removeIf(item -> removeMaterials.contains(item.getType()) || InventoryManager.getUnTouchables().containsValue(item));
        p.setLevel(0);
        p.setExp(0);
        e.setDroppedExp(0);

        PlayerData data = PlayerData.getPlayerData(p);
        data.addToDeaths();
        data.resetKillStreak();
        CurrencyManager.dropCurrency(p, data.getCash());
        data.setCash(Config.CURRENCYSTARTINGVALUE.getIntVal());


        if (cause == EntityDamageEvent.DamageCause.FALL) {
            sendDeathMessage(p, new DamageExplainer("generic.fall"));
            return;
        } else if (p.getLastDamageCause() instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent dmgEvent = (EntityDamageByEntityEvent) p.getLastDamageCause();
            if (dmgEvent.getDamager() instanceof Zombie || dmgEvent.getDamager() instanceof Husk) {
                sendDeathMessage(p, new DamageExplainer("zombie"));
                return;
            }
        }

        if (damage != null) {
            if (damage.getCause().hasNoAttacker()) {
                sendDeathMessage(p, damage.getCause());
            } else {
                sendKillerMessage(p, damage.getDamager(), damage.getGun(), damage.getCause());
            }
            return;
        }

        if (cause == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION || cause == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) {
            sendDeathMessage(p, new DamageExplainer("generic.explosion"));
            return;
        }

        sendDeathMessage(p, new DamageExplainer("generic.fall"));


    }

    private void sendDeathMessage(LivingEntity dead, DamageExplainer damage) {
        if (dead instanceof Player) {
            EndgameDiamondEvent.transferDiamondRandom();
            EventManager.OnPlayerDeath(null, (Player) dead);
        }
        CoreDamage.clearLastDamage(dead);

        for (Player p : Bukkit.getOnlinePlayers()) {
            String returned = Lang.getRandomValue(p, "deaths." + damage.getName());
            p.sendMessage(returned.replace("{dead}", dead.getName()));
        }
    }

    private void sendKillerMessage(LivingEntity dead, Entity killer, Gun g, DamageExplainer damage) {
        if (killer instanceof Player && dead instanceof Player) {
            addToKills((Player) killer);
            PlayerData.getPlayerData((Player) killer).addXp(50);
            killer.sendMessage(ChatColor.LIGHT_PURPLE + "+50 XP");
            EventManager.OnPlayerDeath((Player) killer, (Player) dead);
        }
        CoreDamage.clearLastDamage(dead);

        for (Player p : Bukkit.getOnlinePlayers()) {
            String returned = Lang.getRandomValue(p, "deaths." + damage.getName());
            if (g != null) {
                g.addToKills();
                returned = returned.replace("{gun}", g.getName());
            }
            p.sendMessage(returned.replace("{dead}", dead.getName()).replace("{killer}", killer.getName()));
        }

    }

    private void addToKills(Player killer) {
        PlayerData data = PlayerData.getPlayerData(killer);
        data.addToKills();
        if (data.getKillStreak() % 5 == 0) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.sendMessage(Lang.KILLCOUNTER.convertRand(p).replace("{amount}", String.valueOf(data.getKillStreak())).replace("{player}", killer.getName()));
            }
        }
    }

}
