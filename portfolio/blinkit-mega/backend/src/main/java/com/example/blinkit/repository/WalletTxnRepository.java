package com.example.blinkit.repository;

import com.example.blinkit.entity.WalletTxn;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletTxnRepository extends JpaRepository<WalletTxn, Long> {}
