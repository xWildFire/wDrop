package pl.wildfire.drop.gui;

import org.bukkit.Material;
import pl.wildfire.api.Msg;
import pl.wildfire.api.gui.GuiItem;
import pl.wildfire.api.gui.GuiMenu;
import pl.wildfire.drop.Config;
import pl.wildfire.drop.DropPlugin;
import pl.wildfire.drop.gui.items.CobbleGuiItem;
import pl.wildfire.drop.gui.items.EventGuiItem;
import pl.wildfire.drop.gui.items.ItemGuiItem;

public class ItemGuiMenu extends GuiMenu {
    public ItemGuiMenu() {
        super(Msg.c(Config.GUI_ITEMS$NAME), Size.fit(DropPlugin.getPlugin().getDropManager().getDropItems().size() + 2));
        int pos = 0;
        for (final Material m : DropPlugin.getPlugin().getDropManager().getDropItems().keySet()) {
            this.setItem(pos, (GuiItem) new ItemGuiItem(m, this));
            ++pos;
        }
        this.setItem(pos + 2, (GuiItem) new CobbleGuiItem(this));
        this.setItem(pos + 1, (GuiItem) new EventGuiItem(this));
    }
}
