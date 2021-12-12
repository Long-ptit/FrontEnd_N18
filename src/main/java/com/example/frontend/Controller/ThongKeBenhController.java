package com.example.frontend.Controller;


import com.example.frontend.enity.Kham;
import com.example.frontend.enity.SoLanBenh;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RequestMapping("tk/benh")
@Controller
public class ThongKeBenhController {
    RestTemplate rest = new RestTemplate();
    String url = "http://localhost:8080";

    @GetMapping
    public String getHomeThongKeBenh(Model model) {
        List<SoLanBenh> listSoLan = new ArrayList<>();
        model.addAttribute("listSoLan", listSoLan);
        model.addAttribute("keyword", "");
        return "thongke/tkBenh";
    }

    @GetMapping("/search")
    public String getThongKeBenh(@RequestParam("keyword") String keyword ,Model model) {
       List<SoLanBenh> listSoLan = Arrays.asList(rest.getForObject(url+"/kham/tk/benh/solan/{keyword}", SoLanBenh[].class, keyword));
       model.addAttribute("listSoLan", listSoLan);
        Collections.sort(listSoLan, new Comparator<SoLanBenh>() {
            @Override
            public int compare(SoLanBenh o1, SoLanBenh o2) {
                return o2.getSoLan() - o1.getSoLan();
            }
        });
        model.addAttribute("keyword",keyword);

        return "thongke/tkBenh";
    }

    @GetMapping("/result")
    public String getDetailBenh(@RequestParam("id") String id,@RequestParam("keyword") String keyword, Model model) {
        System.out.println(keyword);
        keyword = keyword.trim();
        if (keyword.equals("")) return "redirect:tk/benh";
        List<Kham> listKham = Arrays.asList(rest.getForObject(url+"/kham/tk/benh/{keyword}/{id}", Kham[].class, keyword, id));
        model.addAttribute("listKham", listKham);
        return "thongke/tkBenhDetail";
    }


}
