package com.bsolz.productservice.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Product {
    @Id
    private String id;
    private String description;
    private Integer prices;
}
