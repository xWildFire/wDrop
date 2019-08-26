package pl.wildfire.drop.base;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import pl.wildfire.api.Msg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Recipe {
    private final String name;
    private final ItemStack item;
    private final List<String> shape;
    private final ShapedRecipe rec;
    private final List<ItemStack> items;
    private final HashMap<Character, ItemStack> ingredients;

    public Recipe(final String name, final ItemStack item, final List<String> shape, final HashMap<Character, ItemStack> ingredients) {
        this.name = name;
        this.item = item.clone();
        if (name != null) {
            final ItemMeta im = this.item.getItemMeta();
            im.setDisplayName(Msg.c(name));
            this.item.setItemMeta(im);
        }
        this.shape = shape;
        this.ingredients = ingredients;
        this.items = new ArrayList<ItemStack>();
        int cc = 0;
        for (int x = 1; x <= ((shape.size() > 3) ? 3 : shape.size()); ++x) {
            this.items.add(cc, ingredients.get(shape.get(x - 1).charAt(0)));
            this.items.add(cc + 1, ingredients.get(shape.get(x - 1).charAt(1)));
            this.items.add(cc + 2, ingredients.get(shape.get(x - 1).charAt(2)));
            cc += 3;
        }
        this.rec = new ShapedRecipe(this.item);
        if (shape.size() >= 3) {
            this.rec.shape(new String[]{shape.get(0), shape.get(1), shape.get(2)});
        } else if (shape.size() >= 2) {
            this.rec.shape(new String[]{shape.get(0), shape.get(1)});
        } else if (shape.size() >= 1) {
            this.rec.shape(new String[]{shape.get(0)});
        }
        for (final char c : ingredients.keySet()) {
            this.rec.setIngredient(c, ingredients.get(c).getType());
        }
        Bukkit.addRecipe((org.bukkit.inventory.Recipe) this.rec);
    }

    public String getName() {
        return this.name;
    }

    public ItemStack getItem() {
        return this.item;
    }

    public List<String> getShape() {
        return this.shape;
    }

    public ShapedRecipe getRec() {
        return this.rec;
    }

    public List<ItemStack> getItems() {
        return this.items;
    }

    public HashMap<Character, ItemStack> getIngredients() {
        return this.ingredients;
    }
}
