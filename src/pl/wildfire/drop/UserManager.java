package pl.wildfire.drop;

import org.bukkit.entity.Player;
import pl.wildfire.drop.base.User;
import pl.wildfire.drop.common.Containable;
import pl.wildfire.drop.common.DropObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class UserManager extends DropObject implements Containable {
    private Map<String, User> users;

    public UserManager(final DropPlugin plugin) {
        super(plugin);
        this.setup();
    }

    public User getUser(final Player player) {
        final String name = player.getName().toLowerCase();
        if (this.users.containsKey(name)) {
            return this.users.get(name);
        }
        final User u = new User(player.getName(), 0, 0L);
        this.users.put(player.getName().toLowerCase(), u);
        return u;
    }

    public Collection<User> getUsers() {
        return this.users.values();
    }

    public User getUser(final String name) {
        if (this.users.containsKey(name.toLowerCase())) {
            return this.users.get(name.toLowerCase());
        }
        final User u = new User(name, 0, 0L);
        this.users.put(name.toLowerCase(), u);
        return u;
    }

    @Override
    public void setup() {
        this.users = new HashMap<String, User>();
        final ResultSet r = this.getPlugin().getDb().query("SELECT * FROM `GraczeDr`");
        try {
            while (r.next()) {
                this.users.put(r.getString("Gracz").toLowerCase(), new User(r.getString("Gracz"), r.getInt("Lvl"), r.getLong("Pkt")));
            }
        } catch (SQLException e) {
            DropPlugin.getPlugin().getLogger().log(Level.WARNING, "An error occured while loading players!", e);
        }
    }

    @Override
    public void dispose() {
        this.users.clear();
        this.users = null;
    }

    public void reloadManager() {
        this.dispose();
        this.setup();
    }
}
