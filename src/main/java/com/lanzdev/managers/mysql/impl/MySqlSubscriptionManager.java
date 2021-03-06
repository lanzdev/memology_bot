package com.lanzdev.managers.mysql.impl;

import com.lanzdev.dao.entity.SubscriptionDao;
import com.lanzdev.dao.mysql.impl.MySqlSubscriptionDao;
import com.lanzdev.managers.entity.SubscriptionManager;
import com.lanzdev.domain.Subscription;

import java.util.List;

public class MySqlSubscriptionManager implements SubscriptionManager {

    private SubscriptionDao dao = new MySqlSubscriptionDao();

    @Override
    public void add(Subscription object) {
        dao.create(object);
    }

    @Override
    public void update(Subscription object) {
        dao.update(object);
    }

    @Override
    public Subscription getById(Integer id) {
        return dao.get(id);
    }

    @Override
    public List<Subscription> getAll( ) {
        return dao.getAll();
    }

    @Override
    public List<Subscription> getByChatId(Long chatId) {
        return dao.getByChat(chatId);
    }

    @Override
    public Subscription getByChatAndWall(Long chatId, String wallDomain) {
        return dao.getByChatAndWall(chatId, wallDomain);
    }

    @Override
    public void delete(Subscription object) {
        dao.delete(object);
    }
}
