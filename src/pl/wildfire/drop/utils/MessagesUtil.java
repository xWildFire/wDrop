package pl.wildfire.drop.utils;

import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import pl.wildfire.api.Msg;
import pl.wildfire.drop.Config;
import pl.wildfire.drop.DropPlugin;
import pl.wildfire.drop.base.Drop;

import java.util.Collection;

public final class MessagesUtil {
    public static String replace(String source, final Drop drop) {
        source = source.replace("{NAME}", drop.getName());
        source = source.replace("{ITEM}", drop.getItem().getType().name());
        source = source.replace("{MESSAGE}", (drop.getMessage() != null && drop.getMessage().length() > 0) ? drop.getMessage() : "");
        source = source.replace("{CHANCE}", Double.toString(drop.getChance()));
        source = source.replace("{EXP}", Integer.toString(drop.getExp()));
        source = source.replace("{FORTUNE}", drop.isFortune() ? "tak" : "nie");
        source = source.replace("{CAN_DISABLE}", drop.isCanDisable() ? "tak" : "nie");
        source = source.replace("{HEIGHT}", (drop.getHeight() != null) ? drop.getHeight().getParse() : "wszedzie");
        source = source.replace("{AMOUNT}", (drop.getAmount() != null) ? drop.getAmount().getParse() : "1");
        source = source.replace("{POINTS}", (drop.getPoints() != null) ? drop.getPoints().getParse() : "0");
        source = source.replace("{TOOLS}", (drop.getTools().size() == 0) ? "wszystkim" : StringUtils.join((Collection) drop.getTools(), ", "));
        return Msg.c(source);
    }

    public static String replace(String source, final Drop drop, final Player player) {
        source = source.replace("{ACTIVE}", drop.isDisabled(player.getName()) ? "nie" : "tak");
        source = source.replace("{CHANCE}", String.format("%1$.2f", drop.getChance(player)));
        source = source.replace("{CAN_DROP}", drop.canDrop(player) ? "tak" : "nie");
        source = source.replace("{ACTIVE_MSG}", drop.isDisabledMsg(player.getName()) ? "nie" : "tak");
        source = replace(source, drop);
        return source;
    }

    public static String replace(String source, final Player player) {
        source = source.replace("{ACTIVE}", DropPlugin.getPlugin().getUserManager().getUser(player).isCobble() ? "tak" : "nie");
        return source;
    }

    public static String replaceEvent(String source) {
        source = source.replace("{EVENT_DROP}", Config.EVENT_DROP ? "tak" : "nie");
        source = source.replace("{EVENT_EXP}", Config.EVENT_EXP ? "tak" : "nie");
        source = source.replace("{EVENT_PKT}", Config.EVENT_PKT ? "tak" : "nie");
        return source;
    }
}
