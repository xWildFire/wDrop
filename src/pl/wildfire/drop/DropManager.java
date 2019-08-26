package pl.wildfire.drop;

import com.google.common.collect.Lists;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import pl.wildfire.api.Parser;
import pl.wildfire.drop.base.DropMask;
import pl.wildfire.drop.base.SkalaDrop;
import pl.wildfire.drop.base.masks.*;
import pl.wildfire.drop.common.ConfigFile;
import pl.wildfire.drop.common.Containable;
import pl.wildfire.drop.common.DropObject;

import java.io.File;
import java.util.HashMap;

public class DropManager extends DropObject implements Containable {
    private final ConfigFile config;
    private HashMap<Material, DropMask> dropMasks;
    private HashMap<Material, ItemDropMask> dropItems;
    private HashMap<Material, Integer> exp;

    public DropManager(final DropPlugin plugin) {
        super(plugin);
        this.config = new ConfigFile(new File(plugin.getDataFolder(), "drops.yml"), plugin.getResource("drops.yml"));
        this.setup();
    }

    public DropMask getDropMask(final Material material) {
        DropMask mask = this.dropMasks.get(material);
        if (mask == null) {
            mask = NormalDropMask.INSTANCE;
        }
        return mask;
    }

    public void reloadManager() {
        this.getConfig().reload();
        this.dispose();
        this.setup();
    }

    @Override
    public void setup() {
        new SkalaDrop(new ItemStack(Material.SULPHUR), 30.0);
        new SkalaDrop(new ItemStack(Material.TNT), 30.0);
        new SkalaDrop(new ItemStack(Material.SLIME_BALL), 25.0);
        new SkalaDrop(new ItemStack(Material.IRON_INGOT), 30.0);
        new SkalaDrop(new ItemStack(Material.IRON_INGOT), 30.0);
        new SkalaDrop(new ItemStack(Material.DIAMOND), 30.0);
        new SkalaDrop(new ItemStack(Material.EMERALD), 30.0);
        new SkalaDrop(new ItemStack(Material.GOLD_INGOT), 30.0);
        new SkalaDrop(new ItemStack(Material.ENDER_PEARL), 30.0);
        new SkalaDrop(new ItemStack(Material.GLOWSTONE_DUST), 30.0);
        new SkalaDrop(new ItemStack(Material.APPLE), 20.0);
        new SkalaDrop(new ItemStack(Material.GLOWSTONE), 25.0);
        new SkalaDrop(new ItemStack(Material.MYCEL), 25.0);
        new SkalaDrop(new ItemStack(Material.SPIDER_EYE), 25.0);
        new SkalaDrop(new ItemStack(Material.ARROW), 25.0);
        new SkalaDrop(new ItemStack(Material.LEATHER), 8.0);
        new SkalaDrop(new ItemStack(Material.SUGAR_CANE), 5.0);
        this.dropMasks = new HashMap<Material, DropMask>();
        this.dropItems = new HashMap<Material, ItemDropMask>();
        this.exp = new HashMap<Material, Integer>();
        this.setDefaultDrops();
        for (final String s : this.getConfig().getStringList("drops-cancel")) {
            this.dropMasks.put(Material.matchMaterial(s), CancelDropMask.INSTANCE);
        }
        for (final String s : this.getConfig().getConfigurationSection("exp").getKeys(false)) {
            this.exp.put(Parser.parseMaterial(s), this.getConfig().getInt("exp." + s));
        }
        for (final String s : this.getConfig().getConfigurationSection("drops").getKeys(false)) {
            final Material m = Parser.parseMaterial(s);
            this.dropItems.put(m, new ItemDropMask(this.getPlugin(), m, Lists.newArrayList((Iterable) this.getConfig().getList("drops." + s))));
        }
    }

    private void setDefaultDrops() {
        this.reg(Material.PUMPKIN_STEM, new TypedDropMask(this.plugin, new ItemStack(Material.PUMPKIN_SEEDS)));
        this.reg(Material.MELON_STEM, new TypedDropMask(this.plugin, new ItemStack(Material.MELON_SEEDS)));
        this.reg(Material.SUGAR_CANE_BLOCK, new TypedDropMask(this.plugin, new ItemStack(Material.SUGAR_CANE)));
        this.reg(Material.CARROT, new RequiredDataDropMask(this.plugin, Material.CARROT_ITEM, 1, 3, (byte) 7));
        this.reg(Material.POTATO, new RequiredDataDropMask(this.plugin, Material.POTATO_ITEM, 1, 3, (byte) 7));
    }

    private void reg(final Material material, final DropMask mask) {
        this.dropMasks.put(material, mask);
    }

    @Override
    public void dispose() {
        this.dropMasks.clear();
        this.dropMasks = null;
        this.dropItems.clear();
        this.dropItems = null;
    }

    public ConfigFile getConfig() {
        return this.config;
    }

    public HashMap<Material, DropMask> getDropMasks() {
        return this.dropMasks;
    }

    public HashMap<Material, ItemDropMask> getDropItems() {
        return this.dropItems;
    }

    public HashMap<Material, Integer> getExp() {
        return this.exp;
    }
}
