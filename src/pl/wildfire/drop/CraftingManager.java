package pl.wildfire.drop;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import pl.wildfire.api.Msg;
import pl.wildfire.api.Parser;
import pl.wildfire.api.gui.GuiItem;
import pl.wildfire.api.gui.GuiMenu;
import pl.wildfire.api.gui.ItemClickEvent;
import pl.wildfire.drop.base.Recipe;
import pl.wildfire.drop.common.ConfigFile;
import pl.wildfire.drop.common.Containable;
import pl.wildfire.drop.common.DropObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CraftingManager extends DropObject implements Containable {
    private final ConfigFile config;
    private List<Recipe> recipes;
    private HashMap<ItemStack, Recipe> modifies;
    private GuiMenu[] inv;
    private List<List<Integer>> list;

    public CraftingManager(final DropPlugin plugin) {
        super(plugin);
        (this.list = new ArrayList<List<Integer>>()).add(Arrays.asList(0, 1, 2, 9, 10, 11, 18, 19, 20));
        this.list.add(Arrays.asList(30, 31, 32, 39, 40, 41, 48, 49, 50));
        this.list.add(Arrays.asList(6, 7, 8, 15, 16, 17, 24, 25, 26));
        this.config = new ConfigFile(new File(plugin.getDataFolder(), "recipes.yml"), plugin.getResource("recipes.yml"));
        this.setup();
    }

    public void reloadManager() {
        this.getConfig().reload();
        this.dispose();
        this.setup();
    }

    public Recipe getRecipe(final ItemStack r) {
        if (this.modifies.containsKey(r)) {
            return this.modifies.get(r);
        }
        return null;
    }

    @Override
    public void setup() {
        this.recipes = new ArrayList<Recipe>();
        this.modifies = new HashMap<ItemStack, Recipe>();
        final ConfigurationSection shaped = this.getConfig().getConfigurationSection("shaped");
        for (final String s : shaped.getKeys(false)) {
            final ConfigurationSection sec = shaped.getConfigurationSection(s);
            final HashMap<Character, ItemStack> ingredients = new HashMap<Character, ItemStack>();
            boolean modify = false;
            final ConfigurationSection cc = sec.getConfigurationSection("ingredients");
            for (final String c : cc.getKeys(false)) {
                final ItemStack it = Parser.parseItem(cc.getString(c));
                ingredients.put(c.toCharArray()[0], it);
                if (it.getAmount() > 1) {
                    modify = true;
                }
            }
            final Recipe r = new Recipe(sec.getString("name"), Parser.parseItem(sec.getString("item")), sec.getStringList("shape"), ingredients);
            this.recipes.add(r);
            if (modify) {
                this.modifies.put(r.getItem(), r);
            }
        }
        this.inv = new GuiMenu[this.getRecipes().size()];
        int r2 = 0;
        int last = 0;
        for (int i = 0; i < this.getRecipes().size() / 3.0; ++i) {
            this.inv[i] = new GuiMenu(Config.GUI_CRAFTING$NAME.replace("{PAGE}", Integer.toString(i)), GuiMenu.Size.fit(54));
            last = i;
            for (int h = 0; h <= 2 && this.getRecipes().size() >= r2 + 1; ++r2, ++h) {
                final Recipe rec = this.getRecipes().get(r2);
                int g = 0;
                for (final String s2 : rec.getShape()) {
                    for (final char ch : s2.toCharArray()) {
                        final GuiItem gi = this.getIngredient(ch, rec.getIngredients());
                        if (gi != null) {
                            this.inv[i].setItem((int) this.list.get(h).get(g), gi);
                        }
                        ++g;
                    }
                }
                this.inv[i].setItem((h == 1) ? 13 : (37 + h * 3), new GuiItem("", rec.getItem(), new String[0]));
                this.inv[i].setItem(45, (GuiItem) new GuiItem("&cPoprzednia Strona", new ItemStack(Material.WOOL, 1, (short) 14), new String[0]) {
                    public void onItemClick(final ItemClickEvent event) {
                        CraftingManager.this.inv[Integer.parseInt(event.getGuiMenu().getName().replace(Msg.c("&4&LRECEPTURY CRAFTINGU-"), "")) - 1].open(event.getPlayer());
                    }
                });
                this.inv[i].setItem(53, (GuiItem) new GuiItem("&6Nastepna Strona", new ItemStack(Material.WOOL, 1, (short) 5), new String[0]) {
                    public void onItemClick(final ItemClickEvent event) {
                        CraftingManager.this.inv[Integer.parseInt(event.getGuiMenu().getName().replace(Msg.c("&4&LRECEPTURY CRAFTINGU-"), "")) + 1].open(event.getPlayer());
                    }
                });
            }
        }
        this.inv[0].setItem(45, new GuiItem("", new ItemStack(Material.AIR), new String[0]));
        this.inv[last].setItem(53, new GuiItem("", new ItemStack(Material.AIR), new String[0]));
    }

    @Override
    public void dispose() {
        this.recipes.clear();
        this.recipes = null;
        this.modifies.clear();
        this.modifies = null;
        this.inv = null;
    }

    private GuiItem getIngredient(final char ch, final HashMap<Character, ItemStack> ingr) {
        if (ingr.containsKey(ch)) {
            final ItemStack m = ingr.get(ch);
            if (m != null) {
                return new GuiItem("", m, new String[0]);
            }
        }
        return null;
    }

    public ConfigFile getConfig() {
        return this.config;
    }

    public List<Recipe> getRecipes() {
        return this.recipes;
    }

    public HashMap<ItemStack, Recipe> getModifies() {
        return this.modifies;
    }

    public GuiMenu[] getInv() {
        return this.inv;
    }

    public List<List<Integer>> getList() {
        return this.list;
    }
}
