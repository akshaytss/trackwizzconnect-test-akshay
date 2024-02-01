package app.trackwizz.connect.helper.massager;

import org.springframework.util.StringUtils;

public class RemoveSpecialCharactersMobileNo {
    public String clear(String input) {
        if (!StringUtils.hasText(input))
            return "";

        if (input.charAt(0) == '#')
            return getMobileNoFromAddress(input);

        return input.trim();
    }

    private String getMobileNoFromAddress(String address) {
        int currInd = address.length() - 1;
        int numLength = 0;
        StringBuilder str = new StringBuilder();
        while (currInd >= 0) {
            if (Character.isDigit(address.charAt(currInd))) {
                str.append(address.charAt(currInd));
                currInd--;
                numLength++;
            } else {
                break;
            }
        }
        if (numLength > 6) {
            return new StringBuilder(str.toString()).reverse().toString();
        } else {
            return "";
        }
    }
}
