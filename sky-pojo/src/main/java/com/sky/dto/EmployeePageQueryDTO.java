package com.sky.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class EmployeePageQueryDTO implements Serializable {
    private String name;
    private Integer page;
    private Integer pageSize;
}
