package edu.fvtc.grocerylist;

public class Item {
    private int Id;
    private String Description;
    private int IsOnShoppingList;
    private int IsInCart;

    public Item() {
        this.Description = "";
        this.IsOnShoppingList = 0;
        this.IsInCart = 0;
    }

    public Item(String description, int isOnShoppingList, int isInCart) {
        this.Description = description;
        this.IsOnShoppingList = isOnShoppingList;
        this.IsInCart = isInCart;
    }

    // Getters and setters
    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }
    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        this.Description = description;
    }

    public int getIsOnShoppingList() {
        return IsOnShoppingList;
    }

    public void setIsOnShoppingList(int isOnShoppingList) {
        this.IsOnShoppingList = isOnShoppingList;
    }

    public int getIsInCart() {
        return IsInCart;
    }

    public void setIsInCart(int isInCart) {
        this.IsInCart = isInCart;
    }
}
