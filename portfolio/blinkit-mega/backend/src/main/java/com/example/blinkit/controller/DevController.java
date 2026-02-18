package com.example.blinkit.controller;

import com.example.blinkit.config.SeedMapStore;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dev")
@RequiredArgsConstructor
public class DevController {
  private final SeedMapStore seedMapStore;

  @GetMapping("/seed-map")
  public Map<String, Object> seedMap() {
    return seedMapStore.snapshot();
  }
}

