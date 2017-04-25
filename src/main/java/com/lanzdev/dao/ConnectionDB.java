package com.lanzdev.dao;

import com.lanzdev.BuildVars;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDB {

    public static synchronized Connection getConnection() {

        Connection connection = null;
        try {
            Class.forName(BuildVars.driverDB).newInstance();
            connection = DriverManager.getConnection(
                    BuildVars.linkDB, BuildVars.userDB, BuildVars.passwordDB);

        } catch (SQLException | ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }

        return connection;
    }
}
