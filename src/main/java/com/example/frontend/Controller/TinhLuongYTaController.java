package com.example.frontend.Controller;

import com.example.frontend.enity.HoTro;
import com.example.frontend.enity.LuongYTa;
import com.example.frontend.enity.Yta;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/tinhluong/yta")
public class TinhLuongYTaController {
    private RestTemplate rest = new RestTemplate();
    String url = "http://localhost:8080";


    @GetMapping
    public String getDsLuongYta(Model model) {
        //List<Yta> ytaList = Arrays.asList(rest.getForObject(url+"/tl/yta/2021-12", Yta[].class));
        List<LuongYTa> luongYTaList = new ArrayList<>();
        model.addAttribute("luongYTaList", luongYTaList);
        model.addAttribute("keyword", "");
        return "yta/tlYTa";
    }

    @GetMapping("/result")
    public String showDsLuongYta(Model model, @RequestParam("keyword") String keyword) {
        List<LuongYTa> luongYTaList = Arrays.asList(rest.getForObject(url + "/hotro/tl/yta/{keyword}", LuongYTa[].class, keyword));
        model.addAttribute("luongYTaList", luongYTaList);
        model.addAttribute("keyword", keyword);
        return "yta/tlYTa";
    }

    @GetMapping("/detail")
    public String showDetailLuong(Model model, @RequestParam("keyword") String keyword,@RequestParam("id") String id ) {
        List<HoTro> listHoTro = Arrays.asList(rest.getForObject(url + "/hotro/tk/yta/{keyword}/{id}", HoTro[].class, keyword, id));
        Yta yta = rest.getForObject(url + "/yta/{id}", Yta.class, id);
        model.addAttribute("listHoTro", listHoTro);
        return "yta/tlYTaResult";
    }

}
