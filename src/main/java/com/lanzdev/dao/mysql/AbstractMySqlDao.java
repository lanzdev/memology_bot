package com.lanzdev.dao.mysql;

import com.lanzdev.dao.ConnectionPool;
import com.lanzdev.dao.DaoException;
import com.lanzdev.dao.GenericDao;
import com.lanzdev.domain.Identified;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractMySqlDao<T extends Identified<PK>, PK> implements GenericDao<T, PK> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractMySqlDao.class);

    protected abstract String getInsertQuery( );

    protected abstract void prepareStatementForCreate(PreparedStatement stmt, T object);

    protected abstract String getSelectLastQuery( );

    protected abstract List<T> parseResultSet(ResultSet rs);

    protected abstract String getSelectByIdQuery( );

    protected abstract String getSelectAllQuery( );

    protected abstract String getUpdateQuery( );

    protected abstract void prepareStatementForUpdate(PreparedStatement stmt, T object);

    protected abstract String getDeleteQuery( );

    @Override
    public void create(T object) {

        try (Connection connection = ConnectionPool.getConnection();
                PreparedStatement stmt = connection.prepareStatement(getInsertQuery())) {

            prepareStatementForCreate(stmt, object);
            int count = stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Exception while creating prepared statement.", e);
        }
    }

    @Override
    public T get(PK key) {

        List<T> list = new ArrayList<>();

        try (Connection connection = ConnectionPool.getConnection();
                PreparedStatement stmt = connection.prepareStatement(getSelectByIdQuery())) {

            stmt.setObject(1, key);
            ResultSet rs = stmt.executeQuery();
            list = parseResultSet(rs);
        } catch (SQLException e) {
            LOGGER.error("Exception while creating prepared statement. %s", e);
        } catch (NullPointerException e) {
            LOGGER.error("There is no object with such key: {}", key);
        }

        if (list == null || list.size() == 0 || list.size() > 1) {
            return null;
        }

        return list.iterator().next();
    }

    @Override
    public List<T> getAll( ) {

        List<T> list = new ArrayList<>();

        try (Connection connection = ConnectionPool.getConnection();
                Statement stmt = connection.createStatement()) {

            ResultSet rs = stmt.executeQuery(getSelectAllQuery());
            list = parseResultSet(rs);
        } catch (Exception e) {
            LOGGER.error("Exception while creating statement.", e);
        }

        return list;
    }

    @Override
    public void update(T object) {

        try (Connection connection = ConnectionPool.getConnection();
                PreparedStatement stmt = connection.prepareStatement(getUpdateQuery())) {

            prepareStatementForUpdate(stmt, object);
            int count = stmt.executeUpdate();
            if (count != 1) {
                throw new DaoException("On update modify more then 1 record: " + count);
            }
        } catch (Exception e) {
            LOGGER.error("Exception while creating prepared statement.", e);
        }
    }

    @Override
    public void delete(T object) {

        try (Connection connection = ConnectionPool.getConnection();
                PreparedStatement stmt = connection.prepareStatement(getDeleteQuery())) {

            stmt.setObject(1, object.getId());
            int count = stmt.executeUpdate();
            if (count != 1) {
                throw new DaoException("On delete modify more then 1 record: " + count);
            }
        } catch (Exception e) {
            LOGGER.error("Exception while creating prepared statement.", e);
        }
    }

}
