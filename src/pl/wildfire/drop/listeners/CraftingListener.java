package pl.wildfire.drop.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import pl.wildfire.drop.DropPlugin;
import pl.wildfire.drop.base.Recipe;
import pl.wildfire.drop.common.DropObject;

public class CraftingListener extends DropObject implements Listener {
    public CraftingListener(final DropPlugin plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEvent(final PrepareItemCraftEvent e) {
        final Recipe r = this.getPlugin().getCraftingManager().getRecipe(e.getRecipe().getResult());
        if (r != null) {
            for (int i = 1; i <= 9; ++i) {
                final ItemStack c = (r.getItems().size() - 1 >= i - 1) ? r.getItems().get(i - 1) : null;
                final ItemStack item = e.getInventory().getItem(i);
                if (!this.equals(c, item) || item.getAmount() < c.getAmount()) {
                    e.getInventory().setResult(new ItemStack(Material.AIR, 1));
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onClick(final InventoryClickEvent e) {
        final Player p = (Player) e.getWhoClicked();
        if (e.getInventory() != null && e.getInventory().getType().equals((Object) InventoryType.WORKBENCH) && e.getSlotType().equals((Object) InventoryType.SlotType.RESULT)) {
            final Recipe r = this.getPlugin().getCraftingManager().getRecipe(((CraftingInventory) e.getInventory()).getResult());
            if (r != null) {
                for (int i = 1; i <= 9; ++i) {
                    final ItemStack c = (r.getItems().size() - 1 >= i - 1) ? r.getItems().get(i - 1) : null;
                    final ItemStack item = e.getInventory().getItem(i);
                    if (item != null) {
                        final int x = item.getAmount() - c.getAmount();
                        if (x <= 0) {
                            e.getInventory().setItem(i, new ItemStack(Material.AIR));
                        } else {
                            item.setAmount(x);
                            e.getInventory().setItem(i, item);
                        }
                    }
                }
                e.setCancelled(true);
                e.setCursor(r.getItem());
            }
        }
    }

    private boolean equals(final ItemStack i1, final ItemStack i2) {
        if ((i1 == null && i2 != null) || (i1 != null && i2 == null)) {
            return false;
        }
        final ItemStack j = i1.clone();
        j.setAmount(i2.getAmount());
        return j.equals((Object) i2);
    }
}
