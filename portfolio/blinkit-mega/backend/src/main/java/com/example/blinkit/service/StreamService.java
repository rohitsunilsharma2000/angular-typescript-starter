package com.example.blinkit.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
public class StreamService {
  private final Map<Long, List<SseEmitter>> orderEmitters = new ConcurrentHashMap<>();
  private final List<SseEmitter> adminEmitters = new CopyOnWriteArrayList<>();

  public SseEmitter subscribeOrder(Long orderId) {
    SseEmitter emitter = new SseEmitter(0L);
    orderEmitters.computeIfAbsent(orderId, k -> new CopyOnWriteArrayList<>()).add(emitter);
    emitter.onCompletion(() -> orderEmitters.getOrDefault(orderId, List.of()).remove(emitter));
    emitter.onTimeout(() -> orderEmitters.getOrDefault(orderId, List.of()).remove(emitter));
    return emitter;
  }

  public SseEmitter subscribeAdmin() {
    SseEmitter emitter = new SseEmitter(0L);
    adminEmitters.add(emitter);
    emitter.onCompletion(() -> adminEmitters.remove(emitter));
    emitter.onTimeout(() -> adminEmitters.remove(emitter));
    return emitter;
  }

  public void publishOrder(Long orderId, String event, String payload) {
    for (SseEmitter emitter : orderEmitters.getOrDefault(orderId, List.of())) {
      try {
        emitter.send(SseEmitter.event().name(event).data(payload));
      } catch (IOException ignored) {
      }
    }
    for (SseEmitter emitter : adminEmitters) {
      try {
        emitter.send(SseEmitter.event().name(event).data(payload));
      } catch (IOException ignored) {
      }
    }
  }
}
