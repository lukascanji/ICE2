package ca.gbc.productservice.dto;

import ca.gbc.productservice.serialization.BigDecimalSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;


import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
//public class ProductResponse {
//    private String id;
//    private String name;
//    private String description;
//    private BigDecimal price;
//
//}

public class ProductResponse {
    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal price;

    // Getters and setters...
}
