package nl.officialfox.kitac;

import java.sql.*;

public class MySQL {

    private String host;
    private int port;
    private String database;
    private String user;
    private String password;

    public MySQL(String host, int port, String database, String user, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.user = user;
        this.password = password;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, user, password);
    }

    public void executeUpdate(String query, Object... params) {
        try (Connection conn = getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(query)) {

                for (int i = 0; i < params.length; i++) {
                    stmt.setObject(i + 1, params[i]);
                }
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet executeQuery(String query, Object... params) {
        try (Connection conn = getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                // set params
                for (int i = 0; i < params.length; i++) {
                    stmt.setObject(i + 1, params[i]);
                }
                return stmt.executeQuery();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
