package com.bbva.exchange.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@Table("roles")
public class Roles {
    @Id
    private UUID id;
    private String name;
    private String description;
}