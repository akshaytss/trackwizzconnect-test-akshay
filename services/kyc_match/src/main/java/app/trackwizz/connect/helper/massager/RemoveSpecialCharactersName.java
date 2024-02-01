package app.trackwizz.connect.helper.massager;

import org.springframework.util.StringUtils;

public class RemoveSpecialCharactersName {
    public String clear(String input) {
        if (!StringUtils.hasText(input))
            return "";

        StringBuilder sb = new StringBuilder();
        for (char c : input.toCharArray()) {
            if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || c == ' ') {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}