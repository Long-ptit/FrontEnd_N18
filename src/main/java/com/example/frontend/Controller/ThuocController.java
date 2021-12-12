package com.example.frontend.Controller;

import com.example.frontend.enity.Thuoc;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/thuoc")
public class ThuocController {

    private RestTemplate rest = new RestTemplate();

    @GetMapping("/current")
    public String homeThuoc(Model model){
        List<Thuoc> list = Arrays.asList(rest.getForObject("http://localhost:8080/thuoc/list-thuoc", Thuoc[].class));
        model.addAttribute("listThuoc", list);
        return "thuoc/dsThuoc";
    }

    @GetMapping("/create")
    public String newThuoc(Model model){
        Thuoc thuoc = new Thuoc();
        model.addAttribute("thuoc", thuoc);
        return "thuoc/addThuoc";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("thuoc") Thuoc thuoc, Errors errors) {
        System.out.println(errors.toString());
            if (errors.hasErrors()) {
                return "thuoc/addThuoc";
            }
            rest.postForObject("http://localhost:8080/thuoc/add-thuoc",thuoc, Thuoc.class);
            return "redirect:/thuoc/current";
    }

    @GetMapping("/update")
    public String edit(@RequestParam("id") long id, Model model) {
        System.out.println("ID : ");
        Thuoc thuoc = rest.getForObject("http://localhost:8080/thuoc/get-thuoc/{id}", Thuoc.class, id);
        model.addAttribute("thuoc", thuoc);

        return "/thuoc/editThuoc";

    }

    @PostMapping("/saveEdit")
    public String update(@Valid @ModelAttribute("thuoc") Thuoc thuoc, Errors errors) {
        if (errors.hasErrors()) {
            return "/thuoc/editThuoc";

        }
        System.out.println(thuoc.getId());
        rest.put("http://localhost:8080/thuoc/update-thuoc/{id}", thuoc, thuoc.getId());
        return "redirect:/thuoc/current";
    }

    @GetMapping("/delete")
    public String deleteThuoc(@RequestParam("id") long id){
        System.out.println("Thanh Cong");
        //cập nhật lại đơn thuốc
        //gọi tất cả các đơn thuốc mà chứa thuốc đó, sửa lại

        rest.delete("http://localhost:8080/thuoc/delete-thuoc/{id}",id);
        return "redirect:/thuoc/current";
    }

    @GetMapping("/search")
    public String searchThuoc( @RequestParam("key") String key, Model model){
        key = key.trim().toLowerCase();
        if (key.equals("")) return "redirect:/thuoc/current";
        List<Thuoc> list = Arrays.asList(rest.getForObject("http://localhost:8080/thuoc/search-list/{key}",Thuoc[].class,key));
        model.addAttribute("listThuoc", list);
        return "thuoc/dsThuoc";
    }
}
