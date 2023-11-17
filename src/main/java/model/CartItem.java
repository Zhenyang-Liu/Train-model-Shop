package model;

public class CartItem {
    private int itemID;
    private Product item;
    private int quantity;

    public CartItem(Product item, int quantity) {
        this.item =  item;
        this.quantity = quantity;
    }

    public CartItem(int itemID, Product item, int quantity) {
        this.itemID = itemID;
        this.item =  item;
        this.quantity = quantity;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public Product getItem() {
        return item;
    }
    
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
