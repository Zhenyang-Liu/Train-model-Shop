package service;

import DAO.BankDetailDAO;
import DAO.EncryptionKeysDAO;
import exception.AuthorizationException;
import exception.DatabaseException;
import exception.ExceptionHandler;
import helper.AESUtil;
import helper.UserSession;
import model.BankDetail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BankDetailService {
    /**
     * Determines the type of a credit card based on its number.
     *
     * This method identifies the credit card type (e.g., Visa, MasterCard, American Express, Discover, JCB)
     * based on the given card number using standard prefixes. Returns "Unknown" if the card type cannot be identified.
     *
     * @param cardNumber The credit card number.
     * @return The type of the credit card, or "Unknown" if the type cannot be determined.
     */
    public static String getCreditCardType(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) return "Unknown";

        String firstDigit = cardNumber.substring(0, 1);
        String firstTwoDigits = cardNumber.substring(0, 2);
        String firstThreeDigits = cardNumber.substring(0, 3);
        String firstFourDigits = cardNumber.substring(0, 4);
        String firstSixDigits = cardNumber.length() >= 6 ? cardNumber.substring(0, 6) : "";

        if (firstDigit.equals("4")) {
            return "Visa";
        } else if (firstTwoDigits.compareTo("51") >= 0 && firstTwoDigits.compareTo("55") <= 0) {
            return "MasterCard";
        } else if (firstTwoDigits.equals("34") || firstTwoDigits.equals("37")) {
            return "American Express";
        } else if (firstFourDigits.equals("6011") || firstTwoDigits.equals("65") ||
                   (firstSixDigits.compareTo("622126") >= 0 && firstSixDigits.compareTo("622925") <= 0) ||
                   (firstThreeDigits.compareTo("644") >= 0 && firstThreeDigits.compareTo("649") <= 0)) {
            return "Discover";
        } else if (firstTwoDigits.equals("35") &&
                   (firstFourDigits.compareTo("3528") >= 0 && firstFourDigits.compareTo("3589") <= 0)) {
            return "JCB";
        } else {
            return "Unknown";
        }
    }

    /**
     * Validates a credit card number using the Luhn algorithm.
     *
     * This method performs a checksum test on the credit card number to determine its validity.
     * It follows the Luhn algorithm commonly used for this purpose.
     *
     * @param cardNumber The credit card number to be validated.
     * @return true if the card number is valid according to the Luhn algorithm; false otherwise.
     */
    public static boolean isValidCreditCardNumber(String cardNumber) {
        int sum = 0;
        boolean alternate = false;
        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            char c = cardNumber.charAt(i);
            // Check if the character is a digit
            if (Character.isDigit(c)) {
                try {
                    int n = Integer.parseInt(String.valueOf(c));
                    if (alternate) {
                        n *= 2;
                        if (n > 9) {
                            n = (n % 10) + 1;
                        }
                    }
                    sum += n;
                    alternate = !alternate;
                } catch (NumberFormatException e) {
                    return false;
                }
            }
        }
        return (sum % 10 == 0);
    }
    
    /**
     * Adds bank details for a user.
     *
     * @param userID        The ID of the user.
     * @param cardHolder    The name of the card holder.
     * @param cardNumber    The credit card number.
     * @param expiryDate    The expiry date of the card.
     * @param securityCode  The security code of the card.
     * @return String       A message indicating the result of the operation.
     */
    public static String addBankDetail(int userID, String cardHolder, String cardNumber, String expiryDate, String securityCode){
        try {
            // Check if user has permission to edit bank details
            if (!PermissionService.hasPermission(userID,"EDIT_OWN_BANK_DETAILS")) {
                throw new AuthorizationException("Access denied. Users can only access their own bank details.");
            }

            // Find or create encryption key for the user
            int keyID = EncryptionKeysDAO.findUserKey(userID);
            String keyString = "";
            if (keyID > 0){
                keyString = EncryptionKeysDAO.findKey(keyID);
            } else if (keyID == 0) {
                keyString = AESUtil.generateAESKey();
                keyID = EncryptionKeysDAO.insertKey(keyString);
                if (keyID == 0){
                    return "Generate encryption key Failed. Please try again.";
                }
                // Associate user with the key
                EncryptionKeysDAO.insertUserKey(userID, keyID);
            } else {
                return "Action Failed. Please try again.";
            }

            // Encrypt card details
            String cardName = getCreditCardType(cardNumber);
            String encryCardNumber = AESUtil.encrypt(cardNumber, keyString);
            String encrySecurityCode = AESUtil.encrypt(securityCode, keyString);

            // Insert bank details into the database
            BankDetail bankDetail = new BankDetail(userID, cardName, cardHolder, encryCardNumber, expiryDate, encrySecurityCode);
            BankDetailDAO.insertBankDetail(bankDetail);

        } catch (DatabaseException e){
            ExceptionHandler.printErrorMessage(e);
            return e.getMessage();
        } catch (Exception ex) {
            ex.printStackTrace();
            return "Action Failed. Please try again.";
        }
        return "success";
    }

    /**
     * Finds and returns the bank details of a user.
     *
     * @param userID The ID of the user.
     * @return BankDetail The decrypted bank detail of the user, or null if an error occurs.
     */
    public static BankDetail findBankDetail(){
        try {
            // Retrieve encrypted bank details from the database
            int currentUserID = UserSession.getInstance().getCurrentUser().getUserID();
            BankDetail bankDetail = BankDetailDAO.findBankDetail(currentUserID);
            if (bankDetail == null) {
                return null;
            }

            // Check if user has permission to view bank details
            if (!PermissionService.hasPermission(bankDetail.getUserID(),"EDIT_OWN_BANK_DETAILS")) {
                throw new AuthorizationException("Access denied. Users can only access their own bank details.");
            }

            // Decrypt bank details
            String keyString = EncryptionKeysDAO.findKey(EncryptionKeysDAO.findUserKey(currentUserID));
            String cardNumber = AESUtil.decrypt(bankDetail.getCardNumber(), keyString);
            String securityCode = AESUtil.decrypt(bankDetail.getSecurityCode(), keyString);

            // Set decrypted values in the bank detail object
            bankDetail.setCardNumber(cardNumber);
            bankDetail.setSecurityCode(securityCode);
            return bankDetail;

        } catch (DatabaseException e){
            ExceptionHandler.printErrorMessage(e);
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Updates the bank details for a specific user.
     *
     * @param userID        The ID of the user whose bank details are to be updated.
     * @param cardHolder    The updated name of the card holder.
     * @param cardNumber    The updated credit card number.
     * @param expiryDate    The updated expiry date of the card.
     * @param securityCode  The updated security code of the card.
     * @return String       A message indicating the result of the operation.
     */
    public static String updateBankDetail(int userID, String cardHolder, String cardNumber, String expiryDate, String securityCode) {
        try {
            // Check if the user has permission to edit their bank details
            if (!PermissionService.hasPermission(userID, "EDIT_OWN_BANK_DETAILS")) {
                throw new AuthorizationException("Access denied. Users can only edit their own bank details.");
            }

            // Retrieve the encryption key for the user
            int keyID = EncryptionKeysDAO.findUserKey(userID);
            if (keyID <= 0) {
                return "Encryption key not found. Please try again.";
            }
            String keyString = EncryptionKeysDAO.findKey(keyID);

            // Encrypt the updated card details
            String cardName = getCreditCardType(cardNumber);
            String encryCardNumber = AESUtil.encrypt(cardNumber, keyString);
            String encrySecurityCode = AESUtil.encrypt(securityCode, keyString);

            // Update the bank details in the database
            BankDetail bankDetail = new BankDetail(userID, cardName, cardHolder, encryCardNumber, expiryDate, encrySecurityCode);
            BankDetailDAO.updateBankDetail(bankDetail);

        } catch (DatabaseException e) {
            ExceptionHandler.printErrorMessage(e);
            return e.getMessage();
        } catch (Exception ex) {
            ex.printStackTrace();
            return "Action Failed. Please try again.";
        }
        return "success";
    }

    /**
     * Checks the validity of bank details.
     *
     * @param cardHolder   The name of the card holder.
     * @param cardNumber   The credit card number.
     * @param expiryDate   The expiry date of the card in mm/yy format.
     * @param securityCode The security code of the card.
     * @return String      A message indicating whether the bank details are valid or not.
     */
    public static String checkBankDetail(String cardHolder, String cardNumber, String expiryDate, String securityCode) {
        // Check if the card number is valid
        if (!isValidCreditCardNumber(cardNumber)) {
            
            return "Invalid card number.";
        }

        // Check the card type
        String cardType = getCreditCardType(cardNumber);
        if ("Unknown".equals(cardType)) {
            return "Card type cannot be determined. Change a card";
        }

        // Check the expiry date format and if it's a future date.
        if (!expiryDate.matches("\\d{2}/\\d{2}") || !isFutureDate(expiryDate)) {
            return "Invalid expiry date. Ensure it's in mm/yy format and is a future date.";
        }

        // Check the security code
        if (!securityCode.matches("\\d{3}")) {
            return "Invalid security code. Should be 3digits.";
        }

        // All checks passed
        return "Bank details are valid.";
    }

    /**
     * Checks if a given date in mm/yy format is a future date.
     *
     * @param dateStr The date string in mm/yy format.
     * @return true if the date is in the future, false otherwise.
     */
    private static boolean isFutureDate(String dateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/yy");
            sdf.setLenient(false);
            Date expiry = sdf.parse(dateStr);
            return expiry.after(new Date());
        } catch (ParseException e) {
            return false;
        }
    }

}
