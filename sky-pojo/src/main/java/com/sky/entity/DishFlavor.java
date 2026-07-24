package com.sky.entity;

import lombok.Data;
import java.io.Serializable;

@Data
public class DishFlavor implements Serializable {
    private Long id;
    private Long dishId;
    private String name;
    private String value;
}
