package com.example.domainservice.rabbit;

import com.example.domainservice.entity.EventEntity;
import com.example.domainservice.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqListener {
    private final EventService eventService;
    private static final Logger logger = LoggerFactory.getLogger(EventService.class);

    public RabbitMqListener(EventService eventService) {
        this.eventService = eventService;
    }

    @RabbitListener(queues = "domain.event_create_queue")
    public void create(EventEntity event) {
        EventEntity savedEvent = eventService.createEvent(event);
        logger.info("Создано новое событие: ID={}, name={}", savedEvent.getId(), savedEvent.getName());
    }

    @RabbitListener(queues = "domain.event_update_queue")
    public void update(EventEntity event) {
        EventEntity updatedEvent = eventService.updateEvent(event.getId(), event);
        if (updatedEvent != null) {
            logger.info("Событие обновлено: ID={}, name={}", updatedEvent.getId(), updatedEvent.getName());
        } else {
            logger.warn("Событие с ID={} не найдено для обновления", event.getId());
        }
    }

    @RabbitListener(queues = "domain.event_delete_queue")
    public void delete(Long id) {
        eventService.deleteEvent(id);
        logger.info("Событие удалено: ID={}", id);
    }
}