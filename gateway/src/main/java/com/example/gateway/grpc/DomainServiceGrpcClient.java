package com.example.gateway.grpc;

import com.example.gateway.entity.EventDto;
import com.example.grpc.EventServiceGrpc;
import com.example.grpc.EventServiceProto;
import org.springframework.stereotype.Service;

@Service
public class DomainServiceGrpcClient {

    private final EventServiceGrpc.EventServiceBlockingStub blockingStub;

    public DomainServiceGrpcClient(EventServiceGrpc.EventServiceBlockingStub blockingStub) {
        this.blockingStub = blockingStub;
    }

    public EventDto getEvent(Long id) {
        EventServiceProto.GetEventRequest request = EventServiceProto.GetEventRequest.newBuilder()
                .setId(id)
                .build();

        EventServiceProto.EventResponse response = blockingStub.getEvent(request);

        return new EventDto(
                response.getId(),
                response.getName(),
                response.getLocation(),
                response.getDate()
        );
    }

    public Long createEvent(String name, String location, String date) {
        return blockingStub.createEvent(EventServiceProto.CreateEventRequest.newBuilder()
                .setName(name)
                .setLocation(location)
                .setDate(date)
                .build()).getId();
    }

    public void updateEvent(Long id, String name, String location, String date) {
        blockingStub.updateEvent(EventServiceProto.UpdateEventRequest.newBuilder()
                .setId(id)
                .setName(name)
                .setLocation(location)
                .setDate(date)
                .build());
    }

    public void deleteEvent(Long id) {
        blockingStub.deleteEvent(EventServiceProto.DeleteEventRequest.newBuilder().setId(id).build());
    }
}
