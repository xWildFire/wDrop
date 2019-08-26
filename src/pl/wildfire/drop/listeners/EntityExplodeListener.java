package pl.wildfire.drop.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;
import pl.wildfire.drop.DropPlugin;
import pl.wildfire.drop.base.DropMask;
import pl.wildfire.drop.base.masks.CancelDropMask;
import pl.wildfire.drop.common.DropObject;

import java.util.Iterator;

public class EntityExplodeListener extends DropObject implements Listener {
    public EntityExplodeListener(final DropPlugin plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityExplode(final EntityExplodeEvent event) {
        final Iterator<Block> blocks = event.blockList().iterator();
        Block b = null;
        while (blocks.hasNext()) {
            b = blocks.next();
            final DropMask mask = this.getDropManager().getDropMask(b.getType());
            if (mask instanceof CancelDropMask) {
                b.setType(Material.AIR);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDie(final EntityDeathEvent e) {
        if (e.getEntity().getType().equals((Object) EntityType.CREEPER)) {
            e.getDrops().add(new ItemStack(Material.SULPHUR, 4));
        }
    }
}
