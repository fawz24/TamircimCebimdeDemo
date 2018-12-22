package com.tamirdunyasi.tamircimcebimdedemo;

public class Client extends User {
    protected String surname;

    public Client(String address, String email, String name, String password, String phone, String surname, int type){
        this.address = address;
        this.email = email;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.type = type;
        this.surname = surname;
    }

    public Client(String id, String address, String email, String name, String password, String phone, String surname, int type){
        this.id = id;
        this.address = address;
        this.email = email;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.type = type;
        this.surname = surname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}
