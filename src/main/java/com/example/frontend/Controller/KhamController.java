package com.example.frontend.Controller;

import com.example.frontend.enity.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/kham")
public class KhamController {
    RestTemplate rest = new RestTemplate();
    String url = "http://localhost:8080";
    @GetMapping
    public String getDsKham(Model model) {
        List<Kham> listKham = Arrays.asList(rest.getForObject(url+"/kham",Kham[].class));
        model.addAttribute("listKham", listKham);
        return "kham/dsKham";
    }

    @GetMapping("/create")
    public String addKham(Model model) {
        List<Yta> listYta = Arrays.asList(rest.getForObject(url + "/yta", Yta[].class));
        List<BacSi> listBacsi = Arrays.asList(rest.getForObject(url + "/bacsi",BacSi[].class));
        List<Benh> listBenh = Arrays.asList(rest.getForObject(url + "/benh/list-benh",Benh[].class));
        List<BenhNhan> listBenhNhan = Arrays.asList(rest.getForObject(url + "/benhnhan",BenhNhan[].class));
        List<Thuoc> listThuoc = Arrays.asList(rest.getForObject(url+"/thuoc/list-thuoc", Thuoc[].class));

        Kham kham = new Kham();
        model.addAttribute("kham", kham);
        model.addAttribute("listYta", listYta);
        model.addAttribute("listBacsi", listBacsi);
        model.addAttribute("listBenh", listBenh);
        model.addAttribute("listBenhNhan", listBenhNhan);
        model.addAttribute("listThuoc", listThuoc);
        return "kham/addKham";
    }

    @PostMapping("/save")
    public String saveKham(@Valid @ModelAttribute("kham") Kham kham, @RequestParam("idYta") String idYta, @RequestParam("idThuoc") String idThuoc, @RequestParam("soLuong") String soLuong, Errors errors) {
        if(errors.hasErrors()) return "kham/addKham";

        System.out.println("thuoc id " + idThuoc);//a,x,x
        System.out.println("soluong" +soLuong);


        List<Yta> ytaList = new ArrayList<>();
        for(String i: idYta.split(",") ) {
            Yta yTa = rest.getForObject(url+ "/yta/{id}", Yta.class, i);
            ytaList.add(yTa);
        }
        Kham a = rest.postForObject(url+"/kham", kham, Kham.class);
        for(int i = 0; i< ytaList.size() ; i++) {
            HoTro hoTro = new HoTro();
            Yta yta1 = ytaList.get(i);
            hoTro.setKham(a);
            hoTro.setYta(yta1);
            rest.postForObject(url + "/hotro",hoTro , HoTro.class);
        }
        List<Integer> soLuongThuoc = new ArrayList<>();
        List<String> listSoluong = Arrays.asList(soLuong.split(","));
        for (int i=0 ; i<listSoluong.size() ; i++) {
            if(!listSoluong.get(i).equals("")) {
                soLuongThuoc.add(Integer.valueOf(listSoluong.get(i)));
            }
        }

        List<Thuoc> listThuoc = new ArrayList<>();
//        if( idThuoc.length() == 1) return "redirect:/kham/";
//        if( soLuong.length() == 0) return "redirect:/kham/";
        //trừ đi kí tự check null
       if(idThuoc.length() != 1)  idThuoc = idThuoc.substring(0, idThuoc.length()-2);
        List<String> listIdThuocc = Arrays.asList(idThuoc.split(","));
        System.out.println("hehe"+listIdThuocc.get(0));
        List<String> listIdThuocc1 = new ArrayList<>();
        for(int i=0 ; i<listIdThuocc.size() ; i++) {
            if(!listIdThuocc.get(i).equals("") && !listIdThuocc.get(i).equals("a")) {
                System.out.println("vl"+listIdThuocc.get(i));
                System.out.println("add");
                listIdThuocc1.add((listIdThuocc.get(i)));
            }
        }
        for( int i=0 ; i<listIdThuocc1.size() ; i++) {
            listThuoc.add(rest.getForObject(url+"/thuoc/get-thuoc/{id}",Thuoc.class, listIdThuocc1.get(i)));
        }
        int tongTien = 0;
        for( int i=0 ; i<listThuoc.size() ; i++) {
            int gia = listThuoc.get(i).getGia()*soLuongThuoc.get(i);
            tongTien += gia;
        }

        System.out.println(a.getId());
        DonThuoc donThuoc = new DonThuoc();
        donThuoc.setTongTien(tongTien);
        donThuoc.setKham(a);
        DonThuoc d = rest.postForObject(url + "/donthuoc", donThuoc, DonThuoc.class);
        for(int i=0 ; i<listThuoc.size() ; i++) {
            ThuocSuDung thuocSuDung = new ThuocSuDung();
            thuocSuDung.setThuoc(listThuoc.get(i));
            thuocSuDung.setDonThuoc(d);
            thuocSuDung.setSoLuong(soLuongThuoc.get(i));
            rest.postForObject(url + "/thuocsd", thuocSuDung, ThuocSuDung.class);
        }


        return "redirect:/kham/";
    }

