package pl.wildfire.drop.base;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.wildfire.drop.DropPlugin;
import pl.wildfire.drop.common.DropObject;

public abstract class DropMask extends DropObject {
    public DropMask(final DropPlugin plugin) {
        super(plugin);
    }

    public abstract void breakBlock(final Player p0, final ItemStack p1, final Block p2);
}
