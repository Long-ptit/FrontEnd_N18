package com.example.frontend.enity;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class BenhNhan {
    private int id;
    @NotBlank(message = "Vui lòng nhập đủ thông tin")
    private String ten;
    @NotBlank(message = "Vui lòng nhập đủ thông tin")
    private String ngaySinh;
    @NotBlank(message = "Vui lòng nhập đủ thông tin")
    private String diaChi;
    @NotBlank(message = "Vui lòng nhập đủ thông tin")
    private String sdt;
}
