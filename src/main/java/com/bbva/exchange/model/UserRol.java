package com.bbva.exchange.model;

import lombok.Data;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@Table("user_roles")
public class UserRol {
    private UUID userId;
    private UUID roleId;
}