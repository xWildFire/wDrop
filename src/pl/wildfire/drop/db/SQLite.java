package pl.wildfire.drop.db;

import pl.wildfire.drop.DropPlugin;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Level;

public class SQLite implements Db {
    private final File file;
    private final Executor executor;
    private Connection conn;

    public SQLite(final File file) {
        this.file = file;
        this.executor = Executors.newSingleThreadExecutor();
    }

    @Override
    public boolean connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            this.conn = DriverManager.getConnection("jdbc:sqlite:" + this.file.getParentFile() + File.separator + this.file.getName());
            return true;
        } catch (Exception e) {
            DropPlugin.getPlugin().getLogger().log(Level.WARNING, "An error occured while connecting to SQLite!", e);
            return false;
        }
    }

    @Override
    public void update(final String update) {
        if (!this.isConnected()) {
            this.reconnect();
        }
        this.executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    SQLite.this.conn.createStatement().executeUpdate(update);
                } catch (SQLException e) {
                    DropPlugin.getPlugin().getLogger().log(Level.WARNING, "An error occured while executing update!", e);
                }
            }
        });
    }

    @Override
    public void updateNow(final String update) {
        if (!this.isConnected()) {
            this.reconnect();
        }
        try {
            this.conn.createStatement().executeUpdate(update);
        } catch (SQLException e) {
            DropPlugin.getPlugin().getLogger().log(Level.WARNING, "An error occured while executing update now!", e);
        }
    }

    @Override
    public void disconnect() {
        if (this.conn != null) {
            try {
                this.conn.close();
            } catch (SQLException ex) {
            }
        }
    }

    @Override
    public boolean reconnect() {
        return this.connect();
    }

    @Override
    public boolean isConnected() {
        try {
            return !this.conn.isClosed() || this.conn == null;
        } catch (SQLException ex) {
            return false;
        }
    }

    @Override
    public ResultSet query(final String query) {
        if (!this.isConnected()) {
            this.reconnect();
        }
        try {
            return this.conn.createStatement().executeQuery(query);
        } catch (SQLException e) {
            DropPlugin.getPlugin().getLogger().log(Level.WARNING, "An error occured while executing query!", e);
            return null;
        }
    }

    @Override
    public Connection getConnection() {
        return this.conn;
    }
}
