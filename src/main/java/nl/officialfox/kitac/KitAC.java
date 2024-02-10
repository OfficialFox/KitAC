package nl.officialfox.kitac;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class KitAC extends JavaPlugin implements Listener {

    String host = "localhost";
    int port = 3306;
    String db = "players";
    String user = "root";
    String pass = "password";
    private MySQL mysql;

    @Override
    public void onEnable() {

        mysql = new MySQL(host, port, db, user, pass);

        mysql.executeUpdate("CREATE TABLE IF NOT EXISTS playerdata (uuid VARCHAR(36), name VARCHAR(16), kills INT, deaths INT, money INT)");

        getServer().getPluginManager().registerEvents(this, this);

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        mysql.executeUpdate("INSERT INTO playerdata VALUES(?, ?, 0, 0, 0)", uuid, player.getName());

    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {

        Player player = event.getEntity();
        UUID playerUUID = player.getUniqueId();

        mysql.executeUpdate("UPDATE playerdata SET deaths = deaths + 1 WHERE uuid = ?", playerUUID);

        Player killer = player.getKiller();
        if(killer != null) {
            UUID killerUUID = killer.getUniqueId();
            mysql.executeUpdate("UPDATE playerdata SET kills = kills + 1 WHERE uuid = ?", killerUUID);
        }

    }

    @EventHandler
    public void onMoneyTransfer(MoneyTransferEvent event) throws SQLException {

        Player sender = event.getSender();
        UUID senderUUID = sender.getUniqueId();

        Player receiver = event.getReceiver();
        UUID receiverUUID = receiver.getUniqueId();

        int amount = event.getAmount();

        int senderMoney = getMoney(senderUUID);
        int receiverMoney = getMoney(receiverUUID);

        if(senderMoney >= amount) {
            mysql.executeUpdate("UPDATE playerdata SET money = money - ? WHERE uuid = ?", amount, senderUUID);
            mysql.executeUpdate("UPDATE playerdata SET money = money + ? WHERE uuid = ?", amount, receiverUUID);
        }

    }

    public int getMoney(UUID uuid) throws SQLException {
        ResultSet rs = mysql.executeQuery("SELECT money FROM playerdata WHERE uuid = ?", uuid);
        if (rs.next()) {
            return rs.getInt("money");
        }
        return 0;
    }

    public int getKills(UUID uuid) throws SQLException {
        ResultSet rs = mysql.executeQuery("SELECT kills FROM playerdata WHERE uuid = ?", uuid);
        if (rs.next()) {
            return rs.getInt("kills");
        }
        return 0;
    }

    public int getDeaths(UUID uuid) throws SQLException {
        ResultSet rs = mysql.executeQuery("SELECT deaths FROM playerdata WHERE uuid = ?", uuid);
        if (rs.next()) {
            return rs.getInt("deaths");
        }
        return 0;
    }

}