package com.sky.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class DishPageQueryDTO implements Serializable {
    private String name;
    private Integer page;
    private Integer pageSize;
    private Long categoryId;
    private Integer status;
}
