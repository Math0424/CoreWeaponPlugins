package me.Math0424.SurvivalGuns;

import io.netty.channel.Channel;
import me.Math0424.CoreWeapons.CoreWeaponsAPI;
import me.Math0424.CoreWeapons.NMS.NMSUtil;
import me.Math0424.CoreWeapons.NMS.Packets.Reflection;
import me.Math0424.CoreWeapons.NMS.Packets.TinyProtocol;
import me.Math0424.CoreWeapons.Resourcepack.ResourcepackManager;
import me.Math0424.CoreWeapons.Sound.SoundSystem;
import me.Math0424.CoreWeapons.Sound.Types.EasyRangedSound;
import me.Math0424.CoreWeapons.Sound.Types.SimpleSound;
import me.Math0424.SurvivalGuns.Commands.WitheredSurvivalCommands;
import me.Math0424.SurvivalGuns.CoreWeapons.*;
import me.Math0424.SurvivalGuns.CoreWeapons.Crafting.Craftable;
import me.Math0424.SurvivalGuns.Files.Changeable.Config;
import me.Math0424.SurvivalGuns.Files.FileLoader;
import me.Math0424.SurvivalGuns.Listeners.DeathListener;
import me.Math0424.SurvivalGuns.Listeners.MainListener;
import net.minecraft.network.protocol.game.PacketPlayInAutoRecipe;
import net.minecraft.network.protocol.game.PacketPlayOutAutoRecipe;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.world.item.crafting.ShapedRecipes;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class SurvivalGuns extends JavaPlugin {


    private static SurvivalGuns plugin;

    public static SurvivalGuns getPlugin() { return plugin; }

    public static void log(Level l, String message) {
        Bukkit.getServer().getLogger().log(l, ChatColor.YELLOW + "[SurvivalGuns] " + ChatColor.RESET + message);
    }

    public void onLoad() {
        plugin = this;

        //serializable
        ConfigurationSerialization.registerClass(ComponentSerializable.class);
        ConfigurationSerialization.registerClass(AttachmentSerializable.class);
        ConfigurationSerialization.registerClass(AmmoSerializable.class);
        ConfigurationSerialization.registerClass(GrenadeSerializable.class);
        ConfigurationSerialization.registerClass(GunSerializable.class);

        CoreWeaponsAPI.AddPluginToUpdateChecker("84104", this);
        new FileLoader();
        ResourcepackManager.addPack(Config.RESOURCEPACK.getStrVal(), 10);
    }

    public void onEnable() {

        if (!CoreWeaponsAPI.IsInitialized()) {
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        MainListener manl = new MainListener();
        this.getServer().getPluginManager().registerEvents(manl, this);

        DeathListener deal = new DeathListener();
        this.getServer().getPluginManager().registerEvents(deal, this);

        WitheredSurvivalCommands wsc = new WitheredSurvivalCommands();
        this.getCommand("survivalguns").setExecutor(wsc);
        this.getCommand("survivalguns").setTabCompleter(wsc);

        Craftable.RegisterRecipes();

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (Config.GIVERECIPIES.getBoolVal()) {
                p.discoverRecipes(Craftable.getCraftables().keySet());
            }
        }

        log(Level.INFO, ChatColor.YELLOW + "Survival guns have been enabled");
        log(Level.INFO, ChatColor.YELLOW + "By: Math0424");

        log(Level.INFO, "Loaded " + GunSerializable.getRegistered().size() + " Guns");
        log(Level.INFO, "Loaded " + GrenadeSerializable.getRegistered().size() + " Grenades");
        log(Level.INFO, "Loaded " + ComponentSerializable.getRegistered().size() + " Components");
        log(Level.INFO, "Loaded " + AttachmentSerializable.getRegistered().size() + " Attachments");
        log(Level.INFO, "Loaded " + AmmoSerializable.getRegistered().size() + " Ammo");
        log(Level.INFO, "Loaded " + Craftable.getCraftables().size() + " Recipies");

        setupNonBreakable();
        manageRecipies();
        addSounds();
    }

    /**
     * This is a massive hack to get the recipes to work as expected
     */
    private void manageRecipies() {
        new TinyProtocol(this) {
            final Reflection.FieldAccessor<MinecraftKey> inKey = Reflection.getField(PacketPlayInAutoRecipe.class, MinecraftKey.class, 0);
            final Reflection.FieldAccessor<Integer> inVal = Reflection.getField(PacketPlayInAutoRecipe.class, int.class, 0);

            @Override
            public Object onPacketInAsync(Player sender, Channel channel, Object packet) {
                if (packet instanceof PacketPlayInAutoRecipe e) {
                    String[] key = NMSUtil.NMS().GetKeyDetails(inKey.get(e));
                    Craftable c = Craftable.getCraftable(key[0], key[1]);
                    if (c != null) {

                        Bukkit.getScheduler().runTask(plugin, () -> {
                            var inv = (CraftingInventory)sender.getOpenInventory().getTopInventory();
                            for (ItemStack itemStack : inv.getMatrix())
                                if (itemStack != null)
                                    sender.getInventory().addItem(itemStack);
                            inv.setMatrix(new ItemStack[9]);
                        });

                        if (!canCraftItem(sender, c)) {
                            var rec = new ShapedRecipes(inKey.get(e), "", 0, 0, null, null);
                            var pak = new PacketPlayOutAutoRecipe(inVal.get(packet), rec);
                            sendPacket(sender, pak);
                        } else {
                            Bukkit.getScheduler().runTask(plugin, () -> {
                                ItemStack[] arr = getRecipeArr(c);
                                for (ItemStack itemStack : arr)
                                    if (itemStack != null)
                                        sender.getInventory().removeItem(itemStack);

                                var inv = (CraftingInventory)sender.getOpenInventory().getTopInventory();
                                inv.setMatrix(arr);
                            });
                        }

                        return null;
                    }
                }
                return packet;
            }
        };
    }



    public ItemStack[] getRecipeArr(Craftable c) {
        ItemStack[] arr = new ItemStack[9];
        int curr = 0;
        for(var x : c.getRecipe().getShape()) {
            for(var y : x.split("")) {
                arr[curr] = c.getRecipe().getIngredientMap().get(y.charAt(0));
                curr++;
            }
        }
        return arr;
    }

    private boolean canCraftItem(Player p, Craftable c) {
        Map<Material, Integer> materials = new HashMap<>();

        Map<String, Integer> items = new HashMap<>();
        Map<String, ItemStack> itemDict = new HashMap<>();

        for(var x : c.getRecipe().getChoiceMap().values()) {
            if (x instanceof RecipeChoice.MaterialChoice e) {
                Material m = e.getItemStack().getType();
                if (!materials.containsKey(m))
                    materials.put(m, 0);
                materials.put(m, materials.get(m) + 1);
            } else if(x instanceof RecipeChoice.ExactChoice e) {
                ItemStack m = e.getItemStack();
                String name = m.getItemMeta().hasDisplayName() ? m.getItemMeta().getDisplayName() : m.getType().toString();
                if (!items.containsKey(name)) {
                    items.put(name, 0);
                    itemDict.put(name, m);
                }
                items.put(name, items.get(m) + 1);
            }
        }

        for(Material m : materials.keySet()) {
            if (!p.getInventory().containsAtLeast(new ItemStack(m), materials.get(m))) {
                return false;
            }
        }
        for(String s : items.keySet()) {
            if (!p.getInventory().containsAtLeast(itemDict.get(s), items.get(s))) {
                return false;
            }
        }
        return true;
    }

    private void setupNonBreakable() {
        ArrayList<Material> nonBreakable = new ArrayList<>();
        nonBreakable.add(Material.ENDER_CHEST);
        nonBreakable.add(Material.BEDROCK);
        nonBreakable.add(Material.OBSIDIAN);
        nonBreakable.add(Material.BARRIER);
        nonBreakable.add(Material.END_GATEWAY);
        nonBreakable.add(Material.END_PORTAL_FRAME);
        nonBreakable.add(Material.END_PORTAL);
        nonBreakable.add(Material.COMMAND_BLOCK);
        nonBreakable.add(Material.ANCIENT_DEBRIS);
        nonBreakable.add(Material.REPEATING_COMMAND_BLOCK);
        nonBreakable.add(Material.CHAIN_COMMAND_BLOCK);
        nonBreakable.add(Material.STRUCTURE_BLOCK);
        for (Material m : nonBreakable) {
            CoreWeaponsAPI.getPlugin().addMaterialToUnBreakables(m);
        }
    }

    private void addSounds() {
        //Special
        SoundSystem.registerSound(new SimpleSound("gun_pistol_small_shoot_acid", "acid_gun"));
        SoundSystem.registerSound(new EasyRangedSound("gun_laser_shoot", "laser_gun"));
        SoundSystem.registerSound(new EasyRangedSound("gun_launcher_shoot", "rocket_launcher"));
        SoundSystem.registerSound(new EasyRangedSound("gun_launcher_shoot_nuke", "nuke_gun"));
        SoundSystem.registerSound(new EasyRangedSound("gun_rocket_shoot", "rocket_launcher"));
        SoundSystem.registerSound(new EasyRangedSound("gun_blowdart_shoot", "tracer_gun"));

        //Generic
        SoundSystem.registerSound(new EasyRangedSound("gun_pistol_large_shoot", "large_pistol"));
        SoundSystem.registerSound(new EasyRangedSound("gun_pistol_medium_shoot", "medium_pistol"));
        SoundSystem.registerSound(new EasyRangedSound("gun_pistol_small_shoot", "small_pistol"));

        SoundSystem.registerSound(new EasyRangedSound("gun_automatic_large_shoot", "large_automatic"));
        SoundSystem.registerSound(new EasyRangedSound("gun_automatic_medium_shoot", "medium_automatic"));
        SoundSystem.registerSound(new EasyRangedSound("gun_automatic_small_shoot", "small_automatic"));

        SoundSystem.registerSound(new EasyRangedSound("gun_shotgun_large_shoot", "shotgun"));
        SoundSystem.registerSound(new EasyRangedSound("gun_shotgun_medium_shoot", "shotgun"));

        SoundSystem.registerSound(new EasyRangedSound("gun_sniper_large_shoot", "sniper_gun"));
        SoundSystem.registerSound(new EasyRangedSound("gun_sniper_medium_shoot", "sniper_gun"));
    }

}
