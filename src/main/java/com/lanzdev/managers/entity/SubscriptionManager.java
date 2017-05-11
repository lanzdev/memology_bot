package com.lanzdev.managers.entity;

import com.lanzdev.managers.Manager;
import com.lanzdev.domain.Subscription;

import java.util.List;

public interface SubscriptionManager extends Manager<Subscription, Integer> {

    List<Subscription> getByChatId(Long chatId);

    Subscription getByChatAndWall(Long chatId, String wallDomain);
}