    @GetMapping("/edit")
    public String editKham(Model model, @RequestParam("id") String id) {

        List<Yta> listYta = Arrays.asList(rest.getForObject(url + "/yta", Yta[].class));
        List<BacSi> listBacsi = Arrays.asList(rest.getForObject(url + "/bacsi",BacSi[].class));
        List<Benh> listBenh = Arrays.asList(rest.getForObject(url + "/benh/list-benh",Benh[].class));
        List<BenhNhan> listBenhNhan = Arrays.asList(rest.getForObject(url + "/benhnhan",BenhNhan[].class));
        List<Thuoc> listThuoc = Arrays.asList(rest.getForObject(url+"/thuoc/list-thuoc", Thuoc[].class));
        Kham kham = rest.getForObject(url+"/kham/{id}", Kham.class, id);
        model.addAttribute("kham", kham);
        model.addAttribute("listYta", listYta);
        model.addAttribute("listBacsi", listBacsi);
        model.addAttribute("listBenh", listBenh);
        model.addAttribute("listBenhNhan", listBenhNhan);
        model.addAttribute("listThuoc", listThuoc);

        return "kham/editKham";
    }

    @PostMapping("/saveEdit")
    public String saveEditKham(Kham kham,@RequestParam("idYta") String idYta,@RequestParam("idThuoc") String idThuoc, @RequestParam("soLuong") String soLuong) {
        List<Integer> soLuongThuoc = new ArrayList<>();
        List<String> listSoluong = Arrays.asList(soLuong.split(","));
        for (int i=0 ; i<listSoluong.size() ; i++) {
            if(!listSoluong.get(i).equals("")) {
                soLuongThuoc.add(Integer.valueOf(listSoluong.get(i)));
            }
        }

        List<Yta> ytaList = new ArrayList<>();
        for(String i: idYta.split(",") ) {
            Yta yTa = rest.getForObject(url+ "/yta/{id}", Yta.class, i);
            ytaList.add(yTa);
        }
        Kham a = kham;
        rest.delete(url+"/hotro/delete/hotro-theo-id-kham/{id}", a.getId());
        rest.put(url+"/kham/{id}", kham,kham.getId());
        for(int i = 0; i< ytaList.size() ; i++) {
            HoTro hoTro = new HoTro();
            Yta yta1 = ytaList.get(i);
            hoTro.setKham(a);
            hoTro.setYta(yta1);
            rest.postForObject(url + "/hotro",hoTro , HoTro.class);
        }
        DonThuoc donThuoc1 = rest.getForObject(url + "/donthuoc/get-donthuoc-id-kham/{id}", DonThuoc.class, a.getId());
        rest.delete(url+"/thuocsd/deleteByDonThuoc/{id}", donThuoc1.getId());

        donThuoc1.setTongTien(0);
        List<Thuoc> listThuoc = new ArrayList<>();
        rest.put(url+"/donthuoc/{id}", donThuoc1, donThuoc1.getId());
        // truong hop xóa hết thuôc, đã xóa không cần add thêm thuốc
        if( idThuoc.length() == 1) return "redirect:/kham/";
        idThuoc = idThuoc.substring(0, idThuoc.length()-2);
        for(String i : idThuoc.split(",")) {
            listThuoc.add(rest.getForObject(url+"/thuoc/get-thuoc/{id}",Thuoc.class, Integer.valueOf(i)));
        }

        System.out.println(a.getId());
        //cập nhật đơn thuốc :>
        int tongTien = 0;
        for(int i=0 ; i<listThuoc.size() ; i++) {
            ThuocSuDung thuocSuDung = new ThuocSuDung();
            thuocSuDung.setThuoc(listThuoc.get(i));
            int gia = listThuoc.get(i).getGia()*soLuongThuoc.get(i);
            tongTien += gia;
            thuocSuDung.setDonThuoc(donThuoc1);
            thuocSuDung.setSoLuong(soLuongThuoc.get(i));
            rest.postForObject(url + "/thuocsd", thuocSuDung, ThuocSuDung.class);
        }

        donThuoc1.setTongTien(tongTien);
        rest.put(url+"/donthuoc/{id}", donThuoc1, donThuoc1.getId());


        return "redirect:/kham/";
    }

    @GetMapping("/delete")
    public String deleteKham(@RequestParam("id") String id) {
        rest.delete(url+"/kham/{id}",id);

        return "redirect:/kham/";
    }

    @GetMapping("/viewDonThuoc")
    public String viewDonThuoc(Model model,@RequestParam("id") String id) {
        DonThuoc donThuoc = rest.getForObject(url+"/donthuoc/get-donthuoc-id-kham/{id}", DonThuoc.class, id);
        List<ThuocSuDung> listThuoc;
        listThuoc = Arrays.asList(rest.getForObject(url+ "/thuocsd/thuoctronghoadon/{id}", ThuocSuDung[].class, donThuoc.getId()));
        if (listThuoc == null) {
            listThuoc = new ArrayList<>();
        }
        model.addAttribute(donThuoc);
        model.addAttribute(listThuoc);

        return "kham/detailDonThuoc";

    }

    @GetMapping("/viewYta")
    public  String viewYta(Model model, @RequestParam("id") String id) {
        List<HoTro> hoTroList = Arrays.asList(rest.getForObject(url +"/hotro/dsYtaFromKhamId/{id}", HoTro[].class, id));
        model.addAttribute("hoTroList", hoTroList);

        return "kham/detailYta";
    }

    @GetMapping("/search")
    public String searchKhamTheoIdNguoiDung(Model model,@RequestParam("keyword") String keyword) {
        List<Kham> listKham = Arrays.asList(rest.getForObject(url+"/kham/bn/{id}",Kham[].class, keyword));
        model.addAttribute("listKham", listKham);
        return "kham/dsKham";
    }

}
