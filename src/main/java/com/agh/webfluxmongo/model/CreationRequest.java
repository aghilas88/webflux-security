package com.agh.webfluxmongo.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreationRequest {
    private String name;
    private String description;
}
