package com.bbva.exchange.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Table("users")
public class User {
    @Id
    private UUID id;
    private String username;
    private String password;
    private LocalDateTime createdAt;
}
