package com.lanzdev.dao.mysql.implementation;

import com.lanzdev.dao.entity.ChatDao;
import com.lanzdev.dao.mysql.AbstractMySqlDao;
import com.lanzdev.dao.mysql.Query;
import com.lanzdev.model.entity.Chat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

public class MySqlChatDao
        extends AbstractMySqlDao<Chat, Long>
        implements ChatDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(MySqlChatDao.class);

    @Override
    protected String getInsertQuery( ) {
        return Query.INSERT_CHAT;
    }

    @Override
    protected String getSelectLastQuery( ) {
        return Query.SELECT_LAST_CHAT;
    }

    @Override
    protected String getSelectByIdQuery( ) {
        return Query.SELECT_CHAT_BY_ID;
    }

    @Override
    protected String getSelectAllQuery( ) {
        return Query.SELECT_ALL_CHATS;
    }

    @Override
    protected String getUpdateQuery( ) {
        return Query.UPDATE_CHAT;
    }

    @Override
    protected String getDeleteQuery( ) {
        return Query.DELETE_CHAT;
    }

    @Override
    protected void prepareStatementForCreate(PreparedStatement stmt, Chat object) {

        try {
            stmt.setLong(1, object.getId());
            stmt.setString(2, object.getFirstName());
            stmt.setString(3, object.getLastName());
            stmt.setBoolean(4, object.isSuspended());
            stmt.setString(5, object.getLastCommand());
        } catch (Exception e) {
            LOGGER.error("Exception while preparing statement.", e);
        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement stmt, Chat object) {

        try {
            stmt.setString(1, object.getFirstName());
            stmt.setString(2,object.getLastName());
            stmt.setBoolean(3, object.isSuspended());
            stmt.setString(4, object.getLastCommand());
            stmt.setLong(5, object.getId());
        } catch (Exception e) {
            LOGGER.error("Exception while preparing statement.", e);
        }
    }

    @Override
    protected List<Chat> parseResultSet(ResultSet rs) {

        List<Chat> list = new LinkedList<>();

        try {
            while (rs.next()) {
                Chat chat = new Chat();
                chat.setId(rs.getLong("chat_id"));
                chat.setFirstName(rs.getString("first_name"));
                chat.setLastName(rs.getString("last_name"));
                chat.setSuspended(rs.getBoolean("suspended"));
                chat.setLastCommand(rs.getString("last_command"));
                list.add(chat);
            }
        } catch (Exception e) {
            LOGGER.error("Exception while preparing statement.", e);
        }

        return list;
    }
}
