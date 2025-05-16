package com.bbva.exchange.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Data
public class AuthRequest {
    private String username;
    private String password;
}
