package com.sara_developers.android.newzfly.AdminView.AddNewsCategory;

public class CategoryModel {

    String CategoryUniqueid;
    String PrimeCategory;
    String SubCategory;
    String ImgUrl;

    public String getCategoryUniqueid() {
        return CategoryUniqueid;
    }

    public String getPrimeCategory() {
        return PrimeCategory;
    }

    public String getSubCategory() {
        return SubCategory;
    }

    public String getImgUrl() {
        return ImgUrl;
    }

    public CategoryModel(String categoryUniqueid, String primeCategory, String subCategory, String imgUrl) {

        CategoryUniqueid = categoryUniqueid;
        PrimeCategory = primeCategory;
        SubCategory = subCategory;
        ImgUrl = imgUrl;
    }
}
