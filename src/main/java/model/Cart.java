package model;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;

public class Cart {
    private int cartID;
    private final int userID;
    private final Timestamp creationDate;
    private ArrayList<CartItem> itemList;

    public Cart(int userID) {
        this.userID = userID;
        this.creationDate = Timestamp.from(Instant.now());
        this.itemList = new ArrayList<CartItem>();
    }

    public Cart(int cartID, int userID, Timestamp creationDate, ArrayList<CartItem> itemList) {
        this.cartID = cartID;
        this.userID = userID;
        this.creationDate = creationDate;
        this.itemList = itemList;
    }

    public int getCartID(){
        return cartID;
    }

    public void setCartID(int cartID) {
        this.cartID = cartID;
    }

    public int getUserID(){
        return userID;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public String getFormattedCreationDate() {
        return formatTimestamp(creationDate);
    }

        private String formatTimestamp(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(timestamp);
    }

    public ArrayList<CartItem> getCartItems() {
        return itemList;
    }

    public void setCartItems(ArrayList<CartItem> itemList) {
        this.itemList = itemList;
    }
}
