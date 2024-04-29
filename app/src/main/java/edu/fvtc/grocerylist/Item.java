package edu.fvtc.grocerylist;

import android.graphics.Bitmap;

public class Item {
    private int id;
    private String description;
    private int isOnShoppingList;
    private int isInCart;
    private String owner;
    private double latitude;
    private double longitude;

    private int imgId;
    private Bitmap photo;

    public Item() {
        this.id = -1;
        this.description = "New Item";
        this.isOnShoppingList = 0;
        this.isInCart = 0;
        this.imgId = 0;
        this.owner = "";
        this.latitude = 0;
        this.longitude = 0;
    }

    public Item(String description, int isOnShoppingList, int isInCart) {
        this.id = id;
        this.description = description;
        this.isOnShoppingList = isOnShoppingList;
        this.isInCart = isInCart;
    }


    public Item(int id, String description, int isOnShoppingList, int isInCart, String owner, double latitude, double longitude, int imgId ) {
        this.id = id;
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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }


    // API
    public Bitmap getPhoto() {
        return photo;
    }
    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }



    public void setControlText(int controlId, String value)
    {
        if(controlId == R.id.etName)
        {
            this.setDescription(value);
        }

    }



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
