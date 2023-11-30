package model;

public class BankDetail {
    private int bankDetailID;
    private int userID;
    private String cardName;
    private String cardHolderName;
    private String cardNumber;
    private String expiryDate;

    public BankDetail() {
    }

    public BankDetail(int userID, String cardName, String cardHolderName, String cardNumber, String expiryDate) {
        this.userID = userID;
        this.cardName = cardName;
        this.cardHolderName = cardHolderName;
        this.cardNumber = cardNumber;
        this.expiryDate = expiryDate;
    }

    public BankDetail(int bankDetailID, int userID, String cardName, String cardHolderName, String cardNumber, String expiryDate) {
        this(userID, cardName, cardHolderName, cardNumber, expiryDate);
        this.bankDetailID = bankDetailID;
    }

    // Getter and Setter

    public int getBankDetailID() {
        return bankDetailID;
    }

    public void setBankDetailID(int bankDetailID) {
        this.bankDetailID = bankDetailID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public void setExpiryDate(int month, int year) {
        this.expiryDate = Integer.toString(month) + "/" + Integer.toString(year);
    }

}
