package com.tamirdunyasi.tamircimcebimdedemo;

import java.util.List;

public class Company extends User {

    protected List<String> category;
    protected int point;
    protected String registrationNo;
    protected String taxIdNo;
    protected String taxOffice;
    protected String title;

    public Company(String address, String email, String name, String password, String phone, String type, List<String> category,
                   String registrationNo, String taxIdNo, String taxOffice, String title){
        this.address = address;
        this.email = email;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.type = type;
        this.category = category;
        this.registrationNo = registrationNo;
        this.taxIdNo = taxIdNo;
        this.taxOffice = taxOffice;
        this.title = title;
        this.point = 0;
    }

    public Company(String id, String address, String email, String name, String password, String phone, String type, List<String> category,
                   String registrationNo, String taxIdNo, String taxOffice, String title){
        this.id = id;
        this.address = address;
        this.email = email;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.type = type;
        this.category = category;
        this.registrationNo = registrationNo;
        this.taxIdNo = taxIdNo;
        this.taxOffice = taxOffice;
        this.title = title;
        this.point = 0;
    }

    public List<String> getCategory() {
        return category;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public String getRegistrationNo() {
        return registrationNo;
    }

    public void setRegistrationNo(String registrationNo) {
        this.registrationNo = registrationNo;
    }

    public String getTaxIdNo() {
        return taxIdNo;
    }

    public void setTaxIdNo(String taxIdNo) {
        this.taxIdNo = taxIdNo;
    }

    public String getTaxOffice() {
        return taxOffice;
    }

    public void setTaxOffice(String taxOffice) {
        this.taxOffice = taxOffice;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
