package com.lanzdev.dao.mysql.implementation;

import com.lanzdev.dao.ConnectionDB;
import com.lanzdev.dao.entity.WallDao;
import com.lanzdev.dao.mysql.AbstractMySqlDao;
import com.lanzdev.dao.mysql.Query;
import com.lanzdev.model.entity.Wall;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

public class MySqlWallDao
        extends AbstractMySqlDao<Wall, Integer>
        implements WallDao {

    @Override
    public List<Wall> getAllApproved( ) {

        List<Wall> list = new LinkedList<>();
        connection = ConnectionDB.getConnection();

        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(Query.SELECT_ALL_APPROVED);
            list = parseResultSet(rs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }

        return list;
    }

    @Override
    public Wall getByDomain(String domain) {

        Wall wall = null;
        connection = ConnectionDB.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(Query.SELECT_BY_DOMAIN)) {
            stmt.setString(1, domain);
            ResultSet rs = stmt.executeQuery();
            List<Wall> list = parseResultSet(rs);
            if (list.size() != 0) {
                wall = list.iterator().next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }

        return wall;
    }

    @Override
    protected String getInsertQuery( ) {
        return Query.INSERT_WALL;
    }

    @Override
    protected String getSelectLastQuery( ) {
        return Query.SELECT_LAST_WALL;
    }

    @Override
    protected String getSelectByIdQuery( ) {
        return Query.SELECT_WALL_BY_ID;
    }

    @Override
    protected String getSelectAllQuery( ) {
        return Query.SELECT_ALL_WALLS;
    }

    @Override
    protected String getUpdateQuery( ) {
        return Query.UPDATE_WALL;
    }

    @Override
    protected String getDeleteQuery( ) {
        return Query.DELETE_WALL;
    }

    @Override
    protected void prepareStatementForCreate(PreparedStatement stmt, Wall object) {

        try {
            stmt.setString(1, object.getWallDomain());
            stmt.setBoolean(2, object.isApproved());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement stmt, Wall object) {

        try {
            stmt.setString(1, object.getWallDomain());
            stmt.setBoolean(2, object.isApproved());
            stmt.setInt(3, object.getPopularity());
            stmt.setInt(4, object.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected List<Wall> parseResultSet(ResultSet rs) {

        List<Wall> list = new LinkedList<>();

        try {
            while (rs.next()) {
                Wall wall = new Wall();
                wall.setId(rs.getInt("wall_id"));
                wall.setWallDomain(rs.getString("wall_domain"));
                wall.setApproved(rs.getBoolean("approved"));
                wall.setPopularity(rs.getInt("popularity"));
                list.add(wall);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

}
