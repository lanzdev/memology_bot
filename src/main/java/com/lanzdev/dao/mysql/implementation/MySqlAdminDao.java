package com.lanzdev.dao.mysql.implementation;

import com.lanzdev.dao.entity.AdminDao;
import com.lanzdev.dao.mysql.AbstractMySqlDao;
import com.lanzdev.dao.mysql.Query;
import com.lanzdev.model.entity.Admin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

public class MySqlAdminDao
        extends AbstractMySqlDao<Admin, Integer>
        implements AdminDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(MySqlAdminDao.class);

    @Override
    protected String getInsertQuery( ) {
        return Query.INSERT_ADMIN;
    }

    @Override
    protected String getSelectLastQuery( ) {
        return Query.SELECT_LAST_ADMIN;
    }

    @Override
    protected String getSelectByIdQuery( ) {
        return Query.SELECT_ADMIN_BY_ID;
    }

    @Override
    protected String getSelectAllQuery( ) {
        return Query.SELECT_ALL_ADMINS;
    }

    @Override
    protected String getUpdateQuery( ) {
        return Query.UPDATE_ADMIN;
    }

    @Override
    protected String getDeleteQuery( ) {
        return Query.DELETE_ADMIN;
    }

    @Override
    protected void prepareStatementForCreate(PreparedStatement stmt, Admin object) {

        try {
            stmt.setString(1, object.getLogin());
            stmt.setString(2, object.getPassword());
        } catch (Exception e) {
            LOGGER.error("Exception while preparing statement.", e);
        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement stmt, Admin object) {

        try {
            stmt.setString(1, object.getLogin());
            stmt.setString(2, object.getPassword());
            stmt.setInt(3, object.getId());
        } catch (Exception e) {
            LOGGER.error("Exception while preparing statement.", e);
        }
    }

    @Override
    protected List<Admin> parseResultSet(ResultSet rs) {

        List<Admin> list = new LinkedList<>();

        try {
            while (rs.next()) {
                Admin admin = new Admin();
                admin.setId(rs.getInt("admin_id"));
                admin.setLogin(rs.getString("login"));
                admin.setPassword(rs.getString("password"));
                list.add(admin);
            }
        } catch (Exception e) {
            LOGGER.error("Exception while preparing statement.", e);
        }

        return list;
    }
}
