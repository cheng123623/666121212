package com.sky.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class CategoryPageQueryDTO implements Serializable {
    private Integer page;
    private Integer pageSize;
}
