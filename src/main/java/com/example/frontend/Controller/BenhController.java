package com.example.frontend.Controller;

import com.example.frontend.enity.Benh;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

//import javax.validation.Valid;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/benh")
public class BenhController {
    private RestTemplate rest = new RestTemplate();

    @GetMapping("/current")
    public String homeBenh(Model model){
        List<Benh> list = Arrays.asList(rest.getForObject("http://localhost:8080/benh/list-benh", Benh[].class));
       model.addAttribute("listBenh", list);
        return "benh/dsBenh";
    }

    @GetMapping("/create")
    public String newBenh(Model model){
        Benh benh = new Benh();
        model.addAttribute("benh", benh);
        return "benh/addBenh";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("benh") Benh benh, Errors errors) {
        if(errors.hasErrors()) {
            return "benh/addBenh";
        }
            rest.postForObject("http://localhost:8080/benh/add-benh",benh, Benh.class);
            return "redirect:/benh/current";
    }

    @GetMapping("/update")
    public String edit(@RequestParam("id") long id, Model model) {
        System.out.println("ID : ");
        Benh benh = rest.getForObject("http://localhost:8080/benh/get-benh/{id}", Benh.class, id);
        model.addAttribute("benh", benh);

        return "/benh/editBenh";

    }

    @PostMapping("/saveEdit")
    public String update(Benh benh) {
        System.out.println(benh.getId());
        rest.put("http://localhost:8080/benh/update-benh/{id}", benh, benh.getId());
        return "redirect:/benh/current";
    }

    @GetMapping("/delete")
    public String deleteBenh(@RequestParam("id") long id){
        System.out.println("Thanh Cong");
        rest.delete("http://localhost:8080/benh/delete-benh/{id}",id);
        return "redirect:/benh/current";
    }

    @GetMapping("/search")
    public String searchbenh( @RequestParam("key") String key, Model model){
        key = key.trim().toLowerCase();
        if (key.equals("")) return "redirect:/benh/current";
        List<Benh> list = Arrays.asList(rest.getForObject("http://localhost:8080/benh/search-list/{key}",Benh[].class,key));
        model.addAttribute("listBenh", list);
        return "benh/dsBenh";
    }
}
