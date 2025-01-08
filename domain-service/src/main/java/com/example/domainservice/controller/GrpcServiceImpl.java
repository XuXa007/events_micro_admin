package com.example.domainservice.controller;

import com.example.domainservice.entity.EventEntity;
import com.example.domainservice.rabbit.RabbitMqSender;
import com.example.domainservice.service.EventService;
import com.example.grpc.EventServiceGrpc;
import com.example.grpc.EventServiceProto;
import io.grpc.Status;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@GrpcService
public class GrpcServiceImpl extends EventServiceGrpc.EventServiceImplBase {
    private final EventService eventService;
    private final RabbitMqSender rabbitMqSender;
    private static final Logger logger = LoggerFactory.getLogger(GrpcServiceImpl.class);


    public GrpcServiceImpl(EventService eventService, RabbitMqSender rabbitMqSender) {
        this.eventService = eventService;
        this.rabbitMqSender = rabbitMqSender;
    }


    @Override
    public void getEvent(EventServiceProto.GetEventRequest request,
                         io.grpc.stub.StreamObserver<EventServiceProto.EventResponse> responseObserver) {
        EventEntity event = eventService.getEvent(request.getId());
        if (event == null) {
            logger.warn("Событие с ID={} не найдено", request.getId());
            responseObserver.onError(Status.NOT_FOUND
                    .withDescription("Event not found")
                    .asRuntimeException());
            return;
        }

        EventServiceProto.EventResponse response = EventServiceProto.EventResponse.newBuilder()
                .setId(event.getId())
                .setName(event.getName())
                .setLocation(event.getLocation())
                .setDate(event.getDate())
                .build();
        logger.info("Событие найдено: ID={}", event.getId());
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void createEvent(EventServiceProto.CreateEventRequest request,
                            io.grpc.stub.StreamObserver<EventServiceProto.CreateEventResponse> responseObserver) {
        EventEntity event = new EventEntity(null, request.getName(), request.getDate(), request.getLocation());
        rabbitMqSender.sendMessage("domain.event_create_queue", event);
        logger.info("Событие отправлено в очередь на создание: name={}", event.getName());

        EventServiceProto.CreateEventResponse response = EventServiceProto.CreateEventResponse.newBuilder()
                .setId(0L) // Временный ID
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateEvent(EventServiceProto.UpdateEventRequest request,
                            io.grpc.stub.StreamObserver<EventServiceProto.UpdateEventResponse> responseObserver) {
        EventEntity event = new EventEntity(request.getId(), request.getName(), request.getDate(), request.getLocation());
        rabbitMqSender.sendMessage("domain.event_update_queue", event);
        logger.info("Событие отправлено в очередь на обновление: ID={}", event.getId());

        EventServiceProto.UpdateEventResponse response = EventServiceProto.UpdateEventResponse.newBuilder()
                .setSuccess(true)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void deleteEvent(EventServiceProto.DeleteEventRequest request,
                            io.grpc.stub.StreamObserver<EventServiceProto.DeleteEventResponse> responseObserver) {
        rabbitMqSender.sendMessage("domain.event_delete_queue", request.getId());
        logger.info("Событие отправлено в очередь на удаление: ID={}", request.getId());

        EventServiceProto.DeleteEventResponse response = EventServiceProto.DeleteEventResponse.newBuilder()
                .setSuccess(true)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}