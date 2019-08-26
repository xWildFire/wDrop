package pl.wildfire.drop.base;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SkalaDrop {
    private static final List<SkalaDrop> drops;

    static {
        drops = new ArrayList<SkalaDrop>();
    }

    private final ItemStack item;
    private final double percent;

    public SkalaDrop(final ItemStack i, final double p) {
        this.item = i;
        this.percent = p;
        SkalaDrop.drops.add(this);
    }

    public static List<SkalaDrop> getDrops() {
        return SkalaDrop.drops;
    }

    public ItemStack getItem() {
        return this.item;
    }

    public double getPercent() {
        return this.percent;
    }
}
