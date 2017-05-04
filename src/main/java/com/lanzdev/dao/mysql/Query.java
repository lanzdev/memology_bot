package com.lanzdev.dao.mysql;

public class Query {

    /*
     * Admin queries
     */
    public static final String INSERT_ADMIN = "INSERT INTO admin (login, password) VALUES (?, ?)";
    public static final String SELECT_LAST_ADMIN = "SELECT * FROM admin WHERE admin_id = last_insert_id()";
    public static final String SELECT_ADMIN_BY_ID = "SELECT * FROM admin WHERE admin_id = ?";
    public static final String SELECT_ALL_ADMINS = "SELECT * FROM admin";
    public static final String UPDATE_ADMIN = "UPDATE admin SET login=?, password=? WHERE admin_id = ?";
    public static final String DELETE_ADMIN = "DELETE FROM admin WHERE admin_id = ?";

    /*
     * Chat queries
     */
    public static final String INSERT_CHAT = "INSERT INTO chat (chat_id, first_name, last_name, suspended, last_command) VALUES (?, ?, ?, ?, ?)";
    public static final String SELECT_LAST_CHAT = "SELECT * FROM chat WHERE chat_id = last_insert_id()";
    public static final String SELECT_CHAT_BY_ID = "SELECT * FROM chat WHERE chat_id = ?";
    public static final String SELECT_ALL_CHATS = "SELECT * FROM chat";
    public static final String UPDATE_CHAT = "UPDATE chat SET first_name = ?, last_name = ?, suspended = ?, last_command = ? WHERE chat_id = ?";
    public static final String DELETE_CHAT = "DELETE FROM chat WHERE chat_id = ?";

    /*
     * Subscription queries
     */
    public static final String INSERT_SUBSCRIPTION = "INSERT INTO subscription (chat_id, wall_domain) VALUES (?, ?)";
    public static final String SELECT_LAST_SUBSCRIPTION = "SELECT * FROM subscription WHERE subscription_id = last_insert_id()";
    public static final String SELECT_SUBSCRIPTION_BY_ID = "SELECT * FROM subscription WHERE subscription_id = ?";
    public static final String SELECT_ALL_SUBSCRIPTIONS = "SELECT * FROM subscription";
    public static final String SELECT_SUBSCRIPTION_BY_CHAT = "SELECT * FROM subscription WHERE chat_id = ?";
    public static final String SELECT_SUBSCRIPTION_BY_CHAT_AND_WALL = "SELECT * FROM subscription WHERE chat_id = ? AND wall_domain = ?";
    public static final String UPDATE_SUBSCRIPTION = "UPDATE subscription SET chat_id = ?, wall_domain = ?, last_post_id = ? WHERE subscription_id = ?";
    public static final String DELETE_SUBSCRIPTION = "DELETE FROM subscription WHERE subscription_id = ?";

    /*
     * Wall queries
     */
    public static final String INSERT_WALL = "INSERT INTO wall (wall_domain, approved) VALUES (?, ?)";
    public static final String SELECT_LAST_WALL = "SELECT * FROM wall WHERE wall_id = last_insert_id()";
    public static final String SELECT_WALL_BY_ID = "SELECT * FROM wall WHERE wall_id = ?";
    public static final String SELECT_ALL_WALLS = "SELECT * FROM wall";
    public static final String SELECT_ALL_APPROVED = "SELECT * FROM wall WHERE approved = 1";
    public static final String SELECT_BY_DOMAIN = "SELECT * FROM wall WHERE wall_domain = ?";
    public static final String UPDATE_WALL = "UPDATE wall SET wall_domain = ?, approved = ?, popularity = ? WHERE wall_id = ?";
    public static final String DELETE_WALL = "DELETE FROM wall WHERE wall_id = ?";
}
