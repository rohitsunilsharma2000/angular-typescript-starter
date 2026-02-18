package com.example.zomatox.service;

import com.example.zomatox.dto.addresses.AddressUpsertRequest;
import com.example.zomatox.entity.Address;
import com.example.zomatox.entity.User;
import com.example.zomatox.exception.ApiException;
import com.example.zomatox.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AddressBookService {
  private final AddressRepository addressRepository;

  public Address create(User user, AddressUpsertRequest req) {
    return addressRepository.save(Address.builder()
      .user(user)
      .line1(req.getLine1())
      .city(req.getCity())
      .pincode(req.getPincode())
      .phone(req.getPhone())
      .build());
  }

  public List<Address> list(User user) {
    return addressRepository.findByUser(user);
  }

  public Address update(User user, Long id, AddressUpsertRequest req) {
    Address a = addressRepository.findById(id).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Address not found"));
    if (!a.getUser().getId().equals(user.getId())) throw new ApiException(HttpStatus.FORBIDDEN, "Not your address");
    a.setLine1(req.getLine1());
    a.setCity(req.getCity());
    a.setPincode(req.getPincode());
    a.setPhone(req.getPhone());
    return addressRepository.save(a);
  }

  public void delete(User user, Long id) {
    Address a = addressRepository.findById(id).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Address not found"));
    if (!a.getUser().getId().equals(user.getId())) throw new ApiException(HttpStatus.FORBIDDEN, "Not your address");
    addressRepository.delete(a);
  }
}
