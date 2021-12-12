package com.example.frontend.enity;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
public class BacSi  {
    private int id;
    @NotNull(message = "Không được để null")
    private Integer bacsyThamNien;
    @NotBlank(message = "Vui lòng nhập đủ")
    private String bacsyCMT;
    @NotBlank(message = "Vui lòng nhập đủ")
    private String bacsyTen;
    @NotBlank(message = "Vui lòng nhập đủ")
    private String bacsySDT;
    @NotBlank(message = "Vui lòng nhập đủ")
    private String bacsyDiaChi;
    @NotBlank(message = "Vui lòng nhập đủ")
    private String bacsyNgaySinh;
    @NotBlank(message = "Vui lòng nhập đủ")
    private String bacsyBacNghe;
    @NotBlank(message = "Vui lòng nhập đủ")
    private String bacsyTrinhDoDaoTao;
    @NotBlank(message = "Vui lòng nhập đủ")
    private String bacsyChuyenMon;
}
