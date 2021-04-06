package com.example.digitalbusinesscard;

public class GetUsers {
    private String fname,description,phone,email,whatsapp,address,image,Uid;

    public GetUsers() {
    }

    public GetUsers(String fname, String description, String phone, String email, String whatsapp, String address, String image, String uid) {
        this.fname = fname;
        this.description = description;
        this.phone = phone;
        this.email = email;
        this.whatsapp = whatsapp;
        this.address = address;
        this.image = image;
        Uid = uid;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWhatsapp() {
        return whatsapp;
    }

    public void setWhatsapp(String whatsapp) {
        this.whatsapp = whatsapp;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }
}
