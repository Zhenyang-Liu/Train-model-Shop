package model;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Map;

public class Order {
    private int orderID;
    private final int userID;
    private final int addressID;
    private final Timestamp createTime;
    private Timestamp updateTime;
    private final double totalCost;
    private OrderStatus status;
    private final Map<Product,Integer> itemList;

    public interface OrderState {
        public void nextState(Order order);
    }

    public enum OrderStatus implements OrderState{
        PENDING("Pending") {
            @Override
            public void nextState(Order order) {
                order.setStatus(CONFIRMED);
            }
        },

        CONFIRMED("Confirmed") {
            @Override
            public void nextState(Order order) {
                order.setStatus(FULFILLED);
            }
        },

        FULFILLED("Fulfilled") {
            @Override
            public void nextState(Order order) {
            }
        };
    
        private final String status;
    
        OrderStatus(String status) {
            this.status = status;
        }
    
        public String getStatus() {
            return status;
        }

        public static OrderStatus fromName(String statusName) {
            for (OrderStatus status : OrderStatus.values()) {
                if (status.getStatus().equalsIgnoreCase(statusName)) {
                    return status;
                }
            }
            throw new IllegalArgumentException("Invalid Order Status classification: " + statusName);
        }
    }
    
    public Order(int orderID, int userID, int addressID, Timestamp createTime, Timestamp updateTime, double totalCost, String status, Map<Product, Integer> itemList) {
        this.orderID = orderID;
        this.userID = userID;
        this.addressID = addressID;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.totalCost = calculateTotalCost(itemList);
        this.status = OrderStatus.fromName(status);
        this.itemList = itemList;
    }

    public Order(int userID, int addressID, Map<Product, Integer> itemList) {
        this.userID = userID;
        this.addressID = addressID;
        this.createTime = Timestamp.from(Instant.now());
        this.updateTime = this.createTime; 
        this.totalCost = calculateTotalCost(itemList);
        this.itemList = itemList;
    }

    public int getOrderID(){
        return orderID;
    }

    public void setOrderID(int orderID){
        this.orderID = orderID;
    }

    public int getUserID(){
        return userID;
    }

    public int getAddressID() {
        return addressID;
    }


    public Timestamp getCreateTime() {
        return createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public String getFormattedCreateTime() {
        return formatTimestamp(createTime);
    }

    public String getFormattedUpdateTime() {
        return formatTimestamp(updateTime);
    }

    private String formatTimestamp(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(timestamp);
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public void setUpdateTime() {
        this.updateTime = Timestamp.from(Instant.now());
    }

    public double getTotalCost() {
        return totalCost;
    }

    private double calculateTotalCost(Map<Product, Integer> itemList) {
        double total = 0.0;
        for (Map.Entry<Product, Integer> entry : itemList.entrySet()) {
            total += entry.getKey().getRetailPrice() * entry.getValue();
        }
        return total;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public String getStatusString() {
        return status.getStatus();
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public void setStatus(String statusName) {
        this.status = OrderStatus.fromName(statusName);
    }

    public void nextStatus() {
        status.nextState(this);
    }

    public Map<Product,Integer> getOrderItems() {
        return itemList;
    }

}