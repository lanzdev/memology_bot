package com.lanzdev.dao.mysql;

import com.lanzdev.dao.ConnectionDB;
import com.lanzdev.dao.DaoException;
import com.lanzdev.dao.GenericDao;
import com.lanzdev.model.Identified;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractMySqlDao<T extends Identified<PK>, PK> implements GenericDao<T, PK> {

    protected Connection connection;

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
    public T create(T object) {

        connection = ConnectionDB.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(getInsertQuery())) {
            prepareStatementForCreate(stmt, object);
            int count = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        T created = null;

        try (PreparedStatement stmt = connection.prepareStatement(getSelectLastQuery())) {
            ResultSet rs = stmt.executeQuery();
            List<T> list = parseResultSet(rs);
            if (list == null || list.size() != 1) {
                throw new DaoException("Can't get last added object");
            }
            created = list.iterator().next();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }

        return created;
    }

    @Override
    public T get(PK key) {

        List<T> list = new ArrayList<>();
        connection = ConnectionDB.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(getSelectByIdQuery())) {
            stmt.setObject(1, key);
            ResultSet rs = stmt.executeQuery();
            list = parseResultSet(rs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }

        if (list == null || list.size() == 0) {
            return null;
        }

        if (list.size() > 1) {
            return null;
        }

        return list.iterator().next();
    }

    @Override
    public List<T> getAll( ) {

        List<T> list = new ArrayList<>();
        connection = ConnectionDB.getConnection();

        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(getSelectAllQuery());
            list = parseResultSet(rs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }

        return list;
    }

    @Override
    public void update(T object) {

        connection = ConnectionDB.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(getUpdateQuery())) {
            prepareStatementForUpdate(stmt, object);
            int count = stmt.executeUpdate();
            if (count != 1) {
                throw new DaoException("On update modify more then 1 record: " + count);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    @Override
    public void delete(T object) {

        connection = ConnectionDB.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(getDeleteQuery())) {
            stmt.setObject(1, object.getId());
            int count = stmt.executeUpdate();
            if (count != 1) {
                throw new DaoException("On delete modify more then 1 record: " + count);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    protected void closeConnection() {

        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
