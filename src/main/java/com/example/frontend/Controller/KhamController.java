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
    public String saveKham(Kham kham, @RequestParam("idYta") String idYta, @RequestParam("idThuoc") String idThuoc, @RequestParam("soLuong") String soLuong) {
        System.out.println("idYta " + idYta);
        System.out.println("idThuoc " + idThuoc);
        System.out.println("soLuong " + soLuong);

        List<Yta> ytaList = new ArrayList<>();
        for(String i: idYta.split(",") ) {
            Yta yTa = rest.getForObject(url+ "/yta/{id}", Yta.class, i);
            ytaList.add(yTa);
        }
        Kham a = rest.postForObject(url+"/kham", kham, Kham.class);

        //t???o c??c h??? tr??? c???a y t??
        for(int i = 0; i< ytaList.size() ; i++) {
            HoTro hoTro = new HoTro();
            Yta yta1 = ytaList.get(i);
            hoTro.setKham(a);
            hoTro.setYta(yta1);
            rest.postForObject(url + "/hotro",hoTro , HoTro.class);
        }

        //l???y ra list c??c s??? l?????ng
        List<Integer> soLuongThuoc = new ArrayList<>();
        for(String i: soLuong.split(",")) {
            if(!i.equals("")) {
                soLuongThuoc.add(Integer.valueOf(i));
            }
        }


        //l???y ra list thu???c
        List<Thuoc> listThuoc = new ArrayList<>();
        for(String i: idThuoc.split(",")) {
            if(!i.equals("") && !i.equals("a")) {
                listThuoc.add(rest.getForObject(url+"/thuoc/get-thuoc/{id}",Thuoc.class,i));
            }
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

        // truong hop x??a h???t thu??c, ???? x??a kh??ng c???n add th??m thu???c
        if( idThuoc.length() == 1) return "redirect:/kham/";
        for(String i: idThuoc.split(",")) {
            if(!i.equals("") && !i.equals("a")) {
                listThuoc.add(rest.getForObject(url+"/thuoc/get-thuoc/{id}",Thuoc.class,i));
            }
        }
        List<Integer> soLuongThuoc = new ArrayList<>();
        for(String i: soLuong.split(",")) {
            if(!i.equals("")) {
                soLuongThuoc.add(Integer.valueOf(i));
            }
        }

        System.out.println(a.getId());
        //c???p nh???t ????n thu???c :>
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
        keyword = keyword.trim();
        if (keyword.equals("")) return "redirect:/kham/";
        List<Kham> listKham = Arrays.asList(rest.getForObject(url+"/kham/bn/{id}",Kham[].class, keyword));
        model.addAttribute("listKham", listKham);
        return "kham/dsKham";
    }

}
