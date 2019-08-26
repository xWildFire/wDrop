package pl.wildfire.drop.utils;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public final class ItemUtil {
    public static boolean isPickaxe(final ItemStack tool) {
        return tool != null && Enchantment.DIG_SPEED.canEnchantItem(tool);
    }
}
