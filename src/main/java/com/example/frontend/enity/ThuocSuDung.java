package com.example.frontend.enity;

import lombok.Data;

@Data
public class ThuocSuDung {
    private int id;
    private int soLuong;
    Thuoc thuoc;
    DonThuoc donThuoc;
}
