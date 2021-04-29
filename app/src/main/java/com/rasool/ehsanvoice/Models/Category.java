package com.rasool.ehsanvoice.Models;

import com.rasool.ehsanvoice.Constants;

import java.io.Serializable;
import java.util.Random;

public class Category implements Serializable {
    int id;
    String CategoriesName;
    String CategoriesImage;


    public Category(String categoriesName) {
        this.CategoriesName = categoriesName;
        Random rand = new Random();
        int resID = rand.nextInt(10);
        CategoriesImage = String.valueOf(Constants.drawableIcons[resID]);
//        this.CategoriesImage = categoriesImage;

    }

    public Category(String categoriesImage, String categoriesName) {
        this.CategoriesImage = categoriesImage;
        this.CategoriesName = categoriesName;

    }

    public Category(int id, String categoriesImage, String categoriesName) {
        this.CategoriesImage = categoriesImage;
        this.CategoriesName = categoriesName;
        this.id=id;
    }

    public Category(int id, String categoriesName) {
        this.id=id;
        this.CategoriesName=categoriesName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCategoriesName(String categoriesName) {
        CategoriesName = categoriesName;
    }

    public void setCategoriesImage(String categoriesImage) {
        CategoriesImage = categoriesImage;
    }

    public Category(int id) {
        this.id = id;
    }

    public String getCategoriesName() {
        return CategoriesName;
    }

    public String getCategoriesImage()
    {
        return CategoriesImage;
    }

    public int getId() {
        return id;
    }
}
