package app.trackwizz.connect.helper.massager;

import org.springframework.util.StringUtils;

public class RemoveSpecialCharactersZipCode {
    public String clear(String input) {
        if (!StringUtils.hasText(input))
            return "";

        String address = input.toLowerCase();
        return getZipCodeFromAddress(address);
    }

    private String getZipCodeFromAddress(String addressInput) {
        addressInput = addressInput.trim();
        String address = getReadablePartOfAddress(addressInput);
        String zipCode;
        int currInd = address.length() - 1;
        int numLength = 0;
        StringBuilder str = new StringBuilder();

        while (currInd >= 0) {
            if (Character.isDigit(address.charAt(currInd))) {
                numLength++;
                str.append(address.charAt(currInd));
            } else {
                break;
            }
            currInd--;
        }

        if (str.length() > 6) {
            str = str.delete(0, str.length());
            numLength = 0;

            while (currInd >= 0 && !Character.isDigit(address.charAt(currInd))) {
                currInd--;
            }

            while (currInd >= 0 && numLength < 6) {
                if (Character.isDigit(address.charAt(currInd))) {
                    numLength++;
                    str.append(address.charAt(currInd));
                } else {
                    break;
                }
                currInd--;
            }
        }

        zipCode = reverseZipString(str.toString());
        return zipCode;
    }

    private String getReadablePartOfAddress(String address) {
        StringBuilder str = new StringBuilder(address);
        while (!Character.isDigit(str.charAt(str.length() - 1))) {
            str.deleteCharAt(str.length() - 1);
        }
        return str.toString();
    }

    private String reverseZipString(String str) {
        return new StringBuilder(str).reverse().toString();
    }
}

