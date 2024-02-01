package app.trackwizz.connect.helper.massager;

import org.springframework.util.StringUtils;

public class RemoveSpecialCharactersPanNumber {

    public String clear(String input) {
        if (!StringUtils.hasText(input))
            return "";

        StringBuilder sb = new StringBuilder();
        for (char c : input.toCharArray()) {
            if ((c >= '0' && c <= '9') || (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')) {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
