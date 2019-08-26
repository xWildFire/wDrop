package pl.wildfire.drop.db;

import pl.wildfire.drop.DropPlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Level;

public class MySQL implements Db {
    private final String host;
    private final String user;
    private final String pass;
    private final String name;
    private final int port;
    private final Executor executor;
    private Connection conn;

    public MySQL(final String host, final int port, final String user, final String pass, final String name) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.pass = pass;
        this.name = name;
        this.executor = Executors.newSingleThreadExecutor();
    }

    public MySQL(final String host, final String user, final String pass, final String name) {
        this(host, 3306, user, pass, name);
    }

    @Override
    public boolean connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            this.conn = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.name, this.user, this.pass);
            return true;
        } catch (Exception e) {
            DropPlugin.getPlugin().getLogger().log(Level.WARNING, "An error occured while connecting to MySQL!", e);
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
                    MySQL.this.conn.createStatement().executeUpdate(update);
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
