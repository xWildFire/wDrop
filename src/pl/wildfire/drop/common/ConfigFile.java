package pl.wildfire.drop.common;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;

public class ConfigFile extends YamlConfiguration {
    private static final byte[] BUF;

    static {
        BUF = new byte[1024];
    }

    private final File file;

    public ConfigFile(final File file) {
        this(file, null);
    }

    public ConfigFile(final File file, final InputStream in) {
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                if (in != null) {
                    try (final OutputStream out = new FileOutputStream(file)) {
                        int len;
                        while ((len = in.read(ConfigFile.BUF)) > 0) {
                            out.write(ConfigFile.BUF, 0, len);
                        }
                    }
                    in.close();
                } else {
                    file.createNewFile();
                }
            }
            super.load(file);
        } catch (Exception ex) {
        }
        this.file = file;
    }

    public void load(final File file) {
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            super.load(file);
        } catch (Exception ex) {
        }
    }

    public void save(final File file) {
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            super.save(file);
        } catch (IOException ex) {
        }
    }

    public void load() {
        this.load(this.file);
    }

    public void save() {
        this.save(this.file);
    }

    public void reload() {
        this.load();
        this.save();
    }
}
