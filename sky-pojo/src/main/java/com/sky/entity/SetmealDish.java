package com.sky.entity;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class SetmealDish implements Serializable {
    private Long id;
    private Long setmealId;
    private Long dishId;
    private String name;
    private BigDecimal price;
    private Integer copies;
}
