package com.sara_developers.android.newzfly.Pojo;


public class NewsCategory {

    String CategoryName;
    int Imageid;
    String ImageLink;
    String CategoryType;

    public NewsCategory(String categoryName, int imageid, String imageLink, String categoryType) {
        CategoryName = categoryName;
        Imageid = imageid;
        ImageLink = imageLink;
        CategoryType = categoryType;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public int getImageid() {
        return Imageid;
    }

    public String getImageLink() {
        return ImageLink;
    }

    public String getCategoryType() {
        return CategoryType;
    }
}
