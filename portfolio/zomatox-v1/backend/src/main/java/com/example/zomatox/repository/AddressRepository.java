package com.example.zomatox.repository;

import com.example.zomatox.entity.Address;
import com.example.zomatox.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
  List<Address> findByUser(User user);
}
