package com.example.frontend.Controller;


import com.example.frontend.enity.BacSi;
import com.example.frontend.enity.Benh;
import com.example.frontend.enity.BenhNhan;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/benhnhan")
public class BenhNhanController {

    private RestTemplate rest = new RestTemplate();

    @GetMapping("/current")
    public String benhNhanForm(Model model)
    {
        List<BenhNhan> benhNhanList = Arrays.asList(rest.getForObject("http://localhost:8080/benhnhan", BenhNhan[].class));
        model.addAttribute("listBenhNhan",benhNhanList);
        return "benhnhan/dsBenhNhan";
    }

    @GetMapping("/create")
    public String createBenhNhan(Model model)
    {
        BenhNhan benhnhan = new BenhNhan();
        model.addAttribute("benhnhan",benhnhan);
        return "benhnhan/addBenhNhan";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("benhnhan") BenhNhan benhnhan, Errors errors)
    {
        System.out.println(errors.toString());
        if (errors.hasErrors())  return "benhnhan/addBenhNhan";
        rest.postForObject("http://localhost:8080/benhnhan", benhnhan, BenhNhan.class);
        //System.out.println(benhnhan.toString());
        return "redirect:/benhnhan/current";
    }

    @RequestMapping("/update")
    public String edit(@RequestParam("id") int id , Model model)
    {
        BenhNhan benhNhan = rest.getForObject("http://localhost:8080/benhnhan/{id}", BenhNhan.class,id);
        model.addAttribute("benhnhan",benhNhan);
        return "/benhnhan/editBenhNhan";
    }

    @PostMapping("/saveEdit")
    public String update(BenhNhan benhNhan)
    {
        System.out.println(benhNhan.getId());
        rest.put("http://localhost:8080/benhnhan/{id}",benhNhan,benhNhan.getId());
        return "redirect:/benhnhan/current";
    }


    @GetMapping("/delete")
    public String delete(@RequestParam("id") int id)
    {
        rest.delete("http://localhost:8080/benhnhan/{id}",id);
        return "redirect:/benhnhan/current";
    }

    @GetMapping("/search")
    public String search(@RequestParam("keyword") String keyword,Model model) {
        System.out.println("keyword"  + keyword);
        if (keyword.equals("")) return "redirect:/benhnhan/current";
        List<BenhNhan> benhNhanList = Arrays.asList(rest.getForObject("http://localhost:8080/benhnhan/search/{keyword}", BenhNhan[].class, keyword));
        model.addAttribute("listBenhNhan", benhNhanList);
        return "benhnhan/dsBenhNhan";
    }

}
