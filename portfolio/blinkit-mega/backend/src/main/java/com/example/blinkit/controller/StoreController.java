package com.example.blinkit.controller;

import com.example.blinkit.entity.Store;
import com.example.blinkit.service.CatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreController {
  private final CatalogService catalogService;

  @GetMapping("/resolve")
  public Store resolve(@RequestParam String pincode) {
    return catalogService.resolveStore(pincode);
  }
}
