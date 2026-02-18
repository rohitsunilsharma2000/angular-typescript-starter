package com.example.zomatox.controller;

import com.example.zomatox.dto.addresses.AddressUpsertRequest;
import com.example.zomatox.dto.users.AddressResponse;
import com.example.zomatox.entity.User;
import com.example.zomatox.service.AddressBookService;
import com.example.zomatox.service.UserService;
import com.example.zomatox.util.RequestContext;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressController {
  private final UserService userService;
  private final AddressBookService addressBookService;

  @PostMapping
  public AddressResponse create(@RequestHeader(value = "X-User-Id", required = false) String userId,
                                @Valid @RequestBody AddressUpsertRequest req) {
    User user = RequestContext.requireUser(userService, userId);
    return AddressResponse.from(addressBookService.create(user, req));
  }

  @GetMapping
  public List<AddressResponse> list(@RequestHeader(value = "X-User-Id", required = false) String userId) {
    User user = RequestContext.requireUser(userService, userId);
    return addressBookService.list(user).stream().map(AddressResponse::from).toList();
  }

  @PutMapping("/{id}")
  public AddressResponse update(@RequestHeader(value = "X-User-Id", required = false) String userId,
                                @PathVariable("id") Long id,
                                @Valid @RequestBody AddressUpsertRequest req) {
    User user = RequestContext.requireUser(userService, userId);
    return AddressResponse.from(addressBookService.update(user, id, req));
  }

  @DeleteMapping("/{id}")
  public void delete(@RequestHeader(value = "X-User-Id", required = false) String userId,
                     @PathVariable("id") Long id) {
    User user = RequestContext.requireUser(userService, userId);
    addressBookService.delete(user, id);
  }
}
