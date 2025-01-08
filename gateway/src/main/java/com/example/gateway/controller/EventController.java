package com.example.gateway.controller;

import com.example.gateway.entity.EventDto;
import com.example.gateway.grpc.DomainServiceGrpcClient;
import com.example.grpc.EventServiceProto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/event")
@Tag(name = "Events", description = "API для управления событиями")
public class EventController {

    private final DomainServiceGrpcClient grpcClient;

    public EventController(DomainServiceGrpcClient grpcClient) {
        this.grpcClient = grpcClient;
    }

    @Cacheable(value = "events", key = "#id")
    @GetMapping("/{id}")
    @Operation(summary = "Получить событие по id", description = "Возвращает информацию о событии по его id")
    public EventDto getEvent(@PathVariable Long id) {
        return grpcClient.getEvent(id);
    }

    @PostMapping
    @Operation(summary = "Создать событие", description = "Создает новое событие")
    public ResponseEntity<String> createEvent(@RequestBody Map<String, String> data) {
        grpcClient.createEvent(data.get("name"), data.get("location"), data.get("date"));
        return ResponseEntity.ok("Added");
    }

    @PutMapping("/{id}")
    @CacheEvict(value = "events", key = "#id")
    @Operation(summary = "Обновление события", description = "Обновляет событие by id")
    public ResponseEntity<String> updateEvent(@PathVariable Long id, @RequestBody Map<String, String> data) {
        grpcClient.updateEvent(id, data.get("name"), data.get("location"), data.get("date"));
        return ResponseEntity.ok("Updated");
    }

    @DeleteMapping("/{id}")
    @CacheEvict(value = "events", key = "#id")
    @Operation(summary = "Удаление события", description = "Удаляет событие by id")
    public ResponseEntity<String> deleteEvent(@PathVariable Long id) {
        grpcClient.deleteEvent(id);
        return ResponseEntity.ok("Deleted");
    }
}
