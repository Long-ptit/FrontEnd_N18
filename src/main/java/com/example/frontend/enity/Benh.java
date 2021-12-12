package com.example.frontend.enity;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class Benh {

    private long id;
    @NotBlank(message = "Vui lòng nhập đủ thông tin")
    private String ten;
    @NotBlank(message = "Vui lòng nhập đủ thông tin")
    private String moTa;
}
