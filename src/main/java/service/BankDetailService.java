package service;

import DAO.BankDetailDAO;
import DAO.EncryptionKeysDAO;
import exception.AuthorizationException;
import exception.DatabaseException;
import exception.ExceptionHandler;
import helper.AESUtil;
import model.BankDetail;

public class BankDetailService {
    private static PermissionService permission = new PermissionService();

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
     * Adds bank details for a user to the database.
     *
     * This method first checks if the user has permission to add bank details.
     * If permitted, it encrypts the card number and security code using AES encryption and inserts the bank details into the database.
     * Returns a success message or an error message depending on the outcome of the operation.
     *
     * @param userID       The ID of the user for whom the bank details are being added.
     * @param cardHolder   The name of the card holder.
     * @param cardNumber   The card number.
     * @param expiryDate   The card's expiry date in MM/YY format.
     * @param securityCode The card's security code.
     * @return A status message indicating the result of the operation (e.g., "success" or an error message).
     */
    public static String addBankDetail(int userID, String cardHolder, String cardNumber, String expiryDate, String securityCode){
        try{
            if (!permission.hasPermission(userID,"VIEW_OWN_ORDERS")) {
                throw new AuthorizationException("Access denied. Users can only access their own carts.");
            }
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
            } else {
                return "Action Failed. Please try again.";
            }

            String cardName = getCreditCardType(cardNumber);
            String encryCardNumber = AESUtil.encrypt(cardNumber, keyString);
            String encrySecurityCode = AESUtil.encrypt(securityCode, keyString);
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
    
}
