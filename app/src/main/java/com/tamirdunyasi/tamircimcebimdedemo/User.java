package com.tamirdunyasi.tamircimcebimdedemo;

public abstract class User {
    protected String id;
    protected String address;
    protected String email;
    protected String name;
    protected String password;
    protected String phone;
    protected int type;

//    public User(){
//
//    }

//    public User(String id, String address, String email, String name, String password, String phone, int type){
//        this.id = id;
//        this.address = address;
//        this.email = email;
//        this.name = name;
//        this.password = password;
//        this.phone = phone;
//        this.type = type;
//    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
