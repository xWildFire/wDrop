package pl.wildfire.drop;

import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import pl.wildfire.drop.base.Drop;
import pl.wildfire.drop.base.Item;
import pl.wildfire.drop.commands.CraftingExecutor;
import pl.wildfire.drop.commands.StoneExecutor;
import pl.wildfire.drop.common.Containable;
import pl.wildfire.drop.db.Db;
import pl.wildfire.drop.db.MySQL;
import pl.wildfire.drop.db.SQLite;
import pl.wildfire.drop.listeners.BlockBreakListener;
import pl.wildfire.drop.listeners.CraftingListener;
import pl.wildfire.drop.listeners.EntityExplodeListener;

import java.io.File;
import java.util.logging.Level;

public class DropPlugin extends JavaPlugin implements Containable {
    private static DropPlugin plugin;

    static {
        ConfigurationSerialization.registerClass((Class) Drop.class, "Drop");
    }

    private DropManager dropManager;
    private UserManager userManager;
    private CraftingManager craftingManager;
    private Db db;
    private SaveTask task;

    public static DropPlugin getPlugin() {
        return DropPlugin.plugin;
    }

    public void onLoad() {
        DropPlugin.plugin = this;
    }

    public void onEnable() {
        this.setup();
    }

    public void onDisable() {
        this.dispose();
        DropPlugin.plugin = null;
    }

    public void setup() {
        this.saveDefaultConfig();
        Config.reloadConfig();
        if (Config.MYSQL_USE) {
            this.db = new MySQL(Config.MYSQL_HOST, Config.MYSQL_PORT, Config.MYSQL_USER, Config.MYSQL_PASS, Config.MYSQL_NAME);
        } else {
            this.db = new SQLite(new File(this.getDataFolder(), "SQLite.db"));
        }
        if (!this.db.connect()) {
            this.getServer().getPluginManager().disablePlugin((Plugin) this);
            return;
        }
        this.getLogger().log(Level.INFO, "Connected to the " + this.db.getClass().getSimpleName() + " database.");
        this.db.updateNow("CREATE TABLE IF NOT EXISTS `GraczeDr` (`Gracz` VARCHAR(16), `Lvl` INT, `Pkt` BIGINT, PRIMARY KEY (`Gracz`))");
        new Item();
        this.dropManager = new DropManager(this);
        this.userManager = new UserManager(this);
        this.craftingManager = new CraftingManager(this);
        final PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents((Listener) new BlockBreakListener(this), (Plugin) this);
        pm.registerEvents((Listener) new EntityExplodeListener(this), (Plugin) this);
        pm.registerEvents((Listener) new CraftingListener(this), (Plugin) this);
        this.task = new SaveTask(this);
        new GeneratorTask().runTaskTimer((Plugin) this, 20L, 20L);
        new StoneExecutor();
        new CraftingExecutor();
    }

    public void dispose() {
        this.dropManager.dispose();
        this.dropManager = null;
        this.userManager.dispose();
        this.userManager = null;
        this.craftingManager.dispose();
        this.craftingManager = null;
        this.task.run();
        this.task.cancel();
        this.task = null;
        this.db.disconnect();
        this.db = null;
    }

    public DropManager getDropManager() {
        return this.dropManager;
    }

    public UserManager getUserManager() {
        return this.userManager;
    }

    public CraftingManager getCraftingManager() {
        return this.craftingManager;
    }

    public Db getDb() {
        return this.db;
    }

    public SaveTask getTask() {
        return this.task;
    }
}
