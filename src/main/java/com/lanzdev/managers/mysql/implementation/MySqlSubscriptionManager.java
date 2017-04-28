package com.lanzdev.managers.mysql.implementation;

import com.lanzdev.dao.entity.SubscriptionDao;
import com.lanzdev.dao.mysql.implementation.MySqlSubscriptionDao;
import com.lanzdev.managers.entity.SubscriptionManager;
import com.lanzdev.model.entity.Subscription;

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
    public List<Subscription> getByChat(Long chatId) {
        return dao.getByChat(chatId);
    }

    @Override
    public void delete(Subscription object) {
        dao.delete(object);
    }
}
