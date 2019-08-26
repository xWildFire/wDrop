package pl.wildfire.drop;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import pl.wildfire.drop.base.User;

import java.util.ArrayList;
import java.util.List;

public class SaveTask extends BukkitRunnable {
    private final DropPlugin plugin;
    private List<User> users;

    public SaveTask(final DropPlugin plugin) {
        this.users = new ArrayList<User>();
        super.runTaskTimerAsynchronously((Plugin) (this.plugin = plugin), 4000L, 4000L);
    }

    public void addChange(final User u) {
        if (!this.users.contains(u)) {
            this.users.add(u);
        }
    }

    public void run() {
        if (this.users.size() <= 0) {
            return;
        }
        String q = "";
        for (final User u : this.users) {
            if (!q.equals("")) {
                q += ",";
            }
            q = q + "('" + u.getName() + "','" + u.getLvl() + "','" + u.getPoints() + "')";
        }
        this.plugin.getDb().update("INSERT INTO `GraczeDr` (`Gracz`,`Lvl`,`Pkt`) VALUES " + q + " ON DUPLICATE KEY UPDATE `Lvl`=VALUES(`Lvl`),`Pkt`=VALUES(`Pkt`);");
        this.users.clear();
    }
}
