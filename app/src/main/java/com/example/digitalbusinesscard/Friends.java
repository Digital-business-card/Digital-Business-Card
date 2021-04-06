package com.example.digitalbusinesscard;

public class Friends {
    private String username,description,phone,email,whatsapp,address,image,Uid;

    public Friends() {
    }

    public Friends(String username, String description, String phone, String email, String whatsapp, String address, String image, String uid) {
        this.username = username;
        this.description = description;
        this.phone = phone;
        this.email = email;
        this.whatsapp = whatsapp;
        this.address = address;
        this.image = image;
        this.Uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
        this.Uid = uid;
    }
}
