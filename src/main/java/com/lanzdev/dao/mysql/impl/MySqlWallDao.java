package com.lanzdev.dao.mysql.impl;

import com.lanzdev.dao.ConnectionPool;
import com.lanzdev.dao.entity.WallDao;
import com.lanzdev.dao.mysql.AbstractMySqlDao;
import com.lanzdev.dao.mysql.Query;
import com.lanzdev.domain.Wall;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class MySqlWallDao
        extends AbstractMySqlDao<Wall, Integer>
        implements WallDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(MySqlWallDao.class);

    @Override
    public List<Wall> getAllApproved( ) {

        List<Wall> list = new LinkedList<>();


        try (Connection connection = ConnectionPool.getConnection();
                Statement stmt = connection.createStatement()) {

            ResultSet rs = stmt.executeQuery(Query.SELECT_ALL_APPROVED);
            list = parseResultSet(rs);
        } catch (Exception e) {
            LOGGER.error("Exception while creating statement.", e);
        }

        return list;
    }

    @Override
    public Wall getByDomain(String domain) {

        Wall wall = null;

        try (Connection connection = ConnectionPool.getConnection();
                PreparedStatement stmt = connection.prepareStatement(Query.SELECT_BY_DOMAIN)) {

            stmt.setString(1, domain);
            ResultSet rs = stmt.executeQuery();
            List<Wall> list = parseResultSet(rs);
            if (list.size() != 0) {
                wall = list.iterator().next();
            }
        } catch (SQLException e) {
            LOGGER.error("Exception while creating prepared statement.", e);
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
        } catch (Exception e) {
            LOGGER.error("Exception while preparing statement.", e);
        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement stmt, Wall object) {

        try {
            stmt.setString(1, object.getWallDomain());
            stmt.setBoolean(2, object.isApproved());
            stmt.setInt(3, object.getPopularity());
            stmt.setInt(4, object.getId());
        } catch (Exception e) {
            LOGGER.error("Exception while preparing statement.", e);
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
        } catch (Exception e) {
            LOGGER.error("Exception while preparing statement.", e);
        }

        return list;
    }

}
