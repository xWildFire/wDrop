package pl.wildfire.drop.gui;

import com.google.common.collect.Lists;
import org.bukkit.Material;
import pl.wildfire.api.Msg;
import pl.wildfire.api.gui.GuiItem;
import pl.wildfire.api.gui.GuiMenu;
import pl.wildfire.api.gui.ItemClickEvent;
import pl.wildfire.drop.Config;
import pl.wildfire.drop.DropPlugin;
import pl.wildfire.drop.base.Recipe;

public class CraftingGuiMenu extends GuiMenu {
    public CraftingGuiMenu(final Material material) {
        super(Msg.c(Config.GUI_CRAFTING$NAME.replace("{NAME}", material.name())), Size.fit(DropPlugin.getPlugin().getCraftingManager().getRecipes().size()));
        int pos = 0;
        for (final Recipe drop : DropPlugin.getPlugin().getCraftingManager().getRecipes()) {
            this.setItem(pos, (GuiItem) new GuiItem(drop.getName(), Lists.newArrayList(), drop.getItem()) {
                public void onItemClick(final ItemClickEvent event) {
                }
            });
            ++pos;
        }
    }
}
