package com.example.frontend.enity;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class Kham {

    private int id;
    private String datein;
    private String dateout;
    private String status;
    private String type;
    private Integer tongTien;
    BacSi bacSy;
    BenhNhan benhNhan;
    Benh benh;

}
