package pl.wildfire.drop.common;

import pl.wildfire.drop.DropManager;
import pl.wildfire.drop.DropPlugin;

public class DropObject {
    protected final DropPlugin plugin;

    public DropObject(final DropPlugin plugin) {
        this.plugin = plugin;
    }

    protected final DropManager getDropManager() {
        return this.getPlugin().getDropManager();
    }

    public DropPlugin getPlugin() {
        return this.plugin;
    }
}
