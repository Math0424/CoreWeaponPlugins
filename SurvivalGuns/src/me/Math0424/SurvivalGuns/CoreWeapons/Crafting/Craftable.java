package me.Math0424.SurvivalGuns.CoreWeapons.Crafting;

import me.Math0424.SurvivalGuns.CoreWeapons.IGivable;
import me.Math0424.SurvivalGuns.SurvivalGuns;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public abstract class Craftable extends IGivable {

    private static final Map<NamespacedKey, Craftable> craftables = new HashMap<>();

    protected String name;
    protected List<String> shape;
    protected Map<Character, String> ingredients;

    private ShapedRecipe recipe;

    public void loadRecipe() {
        if (shape == null || ingredients == null || shape.size() < 3) {
            SurvivalGuns.log(Level.SEVERE, "Unable to load crafting recipe for " + name);
            return;
        }
        if (shape.get(0).length() != 3 || shape.get(1).length() != 3 || shape.get(2).length() != 3) {
            SurvivalGuns.log(Level.SEVERE, "invalid shape for recipe " + name + " '" + shape.get(0) + "' '" + shape.get(1) + "' '" + shape.get(2) + "'");
            return;
        }
        String check = shape.get(0) + shape.get(1) + shape.get(2);
        for(Object c : ingredients.keySet()) {
            check = check.replaceAll(c.toString(), "");
        }
        if (!check.trim().isEmpty()) {
            SurvivalGuns.log(Level.SEVERE, "Symbols '" + check + "' does not appear in craft grid for " + name);
            return;
        }

        NamespacedKey key = new NamespacedKey(SurvivalGuns.getPlugin(), ChatColor.stripColor(name).toLowerCase().replace(' ', '-'));
        recipe = new ShapedRecipe(key, getItem());
        recipe.shape(shape.get(0), shape.get(1), shape.get(2));
        craftables.put(key, this);
    }

    public String getName() {
        return ChatColor.stripColor(name).replace(' ', '_');
    }

    public static void RegisterRecipes() {
        for (NamespacedKey key : craftables.keySet()) {
            Craftable cr = craftables.get(key);

            //Why TF is this a string keyset, Java sucks ass
            for (Object o : cr.ingredients.keySet().toArray()) {

                Character c = ((String)o).charAt(0);
                String ingrName = cr.ingredients.get(c.toString());

                if (Material.getMaterial(ingrName.toUpperCase()) != null) {
                    cr.recipe.setIngredient(c, new RecipeChoice.MaterialChoice(Material.getMaterial(ingrName.toUpperCase())));
                } else {
                    ItemStack myIngredient = null;
                    for (Craftable r : craftables.values()) {
                        if (ChatColor.stripColor(r.name).equalsIgnoreCase(ingrName)) {
                            myIngredient = r.getItem();
                            break;
                        }
                    }
                    if (myIngredient != null) {
                        myIngredient.setAmount(1);
                        try {
                            cr.recipe.setIngredient(c, new RecipeChoice.ExactChoice(myIngredient));
                        } catch (Exception ignored) {
                            SurvivalGuns.log(Level.SEVERE, ChatColor.stripColor(cr.name) + ": symbol does not appear in recipe! '" + c + "'");
                        }
                    } else {
                        SurvivalGuns.log(Level.SEVERE, ChatColor.stripColor(cr.name) + ": unknown ingredient '" + ingrName + "'");
                    }
                }
            }
            try {
                SurvivalGuns.getPlugin().getServer().addRecipe(cr.recipe);
            } catch (Exception e) {
                SurvivalGuns.log(Level.SEVERE, "Duplicate recipe: " + ChatColor.stripColor(cr.name) + "; Recipe will crash.");
            }
        }



        for (NamespacedKey key : craftables.keySet()) {
            Craftable c = craftables.get(key);
            Map<Material, Integer> costs = new HashMap<>();
            try {
                getCost(c.getItem(), costs);
            } catch (Exception e) {
                e.printStackTrace();
                SurvivalGuns.log(Level.SEVERE, "Recipe '" + ChatColor.stripColor(c.name) + "' is unable to be crafted!");
                break;
            }
        }
    }


    private static void getCost(ItemStack item, Map<Material, Integer> map) {
        if (item != null) {
            if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
                for (Craftable r : craftables.values()) {
                    if (ChatColor.stripColor(r.name).equalsIgnoreCase(ChatColor.stripColor(item.getItemMeta().getDisplayName()))) {
                        for (Character c : r.recipe.getIngredientMap().keySet()) {
                            ItemStack i = r.recipe.getIngredientMap().get(c);

                            String s = r.shape.get(0) + r.shape.get(1) + r.shape.get(2);
                            for(int x = 0; x < s.chars().filter(z -> z == c).count(); x++) {
                                getCost(i, map);
                            }
                        }
                        break;
                    }
                }
            } else {
                if (!map.containsKey(item.getType()))
                    map.put(item.getType(), 0);
                map.put(item.getType(), map.get(item.getType()) + 1);
            }
        }
    }


    public ShapedRecipe getRecipe() {
        return recipe;
    }

    public static Map<NamespacedKey, Craftable> getCraftables() {
        return craftables;
    }

    public static Craftable getCraftable(String space, String key) {
        for (var n : craftables.keySet()) {
            if (n.getNamespace().equals(space) && n.getKey().equals(key)) {
                return craftables.get(n);
            }
        }
        return null;
    }

}
