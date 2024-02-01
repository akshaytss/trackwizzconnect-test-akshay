package app.trackwizz.connect.helper.massager;

import org.springframework.util.StringUtils;

public class RemoveSpecialCharactersGender {

    public String clear(String input) {
        if (!StringUtils.hasText(input))
            return "";

        String temp = input.toLowerCase();
        if (temp.contains("female"))
            return "Female";
        else if (temp.contains("male"))
            return "Male";
        else
            return input;
    }
}

