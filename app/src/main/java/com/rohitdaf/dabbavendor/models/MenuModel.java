package com.rohitdaf.dabbavendor.models;

import android.graphics.Bitmap;
import android.media.Image;

public class MenuModel {
    Bitmap menuImage;
    String menuSalad,menuMeal,menuFoodMore,menuFoodDesert,menuExtra;

    public MenuModel( String menuSalad, String menuMeal, String menuFoodMore, String menuFoodDesert, String menuExtra) {

        this.menuSalad = menuSalad;
        this.menuMeal = menuMeal;
        this.menuFoodMore = menuFoodMore;
        this.menuFoodDesert = menuFoodDesert;
        this.menuExtra = menuExtra;
    }

    public Bitmap getMenuImage() {
        return menuImage;
    }

    public void setMenuImage(Bitmap menuImage) {
        this.menuImage = menuImage;
    }

    public String getMenuSalad() {
        return menuSalad;
    }

    public void setMenuSalad(String menuSalad) {
        this.menuSalad = menuSalad;
    }

    public String getMenuMeal() {
        return menuMeal;
    }

    public void setMenuMeal(String menuMeal) {
        this.menuMeal = menuMeal;
    }

    public String getMenuFoodMore() {
        return menuFoodMore;
    }

    public void setMenuFoodMore(String menuFoodMore) {
        this.menuFoodMore = menuFoodMore;
    }

    public String getMenuFoodDesert() {
        return menuFoodDesert;
    }

    public void setMenuFoodDesert(String menuFoodDesert) {
        this.menuFoodDesert = menuFoodDesert;
    }

    public String getMenuExtra() {
        return menuExtra;
    }

    public void setMenuExtra(String menuExtra) {
        this.menuExtra = menuExtra;
    }
}
