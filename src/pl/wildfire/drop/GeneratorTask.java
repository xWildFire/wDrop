package pl.wildfire.drop;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class GeneratorTask extends BukkitRunnable {
    private static Map<Location, Long> locations;

    static {
        GeneratorTask.locations = new HashMap<Location, Long>();
    }

    public static void remove(final Location l) {
        if (GeneratorTask.locations.containsKey(l)) {
            GeneratorTask.locations.remove(l);
        }
    }

    public static void add(final Location l) {
        GeneratorTask.locations.put(l, System.currentTimeMillis() + 1800L);
    }

    private static Map<Location, Long> sort(final Map<Location, Long> unsortMap) {
        final List<Map.Entry<Location, Long>> list = new LinkedList<Map.Entry<Location, Long>>(unsortMap.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<Location, Long>>() {
            @Override
            public int compare(final Map.Entry<Location, Long> o1, final Map.Entry<Location, Long> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });
        final Map<Location, Long> sortedMap = new LinkedHashMap<Location, Long>();
        for (final Map.Entry<Location, Long> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

    public void run() {
        final long cur = System.currentTimeMillis();
        GeneratorTask.locations = sort(GeneratorTask.locations);
        final Iterator<Map.Entry<Location, Long>> it = GeneratorTask.locations.entrySet().iterator();
        while (it.hasNext()) {
            final Map.Entry<Location, Long> n = it.next();
            if (n.getValue() <= cur) {
                n.getKey().getBlock().setType(Material.STONE);
                it.remove();
            }
        }
    }
}
