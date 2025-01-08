package com.example.domainservice.service;

import com.example.domainservice.entity.EventEntity;
import com.example.domainservice.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;

    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public EventEntity getEvent(Long id) {
        return eventRepository.findById(id).orElse(null);
    }

    @Override
    public List<EventEntity> getAllEvents() {
        return eventRepository.findAll();
    }

    @Override
    public EventEntity createEvent(EventEntity event) {
        return eventRepository.save(event);
    }

    @Override
    public EventEntity updateEvent(Long id, EventEntity event) {
        EventEntity entity = eventRepository.findById(id).orElse(null);
        if (entity != null) {
            entity.setName(event.getName());
            entity.setDate(event.getDate());
            entity.setLocation(event.getLocation());
            return eventRepository.save(entity);
        }
        return null;
    }

    @Override
    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }

    @Override
    public Long saveEvent(EventEntity event) {
        return eventRepository.save(event).getId();
    }
}
