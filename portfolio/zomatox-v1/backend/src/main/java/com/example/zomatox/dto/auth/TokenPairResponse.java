package com.example.zomatox.dto.auth;

import lombok.Value;

@Value
public class TokenPairResponse {
  String accessToken;
  String refreshToken;
  AuthUserProfile user;
}
