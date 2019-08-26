package pl.wildfire.drop.utils;

import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import pl.wildfire.api.NumberUtil;
import pl.wildfire.drop.Config;
import pl.wildfire.drop.compare.Compare;
import pl.wildfire.drop.compare.Compares;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class DropUtil {
    public static final Compare<Integer> FORTUNE_1_COMPARE;
    public static final Compare<Integer> FORTUNE_2_COMPARE;
    public static final Compare<Integer> FORTUNE_3_COMPARE;
    public static final Compare<Integer> FORTUNE_HIGH_LEVELS_COMPARE;

    static {
        FORTUNE_1_COMPARE = Compares.parseString(Config.FORTUNE_1_AMOUNT);
        FORTUNE_2_COMPARE = Compares.parseString(Config.FORTUNE_2_AMOUNT);
        FORTUNE_3_COMPARE = Compares.parseString(Config.FORTUNE_3_AMOUNT);
        FORTUNE_HIGH_LEVELS_COMPARE = Compares.parseString(Config.FORTUNE_HIGH$LEVELS_AMOUNT);
    }

    public static void recalculateDurability(final Player player, final ItemStack item) {
        if (item.getType().getMaxDurability() == 0) {
            return;
        }
        final int enchantLevel = item.getEnchantmentLevel(Enchantment.DURABILITY);
        final short d = item.getDurability();
        if (enchantLevel > 0) {
            if (100 / (enchantLevel + 1) > NumberUtil.getRandInt(0, 100)) {
                if (d == item.getType().getMaxDurability()) {
                    player.getInventory().clear(player.getInventory().getHeldItemSlot());
                    player.playSound(player.getLocation(), Sound.ITEM_BREAK, 1.0f, 1.0f);
                } else {
                    item.setDurability((short) (d + 1));
                }
            }
        } else if (d == item.getType().getMaxDurability()) {
            player.getInventory().clear(player.getInventory().getHeldItemSlot());
            player.playSound(player.getLocation(), Sound.ITEM_BREAK, 1.0f, 1.0f);
        } else {
            item.setDurability((short) (d + 1));
        }
    }

    public static int addFortuneEnchant(final ItemStack tool) {
        switch (tool.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS)) {
            case 1: {
                if (NumberUtil.getChance(Config.FORTUNE_1_PERCENT)) {
                    return Compares.getRandomValue(DropUtil.FORTUNE_1_COMPARE);
                }
            }
            case 2: {
                if (NumberUtil.getChance(Config.FORTUNE_2_PERCENT)) {
                    return Compares.getRandomValue(DropUtil.FORTUNE_2_COMPARE);
                }
            }
            case 3: {
                if (NumberUtil.getChance(Config.FORTUNE_3_PERCENT)) {
                    return Compares.getRandomValue(DropUtil.FORTUNE_3_COMPARE);
                }
                break;
            }
        }
        if (NumberUtil.getChance(Config.FORTUNE_HIGH$LEVELS_PERCENT)) {
            return Compares.getRandomValue(DropUtil.FORTUNE_HIGH_LEVELS_COMPARE);
        }
        return 0;
    }

    public static void addItemsToPlayer(final Player player, final List<ItemStack> items, final Block b) {
        final PlayerInventory inv = player.getInventory();
        final HashMap<Integer, ItemStack> notStored = (HashMap<Integer, ItemStack>) inv.addItem((ItemStack[]) items.toArray(new ItemStack[items.size()]));
        for (final Map.Entry<Integer, ItemStack> en : notStored.entrySet()) {
            b.getWorld().dropItemNaturally(b.getLocation(), (ItemStack) en.getValue());
        }
    }
}
