package com.example.blinkit.config;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class SeedMapStore {
  private final Map<String, Object> data = new LinkedHashMap<>();

  public synchronized void put(String key, Object value) {
    data.put(key, value);
  }

  public synchronized Map<String, Object> snapshot() {
    return new LinkedHashMap<>(data);
  }
}

