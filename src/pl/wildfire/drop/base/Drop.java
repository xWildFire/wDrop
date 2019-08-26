package pl.wildfire.drop.base;

import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.wildfire.api.Parser;
import pl.wildfire.drop.Config;
import pl.wildfire.drop.compare.Compare;
import pl.wildfire.drop.compare.Compares;
import pl.wildfire.drop.utils.ItemUtil;

import java.util.*;

@SerializableAs("Drop")
public class Drop implements ConfigurationSerializable {
    private final String name;
    private final String permission;
    private final ItemStack item;
    private final String message;
    private final double chance;
    private final int exp;
    private final boolean fortune;
    private final boolean canDisable;
    private final Compare<Integer> height;
    private final Compare<Integer> amount;
    private final Compare<Integer> points;
    private final List<Material> tools;
    private final Set<String> disabled;
    private final Set<String> disabledMsg;

    public Drop(final String name, final String permission, final ItemStack item, final String message, final double chance, final int exp, final boolean fortune, final boolean canDisable, final Compare<Integer> height, final Compare<Integer> amount, final Compare<Integer> points, final List<Material> tools) {
        this.tools = new ArrayList<Material>();
        this.disabled = new HashSet<String>();
        this.disabledMsg = new HashSet<String>();
        this.name = name;
        this.permission = permission;
        this.item = item;
        this.message = message;
        this.chance = chance;
        this.exp = exp;
        this.fortune = fortune;
        this.canDisable = canDisable;
        this.height = height;
        this.amount = amount;
        this.points = points;
        this.tools.addAll(tools);
    }

    public static Drop deserialize(final Map<String, Object> map) {
        String name = null;
        String permission = null;
        ItemStack item = null;
        String message = null;
        double chance = 0.0;
        int exp = 0;
        boolean fortune = false;
        boolean canDisable = true;
        Compare<Integer> height = null;
        Compare<Integer> amount = null;
        Compare<Integer> points = null;
        final List<Material> tools = new ArrayList<Material>();
        if (map.containsKey("name")) {
            name = (String) map.get("name");
        }
        if (name == null) {
            throw new IllegalArgumentException("Name can not be null!");
        }
        if (map.containsKey("permission")) {
            permission = (String) map.get("permission");
        }
        if (map.containsKey("item")) {
            item = Parser.parseItem((String) map.get("item"));
        }
        if (item == null) {
            throw new IllegalArgumentException("Item can not be null!");
        }
        if (map.containsKey("message")) {
            message = (String) map.get("message");
        }
        if (map.containsKey("chance")) {
            try {
                chance = (double) map.get("chance");
            } catch (ClassCastException ee) {
                chance = (int) map.get("chance");
            }
        }
        if (map.containsKey("exp")) {
            exp = (int) map.get("exp");
        }
        if (map.containsKey("fortune")) {
            fortune = (boolean) map.get("fortune");
        }
        if (map.containsKey("can-disable")) {
            canDisable = (boolean) map.get("can-disable");
        }
        if (map.containsKey("height")) {
            height = Compares.parseString((String) map.get("height"));
        }
        if (map.containsKey("amount")) {
            amount = Compares.parseString((String) map.get("amount"));
        }
        if (map.containsKey("points")) {
            points = Compares.parseString((String) map.get("points"));
        }
        if (map.containsKey("tools")) {
            tools.addAll(Parser.parseMaterials((List) map.get("tools")));
        }
        return new Drop(name, permission, item, message, chance, exp, fortune, canDisable, height, amount, points, tools);
    }

    public double getChance(final Player viewer) {
        double chance = this.getChance();
        if (Config.VIP$DROP_ENABLED & viewer.hasPermission(Config.VIP$DROP_PERMISSION)) {
            chance += chance * Config.VIP$DROP_MULTIPLIER;
        }
        if (Config.EVENT_DROP) {
            chance += chance * Config.EVENT_MULTIPLIER;
        }
        return chance;
    }

    public boolean canDrop(final Player viewer) {
        return this.permission == null || viewer.hasPermission(this.permission);
    }

    public boolean isDisabled(final String string) {
        return this.canDisable && this.disabled.contains(string);
    }

    public void changeStatus(final String string) {
        if (!this.canDisable) {
            return;
        }
        if (this.isDisabled(string)) {
            this.disabled.remove(string);
        } else {
            this.disabled.add(string);
        }
    }

    public boolean isDisabledMsg(final String string) {
        return this.message != null && this.disabledMsg.contains(string);
    }

    public void changeStatusMsg(final String string) {
        if (this.message == null || !this.canDisable) {
            return;
        }
        if (this.isDisabledMsg(string)) {
            this.disabledMsg.remove(string);
        } else {
            this.disabledMsg.add(string);
        }
    }

    public boolean enoughHeight(final int height) {
        return this.height == null || this.height.isInRange(height);
    }

    public boolean enoughPickaxe(final ItemStack item) {
        return ItemUtil.isPickaxe(item) && (this.tools.size() < 1 || this.tools.contains(item.getType()));
    }

    public int getRandomAmount() {
        if (this.amount == null) {
            return 1;
        }
        return Compares.getRandomValue(this.amount);
    }

    public int getRandomPoints() {
        if (this.points == null) {
            return 0;
        }
        int i = Compares.getRandomValue(this.points);
        if (Config.EVENT_PKT) {
            i += (int) (i * Config.EVENT_MULTIPLIER);
        }
        return i;
    }

    public Map<String, Object> serialize() {
        final Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("name", this.name);
        if (this.permission != null) {
            map.put("permission", this.permission);
        }
        map.put("item", Parser.toString(this.item));
        if (this.message != null && this.message.length() > 0) {
            map.put("message", this.message);
        }
        map.put("chance", this.chance);
        if (this.exp > 0) {
            map.put("exp", this.exp);
        }
        map.put("fortune", this.fortune);
        map.put("can-disable", this.canDisable);
        if (this.height != null) {
            map.put("height", this.height.getParse());
        }
        if (this.amount != null) {
            map.put("amount", this.amount.getParse());
        }
        if (this.points != null) {
            map.put("points", this.points.getParse());
        }
        final List<String> tls = new ArrayList<String>();
        for (final Material it : this.tools) {
            tls.add(it.name());
        }
        map.put("tools", tls);
        return map;
    }

    public String getName() {
        return this.name;
    }

    public String getPermission() {
        return this.permission;
    }

    public ItemStack getItem() {
        return this.item;
    }

    public String getMessage() {
        return this.message;
    }

    public double getChance() {
        return this.chance;
    }

    public int getExp() {
        return this.exp;
    }

    public boolean isFortune() {
        return this.fortune;
    }

    public boolean isCanDisable() {
        return this.canDisable;
    }

    public Compare<Integer> getHeight() {
        return this.height;
    }

    public Compare<Integer> getAmount() {
        return this.amount;
    }

    public Compare<Integer> getPoints() {
        return this.points;
    }

    public List<Material> getTools() {
        return this.tools;
    }

    public Set<String> getDisabled() {
        return this.disabled;
    }

    public Set<String> getDisabledMsg() {
        return this.disabledMsg;
    }
}
