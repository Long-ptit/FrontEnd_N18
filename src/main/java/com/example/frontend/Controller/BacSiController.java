package com.example.frontend.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.*;
import com.example.frontend.enity.BacSi;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@Controller
@RequestMapping("/bacsi")
public class BacSiController {
    private RestTemplate rest = new RestTemplate();

    @GetMapping("/current")
    public String bacSiForm(Model model)
    {
        List<BacSi> bacSiList = Arrays.asList(rest.getForObject("http://localhost:8080/bacsi", BacSi[].class));
        model.addAttribute("listBacSi",bacSiList);
        return "bacsi/dsBacSi";
    }

    @GetMapping("/create")
    public String createBacSi(Model model)
    {
        BacSi bacsi = new BacSi();
        model.addAttribute("bacsi",bacsi);
        return "bacsi/addBacSi";
    }

    @PostMapping("/save")
    public String save( @Valid @ModelAttribute("bacsi") BacSi bacsi, Errors errors)
    {
        System.out.println("e roo"+errors.toString());
        if (errors.hasErrors()){
            return "bacsi/addBacSi";
        }
        rest.postForObject("http://localhost:8080/bacsi", bacsi, BacSi.class);
        System.out.println(bacsi.toString());
        return "redirect:/bacsi/current";
    }

    @RequestMapping("/update")
    public String edit(@RequestParam("id") int id ,Model model)
    {
        BacSi bacsi = rest.getForObject("http://localhost:8080/bacsi/{id}", BacSi.class,id);
        model.addAttribute("bacsi",bacsi);
        return "/bacsi/editBacSi";
    }

    @PostMapping("/saveEdit")
    public String update(BacSi bacSi)
    {
        System.out.println(bacSi.getId());
        rest.put("http://localhost:8080/bacsi/{id}",bacSi,bacSi.getId());
        return "redirect:/bacsi/current";
    }


    @GetMapping("/delete")
    public String delete(@RequestParam("id") int id)
    {
        rest.delete("http://localhost:8080/bacsi/{id}",id);
        return "redirect:/bacsi/current";
    }

    @GetMapping("/search")
    public String search(@RequestParam("keyword123") String keyword,Model model)
    {
        if (keyword.equals(""))  return "redirect:/bacsi/current";
        List<BacSi> bacSiList = Arrays.asList(rest.getForObject("http://localhost:8080/bacsi/search/{keyword}",BacSi[].class, keyword));
        model.addAttribute("listBacSi", bacSiList);
        return "/bacsi/dsBacSi";
    }


}
