package com.lanzdev.dao.entity;

import com.lanzdev.dao.GenericDao;
import com.lanzdev.model.entity.Subscription;

import java.util.List;

public interface SubscriptionDao extends GenericDao<Subscription, Integer> {

    List<Subscription> getSubscriptionsByChat(Long chatId);
}
