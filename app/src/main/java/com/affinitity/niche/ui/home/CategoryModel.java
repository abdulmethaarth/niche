package com.affinitity.niche.ui.home;

public class CategoryModel {
    private String categoryID;
    private  String categoryName;
    private String categoryPic;

    public CategoryModel(String categoryID, String categoryName, String categoryPic) {
        this.categoryID = categoryID;
        this.categoryName = categoryName;
        this.categoryPic = categoryPic;
    }

    public String  getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String  getCategoryPic() {
        return categoryPic;
    }

    public void setCategoryPic(String categoryPic) {
        this.categoryPic = categoryPic;
    }
}

