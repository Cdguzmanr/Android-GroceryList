package edu.fvtc.grocerylist;

import android.graphics.Bitmap;

public class Item {
    private int id;
    private String description;
    private int isOnShoppingList;
    private int isInCart;
    private int imgId;

    public Item() {
        this.description = "";
        this.isOnShoppingList = 0;
        this.isInCart = 0;
        this.imgId = 0;
    }

    public Item(String description, int isOnShoppingList, int isInCart) {
        this.description = description;
        this.isOnShoppingList = isOnShoppingList;
        this.isInCart = isInCart;
    }

    public Item(String description, int isOnShoppingList, int isInCart, int imgId) {
        this.description = description;
        this.isOnShoppingList = isOnShoppingList;
        this.isInCart = isInCart;
        this.imgId = imgId;
    }

    // Getters and setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public int getIsOnShoppingList() {
        return isOnShoppingList;
    }
    public void setIsOnShoppingList(int isOnShoppingList) { this.isOnShoppingList = isOnShoppingList; }

    public int getIsInCart() {
        return isInCart;
    }

    public void setIsInCart(int isInCart) {
        this.isInCart = isInCart;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }


    // API
    public Bitmap getPhoto() {
        return photo;
    }
    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    private Bitmap photo;

/*    public void setControlText(int controlId, String value)
    {
        if(controlId == R.id.etName)
        {
            this.setName(value);
        } else if (controlId == R.id.etCity) {
            this.setCity(value);
        }
        else
        {
            this.setCellPhone(value);
        }

    }*/

    @Override
    public String toString()
    {
        return String.valueOf(id) + '|' +
                description + '|' +
                isOnShoppingList + '|' +
                isInCart + '|' +
                imgId;
    }
}
