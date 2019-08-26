package pl.wildfire.drop.listeners;

import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import pl.wildfire.api.Msg;
import pl.wildfire.api.NumberUtil;
import pl.wildfire.drop.Config;
import pl.wildfire.drop.DropPlugin;
import pl.wildfire.drop.GeneratorTask;
import pl.wildfire.drop.base.DropMask;
import pl.wildfire.drop.base.Item;
import pl.wildfire.drop.base.SkalaDrop;
import pl.wildfire.drop.base.masks.CancelDropMask;
import pl.wildfire.drop.common.DropObject;

public class BlockBreakListener extends DropObject implements Listener {
    public BlockBreakListener(final DropPlugin plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(final BlockBreakEvent event) {
        final Player p = event.getPlayer();
        final Block b = event.getBlock();
        final ItemStack tool = p.getItemInHand();
        if (!Config.GAMEMODE_DROP && p.getGameMode().equals((Object) GameMode.CREATIVE)) {
            return;
        }
        if (b.getType().equals((Object) Material.ENDER_STONE)) {
            b.getWorld().playEffect(b.getLocation(), Effect.POTION_BREAK, 2, 2);
            b.setType(Material.AIR);
            event.setCancelled(true);
            final Block g = b.getRelative(BlockFace.UP);
            g.setType(Material.AIR);
            GeneratorTask.remove(b.getLocation());
            p.getInventory().addItem(new ItemStack[]{Item.getGenerator()});
            return;
        }
        final Block g = b.getRelative(BlockFace.DOWN);
        if (b.getType().equals((Object) Material.STONE) && g.getType().equals((Object) Material.ENDER_STONE)) {
            GeneratorTask.add(b.getLocation());
        }
        DropMask mask = this.getDropManager().getDropMask(b.getType());
        if (this.getDropManager().getDropItems().containsKey(b.getType())) {
            mask = this.getDropManager().getDropItems().get(b.getType());
        }
        if (!mask.equals(CancelDropMask.INSTANCE) && this.getDropManager().getExp().containsKey(b.getType())) {
            int i = this.getDropManager().getExp().get(b.getType());
            if (Config.EVENT_EXP) {
                i += (int) (i * Config.EVENT_MULTIPLIER);
            }
            p.giveExp(i);
        }
        mask.breakBlock(p, tool, b);
        b.setType(Material.AIR);
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlac(final BlockPlaceEvent e) {
        boolean t = false;
        try {
            final String name = e.getItemInHand().getItemMeta().getDisplayName();
            if (name.equalsIgnoreCase(Msg.c("&6Generator Stone"))) {
                final Block b = e.getBlockPlaced();
                b.setData((byte) (-43));
                b.getRelative(BlockFace.UP).setType(Material.STONE);
            } else if (name.equalsIgnoreCase(Msg.c("&9Skala Niebios"))) {
                final Block b = e.getBlockPlaced();
                e.setCancelled(true);
                t = true;
            }
        } catch (NullPointerException ex) {
        }
        if (t) {
            e.getPlayer().setItemInHand(Item.remAmount(e.getItemInHand()));
            e.getPlayer().getInventory().addItem(new ItemStack[]{this.get()});
            e.getPlayer().updateInventory();
        }
    }

    private ItemStack get() {
        for (final SkalaDrop d : SkalaDrop.getDrops()) {
            if (NumberUtil.getChance(d.getPercent())) {
                final ItemStack it = d.getItem().clone();
                it.setAmount(NumberUtil.getRandInt(1, 3));
                return it;
            }
        }
        return this.get();
    }
}
