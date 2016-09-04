package database;

import entities.Login;
import entities.RecvMessage;
import entities.SendMessage;
import entities.User;

import java.net.UnknownServiceException;
import java.sql.*;
import java.util.*;

/**
 * Created by paulosk on 10/04/16.
 */
public class AccessMananger {

    private final Connection con;

    public AccessMananger() {
        con = DatabaseConnection.connect();
    }

    private boolean withinDistance(String sendDynID, String recvDynID) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(
                "WITH sender AS (                                                   "+
                        "   SELECT dynid, geog FROM user_loc WHERE dynid=?                  "+
                        "                                                                   "+
                        "), receiver AS (                                                   "+
                        "   SELECT dynid, geog FROM user_loc WHERE dynid=?                  "+
                        ")                                                                  "+
                        "                                                                   "+
                        "SELECT 1 FROM sender, receiver                                     "+
                        "WHERE ST_DWithin(sender.geog, receiver.geog, 5000.0)               "
        );

        stmt.setString(1, sendDynID);
        stmt.setString(2, recvDynID);

        ResultSet res = stmt.executeQuery();
        res.next();

        return (res.getRow() == 1);
    }


    /* Returns null if user is not online */
    private String getReceiverDynamicID(String recvUser) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(
                "SELECT dynid FROM users INNER JOIN user_dynid ON users.id = user_dynid.uid " +
                        "WHERE users.name = ?"
        );

        stmt.setString(1, recvUser);

        ResultSet res = stmt.executeQuery();
        res.next();

        return (res.getRow() == 0) ? null : res.getString(1);
    }

    public SendMessage sendMessage(String dynid, String recvUser, String message) throws SQLException {
        String recvDynID;

        if ((recvDynID = getReceiverDynamicID(recvUser)) == null) {
            System.err.println("SEND MESSAGE: USER NOT ONLINE");
            return new SendMessage("User '" + recvUser + "' is not online.");
        }

        if ( ! withinDistance(dynid, recvDynID)) {
            System.err.println("SEND MESSAGE: USER NOT WITHIN DISTANCE.");
            return new SendMessage("User '" + recvUser + "' is not within distance.");
        }

        PreparedStatement stmt = con.prepareStatement(
                "WITH sender AS (                                   "+
                        "   SELECT id FROM users INNER JOIN user_dynid      "+
                        "       ON users.id=user_dynid.uid                  "+
                        "   WHERE dynid=?                                   "+
                        "                                                   "+
                        "), receiver AS (                                   "+
                        "    SELECT id FROM users INNER JOIN user_dynid     "+
                        "       ON users.id=user_dynid.uid                  "+
                        "    WHERE dynid=?                                  "+
                        "                                                   "+
                        "), message AS (                                    "+
                        "   SELECT ? AS msg                                 "+
                        ")                                                  "+
                        "                                                   "+
                        "INSERT INTO messages (send, recv, msg)             "+
                        "   SELECT sender.id, receiver.id, message.msg      "+
                        "       FROM sender, receiver, message              "
        );

        stmt.setString(1, dynid);
        stmt.setString(2, recvDynID);
        stmt.setString(3, message);

        stmt.executeUpdate();

        int updateCount = stmt.getUpdateCount();
        if (updateCount < 1) {
            return new SendMessage("User '" + recvUser + "' is not online.''");
        }

        return new SendMessage();
    }

    public List<User> getZoneUsers(String dynid) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(
                "WITH online_users AS (                                                     "+
                        "   SELECT name, age, email, description, geog, user_dynid.dynid AS dynid   "+
                        "   FROM users INNER JOIN user_dynid ON id=uid INNER JOIN user_loc          "+
                        "       ON user_loc.dynid=user_dynid.dynid                                  "+
                        "                                                                           "+
                        "), main_user AS (                                                          "+
                        "   SELECT geog FROM online_users WHERE dynid = ?                           "+
                        "                                                                           "+
                        "), other_users AS (                                                        "+
                        "   SELECT name, age, email, description, geog FROM online_users            "+
                        "   WHERE dynid != ?                                                        "+
                        ")                                                                          "+
                        "                                                                           "+
                        "SELECT name, age, email, description                                       "+
                        "FROM main_user CROSS JOIN other_users                                      "+
                        "WHERE ST_DWithin(main_user.geog, other_users.geog, 5000)                   "
        );

        stmt.setString(1, dynid);
        stmt.setString(2, dynid);

        ResultSet res = stmt.executeQuery();

        LinkedList<User> users = new LinkedList<User>();
        while (res.next()) {
            String name = res.getString(1);
            int age = res.getInt(2);
            String email = res.getString(3);
            String desc = res.getString(4);

            users.add(new User(name, age, email, desc));
        }

        return users;
    }


    public boolean registerUser(String username, String name, String pass,
                                String age, String email, String description) throws SQLException {


        PreparedStatement stmt = con.prepareStatement(
                "INSERT INTO users (username, name, passw, age, email, description)" +
                        "   VALUES (?, ?, ?, ?, ?, ?)"
        );

        stmt.setString(1, username);
        stmt.setString(2, name);
        stmt.setString(3, pass);
        stmt.setInt(4, Integer.parseInt(age));
        stmt.setString(5, email);
        stmt.setString(6, description);

        stmt.executeUpdate();

        return true;
    }

    public List<RecvMessage> receiveMessages(String dynid) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(
                        "WITH recv_msgs AS (                                                    " +
                        "   SELECT messages.id AS mid, msg, send AS sender_id, send_date FROM   " +
                        "       user_dynid INNER JOIN messages                                  " +
                        "           ON user_dynid.uid=messages.recv WHERE dynid=?               " +
                        "), deleted AS (                                                        " +
                        "   DELETE FROM messages USING recv_msgs                                " +
                        "   WHERE messages.id=recv_msgs.mid                                     " +
                        ")                                                                      " +
                        "                                                                       " +
                        "SELECT msg, users.name, send_date FROM                                 " +
                        "   recv_msgs INNER JOIN users ON recv_msgs.sender_id=users.id          " +
                        "ORDER BY send_date ASC                                                 "
        );

        stmt.setString(1, dynid);
        ResultSet res = stmt.executeQuery();

        LinkedList<RecvMessage> messages = new LinkedList<RecvMessage>();
        while (res.next()) {
            String msg = res.getString(1);
            String sender = res.getString(2);
            Timestamp ts = res.getTimestamp(3);

            messages.add(new RecvMessage(
                    msg, sender, new java.util.Date(ts.getTime())
            ));
        }

        return messages;
    }


    public boolean publishLocation(String dynID, String latitude, String longitude) throws SQLException {

        final String location = "POINT(" + longitude + " " + latitude + ")";

        /* Read the following thread for potential concurrency problems:
                http://stackoverflow.com/questions/1109061/insert-on-duplicate-update-in-postgresql/6527838#6527838
             */
        PreparedStatement stmt = con.prepareStatement(
                "UPDATE user_loc SET geog=ST_GeographyFromText(?) WHERE dynid=?;" +
                        "INSERT INTO user_loc (dynid, geog) SELECT ?, ST_GeographyFromText(?) " +
                        "WHERE NOT EXISTS (SELECT 1 FROM user_loc WHERE dynid=?)"
        );

        stmt.setString(1, location);
        stmt.setString(2, dynID);
        stmt.setString(3, dynID);
        stmt.setString(4, location);
        stmt.setString(5, dynID);

        stmt.executeUpdate();
        return true;
    }

    public boolean logoutUser(String dynID) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(
                "DELETE FROM user_dynid WHERE dynid=?"
        );

        stmt.setString(1, dynID);
        stmt.executeUpdate();

        return stmt.getUpdateCount() == 1;
    }

    public Login loginUser(String user, String pass) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(
                "SELECT count(*), id FROM users WHERE username=? AND passw=? GROUP BY id"
        );

        stmt.setString(1, user);
        stmt.setString(2, pass);

        ResultSet res = stmt.executeQuery();
        res.next();

        if (res.getRow() == 0 || res.getInt(1) != 1) {
            Login log = new Login();
            log.setErrorMessage("Invalid login data. Please try again!");
            return log;
        }

            /* Login succeeded */

        final int uid = res.getInt(2);
        res.close();

        stmt = con.prepareStatement(
                "INSERT INTO user_dynid (uid, dynid) " +
                        "SELECT id, md5(username || random()::text) AS dynid FROM users WHERE id=?"
        );

        stmt.setInt(1, uid);
        stmt.executeUpdate();

        stmt = con.prepareStatement("SELECT dynid FROM user_dynid WHERE uid=?");
        stmt.setInt(1, uid);

        res = stmt.executeQuery();
        res.next();

        String dynamicID = res.getString(1);

        res.close();

        return new Login(dynamicID);
    }

    public void close()  {
        try {
            con.close();
        } catch(SQLException e) {
            System.err.println("SQLException @ AccessManager.close(): " + e.getMessage());
        }
    }
}
