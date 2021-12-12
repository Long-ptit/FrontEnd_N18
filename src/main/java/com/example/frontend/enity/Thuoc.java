package com.example.frontend.enity;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class Thuoc {
    private long id;
    @Min(value = 0, message = "Giá phải lớn hơn không")
    @NotNull(message = "Không được để trống")
    private Integer gia;
   @NotBlank(message = "Không được để trống")
    private String ten;
    @NotBlank(message = "Thiếu Tên Loại Thuốc")
    private String loaiThuoc;
    @NotBlank(message = "Thiếu công dụng")
    private String congDung;
}
