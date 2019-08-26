package pl.wildfire.drop;

import org.bukkit.configuration.file.FileConfiguration;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public final class Config {
    private static final String pathPrefix = "config.";
    public static String GUI_ITEMS$NAME;
    public static String GUI_DROP$NAME;
    public static String GUI_CRAFTING$NAME;
    public static List<String> GUI_ICON_LORE;
    public static List<String> GUI_COBBLE_LORE;
    public static List<String> GUI_EVENT_LORE;
    public static boolean GUI_CLOSE$AFTER$CLICK;
    public static boolean GAMEMODE_DROP;
    public static int LVLUP$BROAD_EVERY;
    public static String MESSAGES_CANCELDROP;
    public static String MESSAGES_LVLUP_NORMAL;
    public static String MESSAGES_LVLUP_BROAD;
    public static double EVENT_MULTIPLIER;
    public static boolean EVENT_DROP;
    public static boolean EVENT_EXP;
    public static boolean EVENT_PKT;
    public static boolean VIP$DROP_ENABLED;
    public static String VIP$DROP_PERMISSION;
    public static double VIP$DROP_MULTIPLIER;
    public static int STONE$EXP;
    public static double FORTUNE_1_PERCENT;
    public static String FORTUNE_1_AMOUNT;
    public static double FORTUNE_2_PERCENT;
    public static String FORTUNE_2_AMOUNT;
    public static double FORTUNE_3_PERCENT;
    public static String FORTUNE_3_AMOUNT;
    public static double FORTUNE_HIGH$LEVELS_PERCENT;
    public static String FORTUNE_HIGH$LEVELS_AMOUNT;
    public static boolean MYSQL_USE;
    public static String MYSQL_HOST;
    public static int MYSQL_PORT;
    public static String MYSQL_USER;
    public static String MYSQL_PASS;
    public static String MYSQL_NAME;

    static {
        Config.GUI_ITEMS$NAME = "&4&lDROP";
        Config.GUI_DROP$NAME = "&4&lDROP Z {NAME}";
        Config.GUI_CRAFTING$NAME = "&4&lRECEPTURY {PAGE}";
        Config.GUI_ICON_LORE = Arrays.asList("&8> &amozesz wydropic: &7{CAN_DROP}", "&8> &aszansa: &7{CHANCE}", "&8> &adoswiadczenie: &7{EXP}", "&8> &azaklecie fortune: &7{FORTUNE}", "&8> &awypada na poziomie: &7{HEIGHT}", "&8> &awypada w ilosci: &7{AMOUNT}", "&8> &amozliwosc wylaczenia: &7{CAN_DISABLE}", "&8> &awydobyc mozna: &7{TOOLS}", "&8> &aaktywny: &7{ACTIVE}");
        Config.GUI_COBBLE_LORE = Arrays.asList("&8> &aaktywny: &7{ACTIVE}");
        Config.GUI_EVENT_LORE = Arrays.asList("&8> &aDrop: &7{EVENT_DROP}", "&8> &aExp: &7{EVENT_EXP}", "&8> &aPunkty: &7{EVENT_PKT}");
        Config.GUI_CLOSE$AFTER$CLICK = false;
        Config.GAMEMODE_DROP = false;
        Config.LVLUP$BROAD_EVERY = 1;
        Config.MESSAGES_CANCELDROP = "&7Drop z tego bloku jest wylaczony! :(";
        Config.MESSAGES_LVLUP_NORMAL = "&7Awansowales na &3{LVL} &7poziom kopania.";
        Config.MESSAGES_LVLUP_BROAD = "&7Gracz &3{PLAYER} &7awansowal na &3{LVL} &7poziom kopania.";
        Config.EVENT_MULTIPLIER = 0.5;
        Config.EVENT_DROP = false;
        Config.EVENT_EXP = false;
        Config.EVENT_PKT = false;
        Config.VIP$DROP_ENABLED = true;
        Config.VIP$DROP_PERMISSION = "karolekdrop.vip";
        Config.VIP$DROP_MULTIPLIER = 0.5;
        Config.STONE$EXP = 10;
        Config.FORTUNE_1_PERCENT = 30.0;
        Config.FORTUNE_1_AMOUNT = "1-2";
        Config.FORTUNE_2_PERCENT = 20.0;
        Config.FORTUNE_2_AMOUNT = "1-3";
        Config.FORTUNE_3_PERCENT = 10.0;
        Config.FORTUNE_3_AMOUNT = "1-4";
        Config.FORTUNE_HIGH$LEVELS_PERCENT = 5.0;
        Config.FORTUNE_HIGH$LEVELS_AMOUNT = "1-10";
        Config.MYSQL_USE = false;
        Config.MYSQL_HOST = "localhost";
        Config.MYSQL_PORT = 3306;
        Config.MYSQL_USER = "root";
        Config.MYSQL_PASS = "password";
        Config.MYSQL_NAME = "database";
    }

    public static void loadConfig() {
        try {
            DropPlugin.getPlugin().saveDefaultConfig();
            final FileConfiguration f = DropPlugin.getPlugin().getConfig();
            for (final Field field : Config.class.getFields()) {
                if (Modifier.isStatic(field.getModifiers()) && Modifier.isPublic(field.getModifiers())) {
                    final String path = "config." + field.getName().toLowerCase().replace("$", "-").replace("_", ".");
                    if (f.isSet(path)) {
                        field.set(null, f.get(path));
                    }
                }
            }
        } catch (Exception e) {
            DropPlugin.getPlugin().getLogger().log(Level.WARNING, "An error occured while loading config!", e);
        }
    }

    public static void saveConfig() {
        try {
            DropPlugin.getPlugin().saveDefaultConfig();
            final FileConfiguration f = DropPlugin.getPlugin().getConfig();
            for (final Field field : Config.class.getFields()) {
                if (Modifier.isStatic(field.getModifiers()) && Modifier.isPublic(field.getModifiers())) {
                    final String path = "config." + field.getName().toLowerCase().replace("$", "-").replace("_", ".");
                    f.set(path, field.get(null));
                }
            }
            DropPlugin.getPlugin().saveConfig();
        } catch (Exception e) {
            DropPlugin.getPlugin().getLogger().log(Level.WARNING, "An error occured while saving config!", e);
        }
    }

    public static void reloadConfig() {
        DropPlugin.getPlugin().reloadConfig();
        loadConfig();
        saveConfig();
    }
}
