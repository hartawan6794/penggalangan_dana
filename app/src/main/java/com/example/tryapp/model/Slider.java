package com.example.tryapp.model;

public class Slider {

    // image url is used to
    // store the url of image
    private String imgUrl;

    // Constructor method.
    public Slider(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    // Getter method
    public String getImgUrl() {
        return imgUrl;
    }

    // Setter method
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}