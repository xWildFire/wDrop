package pl.wildfire.drop.commands;

import com.google.common.collect.Lists;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.wildfire.api.Msg;
import pl.wildfire.api.cmd.Cmd;
import pl.wildfire.drop.Config;
import pl.wildfire.drop.DropPlugin;
import pl.wildfire.drop.base.User;
import pl.wildfire.drop.gui.ItemGuiMenu;

import java.util.Collection;

public class StoneExecutor extends Cmd {
    public StoneExecutor() {
        super("stone", new String[]{"drop", "kamien"});
    }

    public boolean runCommand(final CommandSender sender, final String[] args) {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("reload")) {
                if (!sender.hasPermission("drop.reload")) {
                    return Msg.send(sender, "&4Blad: &cNie posiadasz uprawnien! &7(drop.reload)");
                }
                DropPlugin.getPlugin().getTask().run();
                Config.reloadConfig();
                DropPlugin.getPlugin().getCraftingManager().reloadManager();
                DropPlugin.getPlugin().getDropManager().reloadManager();
                DropPlugin.getPlugin().getDb().reconnect();
                DropPlugin.getPlugin().getUserManager().reloadManager();
                return Msg.send(sender, "&aPlugin zostal przeladowany!");
            } else if (args[0].equalsIgnoreCase("drop")) {
                if (!sender.hasPermission("drop.event")) {
                    return Msg.send(sender, "&4Blad: &cNie posiadasz uprawnien! &7(drop.event)");
                }
                Config.EVENT_DROP = !Config.EVENT_DROP;
                Config.saveConfig();
                return Msg.send(sender, "&6" + (Config.EVENT_DROP ? "Wlaczyles" : "Wylaczyles") + " &7event DROP");
            } else if (args[0].equalsIgnoreCase("exp")) {
                if (!sender.hasPermission("drop.event")) {
                    return Msg.send(sender, "&4Blad: &cNie posiadasz uprawnien! &7(drop.event)");
                }
                Config.EVENT_EXP = !Config.EVENT_EXP;
                Config.saveConfig();
                return Msg.send(sender, "&6" + (Config.EVENT_EXP ? "Wlaczyles" : "Wylaczyles") + " &7event EXP");
            } else if (args[0].equalsIgnoreCase("pkt")) {
                if (!sender.hasPermission("drop.event")) {
                    return Msg.send(sender, "&4Blad: &cNie posiadasz uprawnien! &7(drop.event)");
                }
                Config.EVENT_PKT = !Config.EVENT_PKT;
                Config.saveConfig();
                return Msg.send(sender, "&6" + (Config.EVENT_PKT ? "Wlaczyles" : "Wylaczyles") + " &7event PKT");
            } else if (args[0].equalsIgnoreCase("top")) {
                Msg.send(sender, "&8<&m--------------&r&8> &3Top Kopaczy &8<&m--------------&r&8>");
                final Collection<User> list = (Collection<User>) Lists.newArrayList((Iterable) DropPlugin.getPlugin().getUserManager().getUsers());
                for (int t = 1; t <= 10; ++t) {
                    String str = "&7" + t + ". &c";
                    if (list.size() > 0) {
                        final User u = list.stream().max((entry1, entry2) -> ((entry1.getLvl() == entry2.getLvl()) ? (entry1.getPoints() > entry2.getPoints()) : (entry1.getLvl() > entry2.getLvl())) ? 1 : -1).get();
                        list.remove(u);
                        str = str + u.getName() + " &8[&a" + u.getLvl() + "lvl, " + u.getPoints() + "pkt&8]";
                    } else {
                        str += "Brak";
                    }
                    Msg.send(sender, str);
                }
                Msg.send(sender, "&8<&m--------------&r&8> &3Top Kopaczy &8<&m--------------&r&8>");
                return true;
            }
        }
        if (sender instanceof Player) {
            new ItemGuiMenu().open((Player) sender);
        } else {
            Msg.send(sender, "&4Blad: &cNie mozliwe do wykonania z poziomu konsoli.");
        }
        return true;
    }
}
