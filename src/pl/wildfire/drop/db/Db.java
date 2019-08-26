package pl.wildfire.drop.db;

import java.sql.Connection;
import java.sql.ResultSet;

public interface Db {
    Connection getConnection();

    boolean connect();

    void disconnect();

    boolean reconnect();

    boolean isConnected();

    ResultSet query(String p0);

    void update(String p0);

    void updateNow(String p0);
}
