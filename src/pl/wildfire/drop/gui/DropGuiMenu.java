package pl.wildfire.drop.gui;

import org.bukkit.Material;
import pl.wildfire.api.Msg;
import pl.wildfire.api.gui.GuiItem;
import pl.wildfire.api.gui.GuiMenu;
import pl.wildfire.drop.Config;
import pl.wildfire.drop.DropPlugin;
import pl.wildfire.drop.base.Drop;
import pl.wildfire.drop.gui.items.DropGuiItem;

public class DropGuiMenu extends GuiMenu {
    public DropGuiMenu(final Material material) {
        super(Msg.c(Config.GUI_DROP$NAME.replace("{NAME}", material.name())), Size.fit(DropPlugin.getPlugin().getDropManager().getDropItems().get(material).getDrops().size()));
        int pos = 0;
        for (final Drop drop : DropPlugin.getPlugin().getDropManager().getDropItems().get(material).getDrops()) {
            this.setItem(pos, (GuiItem) new DropGuiItem(drop, this));
            ++pos;
        }
    }
}
