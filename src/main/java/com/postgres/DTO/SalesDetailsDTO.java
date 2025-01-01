package com.postgres.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalesDetailsDTO {
    private Long sale;
    private Long product;
    private Integer quantity;
}
