package com.lanzdev.managers.entity;

import com.lanzdev.managers.Manager;
import com.lanzdev.model.entity.Subscription;

import java.util.List;

public interface SubscriptionManager extends Manager<Subscription, Integer> {

    List<Subscription> getByChat(Long chatId);
}
