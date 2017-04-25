package com.lanzdev.dao.mysql.implementation;

import com.lanzdev.dao.entity.WallDao;
import com.lanzdev.dao.mysql.AbstractMySqlDao;
import com.lanzdev.dao.mysql.Query;
import com.lanzdev.model.entity.Wall;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class MySqlWallDao
        extends AbstractMySqlDao<Wall, Integer>
        implements WallDao {

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

    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement stmt, Wall object) {

    }

    @Override
    protected List<Wall> parseResultSet(ResultSet rs) {
        return null;
    }
}
