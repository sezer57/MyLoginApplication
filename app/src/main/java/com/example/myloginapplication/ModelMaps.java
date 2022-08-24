package com.example.myloginapplication;

public class ModelMaps {
    String name;
    Double longtitude,latitude;
    Integer price;
    Integer id;

    public ModelMaps(Integer id,String name, Double longtitude, Double latitude, Integer price) {
        this.id =id;
        this.name = name;
        this.longtitude = longtitude;
        this.latitude = latitude;
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(Double longtitude) {
        this.longtitude = longtitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}