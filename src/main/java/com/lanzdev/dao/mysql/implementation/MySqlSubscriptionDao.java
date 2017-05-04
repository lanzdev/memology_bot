package com.lanzdev.dao.mysql.implementation;

import com.lanzdev.dao.ConnectionDB;
import com.lanzdev.dao.entity.SubscriptionDao;
import com.lanzdev.dao.mysql.AbstractMySqlDao;
import com.lanzdev.dao.mysql.Query;
import com.lanzdev.model.entity.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MySqlSubscriptionDao
        extends AbstractMySqlDao<Subscription, Integer>
        implements SubscriptionDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(MySqlSubscriptionDao.class);

    @Override
    public List<Subscription> getByChat(Long chatId) {

        List<Subscription> list = new LinkedList<>();

        connection = ConnectionDB.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(Query.SELECT_SUBSCRIPTION_BY_CHAT)) {
            stmt.setLong(1, chatId);
            list = parseResultSet(stmt.executeQuery());
        } catch (Exception e) {
            LOGGER.error("Exception while creating prepared statement.", e);
        } finally {
            closeConnection();
        }

        return list;
    }

    @Override
    public Subscription getByChatAndWall(Long chatId, String wallDomain) {

        List<Subscription> list = new ArrayList<>();
        connection = ConnectionDB.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(Query.SELECT_SUBSCRIPTION_BY_CHAT_AND_WALL)) {
            stmt.setLong(1, chatId);
            stmt.setString(2, wallDomain);
            ResultSet rs = stmt.executeQuery();
            list = parseResultSet(rs);
        } catch (SQLException e) {
            LOGGER.error("Exception while creating prepared statement.", e);
        } catch (NullPointerException e) {
            LOGGER.error("Subscription for {} is not found", wallDomain);
        } finally {
            closeConnection();
        }

        if (list == null || list.size() == 0 || list.size() > 1) {
            return null;
        }

        return list.iterator().next();
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
        } catch (Exception e) {
            LOGGER.error("Exception while preparing statement.", e);
        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement stmt, Subscription object) {

        try {
            stmt.setLong(1, object.getChatId());
            stmt.setString(2, object.getWallDomain());
            stmt.setLong(3, object.getLastPostId());
            stmt.setBoolean(4, object.isActive());
            stmt.setInt(5, object.getId());
        } catch (Exception e) {
            LOGGER.error("Exception while preparing statement.", e);
        }
    }

    @Override
    protected List<Subscription> parseResultSet(ResultSet rs) {

        List<Subscription> list = new LinkedList<>();

        try {
            while (rs.next()) {
                Subscription subscription = new Subscription();
                subscription.setId(rs.getInt("subscription_id"));
                subscription.setChatId(rs.getLong("chat_id"));
                subscription.setWallDomain(rs.getString("wall_domain"));
                subscription.setLastPostId(rs.getLong("last_post_id"));
                subscription.setActive(rs.getBoolean("active"));
                list.add(subscription);
            }
        } catch (Exception e) {
            LOGGER.error("Exception while preparing statement.", e);
        }

        return list;
    }
}
