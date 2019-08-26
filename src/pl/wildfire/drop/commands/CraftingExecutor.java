package pl.wildfire.drop.commands;

import org.bukkit.entity.Player;
import pl.wildfire.api.Msg;
import pl.wildfire.api.cmd.Cmd;
import pl.wildfire.api.gui.GuiMenu;
import pl.wildfire.drop.DropPlugin;

public class CraftingExecutor extends Cmd {
    public CraftingExecutor() {
        super("crafting", new String[]{"craft", "workbench"});
    }

    public boolean runCommand(final Player player, final String[] args) {
        final GuiMenu[] invs = DropPlugin.getPlugin().getCraftingManager().getInv();
        if (invs.length >= 1) {
            invs[0].open(player);
        } else {
            Msg.send(player, "&4Blad: &cBrak nowych craftingow na serwerze.");
        }
        return true;
    }
}
