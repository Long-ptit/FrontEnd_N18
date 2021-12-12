package com.example.frontend.enity;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class Yta {
        private int id;
        @NotNull(message = "Vui lòng nhập đủ")
        private Integer ytaThamNien;
        @NotBlank(message = "Vui lòng nhập đủ")
        private String ytaCMT;
        @NotBlank(message = "Vui lòng nhập đủ")
        private String ytaTen;
        @NotBlank(message = "Vui lòng nhập đủ")
        private String ytaSDT;
        @NotBlank(message = "Vui lòng nhập đủ")
        private String ytaDiaChi;
        @NotBlank(message = "Vui lòng nhập đủ")
        private String ytaNgaySinh;

        public String toString() {
                String res = "Ten: "+ ytaTen + " DiaChi: " + ytaDiaChi + "cmt:"+ ytaCMT + "Ns"+ ytaNgaySinh + "std" + ytaSDT + "tn" +ytaThamNien;
                return res;
        }
}
