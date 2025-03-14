package ru.tecon.leakDetection.ejb;

import jakarta.annotation.Resource;
import jakarta.ejb.Startup;
import jakarta.ejb.Stateless;


import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Startup
@Stateless
public class CheckUserSB {

    private static final Logger LOG = Logger.getLogger(CheckUserSB.class.getName());

    private static final String CHECK_SESSION = "select td_adm.get_active_session_login(?)";
    private static final String CHECK_SESSION_RIGHTS = "select td_adm.get_active_session_rights(?, ?)";

    @Resource(name = "jdbc/DataSourceR")
    private DataSource ds;

    public boolean checkSession(String sessionID) {
        try (Connection connection = ds.getConnection();
             PreparedStatement stm = connection.prepareStatement(CHECK_SESSION)) {
            stm.setString(1, sessionID);

            ResultSet res = stm.executeQuery();
            if (res.next() && (res.getString(1) != null)) {
                return true;
            }
        } catch (SQLException e) {
            LOG.log(Level.WARNING, "check session error: ", e);
        }
        return false;
    }

    public String getUser(String sessionID) {
        try (Connection connection = ds.getConnection();
             PreparedStatement stm = connection.prepareStatement(CHECK_SESSION)) {
            stm.setString(1, sessionID);

            ResultSet res = stm.executeQuery();
            if (res.next() && (res.getString(1) != null)) {
                return res.getString(1);
            }
        } catch (SQLException e) {
            LOG.log(Level.WARNING, "check session error: ", e);
        }
        return null;
    }

    public boolean checkSessionWrite(String sessionID, int formID) {
        try (Connection connection = ds.getConnection();
             PreparedStatement stm = connection.prepareStatement(CHECK_SESSION_RIGHTS)) {
            stm.setString(1, sessionID);
            stm.setInt(2, formID);

            ResultSet res = stm.executeQuery();
            if (res.next() && (res.getInt(1) == 0)) {
                return true;
            }
        } catch (SQLException e) {
            LOG.log(Level.WARNING, "check session write error: ", e);
        }
        return false;
    }
}


