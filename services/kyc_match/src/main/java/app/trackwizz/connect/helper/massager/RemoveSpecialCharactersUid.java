package app.trackwizz.connect.helper.massager;

import org.springframework.util.StringUtils;

public class RemoveSpecialCharactersUid {

    public String clear(String input) {
        if (!StringUtils.hasText(input))
            return "";

        StringBuilder sb = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (Character.isDigit(c) || c == ' ') {
                sb.append(c);
            }
        }

        removeBlankSpace(sb);
        return getLast4Digits(sb.toString());
    }

    private String getLast4DigitsInReversed(String extractedIdNumber) {
        String revStr = new StringBuilder(extractedIdNumber).reverse().toString();
        return revStr.substring(0, 4);
    }

    private String getLast4Digits(String extractedIdNumber) {
        if (extractedIdNumber == null || extractedIdNumber.length() <= 3)
            return extractedIdNumber;

        String last4Digits = getLast4DigitsInReversed(extractedIdNumber);
        last4Digits = getLast4DigitsInReversed(last4Digits);
        return last4Digits;
    }

    private void removeBlankSpace(StringBuilder sb) {
        int j = 0;
        for (int i = 0; i < sb.length(); i++) {
            if (!Character.isWhitespace(sb.charAt(i))) {
                sb.setCharAt(j++, sb.charAt(i));
            }
        }
        sb.delete(j, sb.length());
    }
}

