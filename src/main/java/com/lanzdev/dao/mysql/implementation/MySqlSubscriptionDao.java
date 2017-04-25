package com.lanzdev.dao.mysql.implementation;

import com.lanzdev.dao.ConnectionDB;
import com.lanzdev.dao.entity.SubscriptionDao;
import com.lanzdev.dao.mysql.AbstractMySqlDao;
import com.lanzdev.dao.mysql.Query;
import com.lanzdev.model.entity.Subscription;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class MySqlSubscriptionDao
        extends AbstractMySqlDao<Subscription, Integer>
        implements SubscriptionDao {

    @Override
    public List<Subscription> getSubscriptionsByChat(Long chatId) {

        List<Subscription> list = new LinkedList<>();

        connection = ConnectionDB.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(Query.SELECT_SUBSCRIPTION_BY_CHAT)) {
            stmt.setLong(1, chatId);
            list = parseResultSet(stmt.executeQuery());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }

        return list;
    }

    @Override
    protected String getInsertQuery( ) {
        return Query.INSERT_SUBSCRIPTION;
    }

    @Override
    protected String getSelectLastQuery( ) {
        return Query.SELECT_LAST_SUBSCRIPTION;
    }

    @Override
    protected String getSelectByIdQuery( ) {
        return Query.SELECT_SUBSCRIPTION_BY_ID;
    }

    @Override
    protected String getSelectAllQuery( ) {
        return Query.SELECT_ALL_SUBSCRIPTIONS;
    }

    @Override
    protected String getUpdateQuery( ) {
        return Query.UPDATE_SUBSCRIPTION;
    }

    @Override
    protected String getDeleteQuery( ) {
        return Query.DELETE_SUBSCRIPTION;
    }

    @Override
    protected void prepareStatementForCreate(PreparedStatement stmt, Subscription object) {

        try {
            stmt.setLong(1, object.getChatId());
            stmt.setString(2, object.getWallDomain());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement stmt, Subscription object) {

        throw new UnsupportedOperationException();
    }

    @Override
    protected List<Subscription> parseResultSet(ResultSet rs) {

        List<Subscription> list = new LinkedList<>();

        try {
            while (rs.next()) {
                Subscription subscription = new Subscription();
                subscription.setChatId(rs.getLong("chat_id"));
                subscription.setWallDomain(rs.getString("wall_domain"));
                list.add(subscription);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}
