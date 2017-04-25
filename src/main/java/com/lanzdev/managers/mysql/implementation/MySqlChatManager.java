package com.lanzdev.managers.mysql.implementation;

import com.lanzdev.dao.entity.ChatDao;
import com.lanzdev.dao.mysql.implementation.MySqlChatDao;
import com.lanzdev.managers.entity.ChatManager;
import com.lanzdev.model.entity.Chat;

import java.util.List;

public class MySqlChatManager implements ChatManager {

    private ChatDao dao = new MySqlChatDao();

    @Override
    public Chat add(Chat object) {
        return dao.create(object);
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
}
