package com.agh.webfluxmongo.dto;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "products")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ProductDto {
    @Id
    private String id;

    private String name;

    private String description;

    private Date createdAt;
}
