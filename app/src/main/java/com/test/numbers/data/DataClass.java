package com.test.numbers.data;


import java.util.ArrayList;

public class DataClass {
    private String name;
    private String image;
    public static ArrayList<DataClass> list = new ArrayList<>();

    public DataClass(String name, String image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
