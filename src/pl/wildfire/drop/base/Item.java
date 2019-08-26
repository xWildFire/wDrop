package pl.wildfire.drop.base;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.wildfire.api.Msg;

public class Item {
    private static final ItemStack generator;
    private static final ItemStack skala;

    static {
        generator = new ItemStack(Material.ENDER_STONE, 1, (short) 213);
        skala = new ItemStack(Material.LAPIS_ORE, 1, (short) 213);
    }

    public Item() {
        ItemMeta im = Item.generator.getItemMeta();
        im.setDisplayName(Msg.c("&6Generator Stone"));
        Item.generator.setItemMeta(im);
        im = Item.skala.getItemMeta();
        im.setDisplayName(Msg.c("&9Skala Niebios"));
        Item.skala.setItemMeta(im);
    }

    public static ItemStack getGenerator() {
        return Item.generator;
    }

    public static ItemStack getSkala() {
        return Item.skala;
    }

    public static boolean equals(ItemStack i1, final ItemStack i2) {
        if (i1 == null || i2 == null) {
            return false;
        }
        i1 = i1.clone();
        i1.setAmount(i2.getAmount());
        return i1.equals((Object) i2);
    }

    public static ItemStack remAmount(ItemStack it) {
        if (it == null) {
            return null;
        }
        if (it.getAmount() - 1 <= 0) {
            it.setType(Material.AIR);
            it = new ItemStack(Material.AIR);
        } else {
            it.setAmount(it.getAmount() - 1);
        }
        return it;
    }
}
