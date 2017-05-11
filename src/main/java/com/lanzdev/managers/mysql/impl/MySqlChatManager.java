package com.lanzdev.managers.mysql.impl;

import com.lanzdev.dao.entity.ChatDao;
import com.lanzdev.dao.mysql.impl.MySqlChatDao;
import com.lanzdev.managers.entity.ChatManager;
import com.lanzdev.domain.Chat;

import java.util.List;

public class MySqlChatManager implements ChatManager {

    private ChatDao dao = new MySqlChatDao();

    @Override
    public void add(Chat object) {
        dao.create(object);
    }

    @Override
    public void update(Chat object) {
        dao.update(object);
    }

    @Override
    public Chat getById(Long id) {
        return dao.get(id);
    }

    @Override
    public List<Chat> getAll( ) {
        return dao.getAll();
    }

    @Override
    public void delete(Chat object) {
        dao.delete(object);
    }
}
