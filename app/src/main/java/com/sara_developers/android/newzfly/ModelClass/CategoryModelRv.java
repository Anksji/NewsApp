package com.sara_developers.android.newzfly.ModelClass;

public class CategoryModelRv {

    String CategoryUniqueid;
    String PrimeCategory;
    String SubCategory;

    public CategoryModelRv(String categoryUniqueid, String primeCategory, String subCategory) {
        CategoryUniqueid = categoryUniqueid;
        PrimeCategory = primeCategory;
        SubCategory = subCategory;
    }



    public String getCategoryUniqueid() {
        return CategoryUniqueid;
    }

    public String getPrimeCategory() {
        return PrimeCategory;
    }

    public String getSubCategory() {
        return SubCategory;
    }

}
