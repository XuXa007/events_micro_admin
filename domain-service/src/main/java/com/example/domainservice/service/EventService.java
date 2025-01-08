package com.example.domainservice.service;

import com.example.domainservice.entity.EventEntity;

import java.util.List;

public interface EventService {
    EventEntity getEvent(Long id);
    List<EventEntity> getAllEvents();
    EventEntity createEvent(EventEntity event);
    EventEntity updateEvent(Long id, EventEntity event);
    void deleteEvent(Long id);

    Long saveEvent(EventEntity event);
}
