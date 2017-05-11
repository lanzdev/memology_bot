package com.lanzdev.dao.entity;

import com.lanzdev.dao.GenericDao;
import com.lanzdev.domain.Subscription;

import java.util.List;

public interface SubscriptionDao extends GenericDao<Subscription, Integer> {

    List<Subscription> getByChat(Long chatId);

    Subscription getByChatAndWall(Long chatId, String wallDomain);
}
