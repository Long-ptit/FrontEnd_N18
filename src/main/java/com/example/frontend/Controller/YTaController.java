package com.example.frontend.Controller;

import com.example.frontend.enity.Yta;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/yta")
public class YTaController {
    private RestTemplate rest = new RestTemplate();

    @GetMapping("/current")
    public String yTaForm(Model model) {
        List<Yta> ytaList = Arrays.asList(rest.getForObject("http://localhost:8080/yta", Yta[].class));
        model.addAttribute("listYta", ytaList);
        return "yta/dsYTa";
    }

    @GetMapping("/create")
    public String createYTa(Model model) {
       // List<YTa> yTaList = Arrays.asList(rest.getForObject("http://localhost:8080/yta",YTa[].class));
       //model.addAttribute("listYta", yTaList);
        Yta yta = new Yta();
        model.addAttribute("yta", yta);
        return "yta/addYTa";
    }



    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("yta") Yta yta, Errors errors) {
        //yta.setYtaThamNien(5);
        if (errors.hasErrors()) {
            return "yta/addYTa";
        }
        rest.postForObject("http://localhost:8080/yta",yta, Yta.class);
        System.out.println(yta.toString());
        return "redirect:/yta/current";
    }

    @GetMapping("/update")
    public String edit(@RequestParam("id") String id, Model model) {

    Yta yTa =rest.getForObject("http://localhost:8080/yta/{id}", Yta.class,id);
    model.addAttribute("yta", yTa);
    return "/yta/editYTa";

    }

    @PostMapping("/saveEdit")
    public String update(Yta yTa) {
        System.out.println(yTa.getId());
        rest.put("http://localhost:8080/yta/{id}", yTa, yTa.getId());
        return "redirect:/yta/current";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("id") String id) {
        rest.delete("http://localhost:8080/yta/{id}", id);
        return "redirect:/yta/current";
    }

    @GetMapping("/search")
    public String search(@RequestParam("keywordabc") String keyword, Model model) {
        keyword = keyword.trim();
        if (keyword.equals(""))  return "redirect:/yta/current";
        List<Yta> ytaList = Arrays.asList(rest.getForObject("http://localhost:8080/yta/search/{keyword}", Yta[].class, keyword));
        model.addAttribute("listYta", ytaList);
        return "/yta/dsYTa";
    }











}
