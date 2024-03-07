package edu.fvtc.grocerylist;

public class Item {
    private String Description;
    private int IsOnShoppingList;
    private int IsInCart;

    public Item(String description, int isOnShoppingList, int isInCart) {
        this.Description = description;
        this.IsOnShoppingList = isOnShoppingList;
        this.IsInCart = isInCart;
    }

    // Getters and setters
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
