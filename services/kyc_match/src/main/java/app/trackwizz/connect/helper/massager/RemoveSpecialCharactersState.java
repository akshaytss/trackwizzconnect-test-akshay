package app.trackwizz.connect.helper.massager;

import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashSet;

public class RemoveSpecialCharactersState {
    public String clear(String input) {
        if (!StringUtils.hasText(input))
            return "";

        String address = input.trim();
        return getStateFromAddress(address);
    }

    private String getStateFromAddress(String address) {
        address = address.trim();
        if (!Character.isDigit(address.charAt(address.length() - 1))) {
            address = address.substring(0, address.length() - 1);
        }
        String[] addressWordsList;
        String stateName = "";

        if (address.contains(",")) {
            addressWordsList = address.split(",");
            String temp = addressWordsList[addressWordsList.length - 1];
            if (temp.contains("-")) {
                // cases like: delhi-400006
                stateName = temp.split("-")[0];
            } else {
                stateName = addressWordsList[addressWordsList.length - 2];
            }
            return stateName;
        }

        address = getReadablePartOfAddress(address);
        int zipCodeStartIndex = getZipCodeStartIndexFromAddress(address);
        address = address.substring(0, zipCodeStartIndex).trim();
        addressWordsList = address.split(" ");
        String leftMostWord = addressWordsList[addressWordsList.length - 1].toLowerCase();
        HashSet<String> twoWordStateLeftMostWordSet = new HashSet<>(Arrays.asList("pradesh", "nadu", "bengal", "kashmir"));

        if (twoWordStateLeftMostWordSet.contains(leftMostWord)) {
            stateName = addressWordsList[addressWordsList.length - 2] + addressWordsList[addressWordsList.length - 1];
        } else if (leftMostWord.contains("islands") || leftMostWord.contains("nicobar")) {
            stateName = "Andaman And Nicobar";
        } else if (leftMostWord.contains("diu") || leftMostWord.contains("haveli")) {
            stateName = "Dadra And Nagar Haveli And Daman And Diu";
        } else {
            stateName = addressWordsList[addressWordsList.length - 1];
        }
        return stateName;
    }

    private int getZipCodeStartIndexFromAddress(String address) {
        int currInd = address.length() - 1;
        int numLength = 0;
        StringBuilder str = new StringBuilder();

        while (currInd >= 0) {
            if (Character.isDigit(address.charAt(currInd))) {
                currInd--;
                numLength++;
            } else {
                break;
            }
        }

        if (str.length() > 6) {
            str = new StringBuilder();
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
        return currInd + 1;
    }

    private String getReadablePartOfAddress(String address) {
        StringBuilder str = new StringBuilder(address);
        while (!Character.isDigit(str.charAt(str.length() - 1))) {
            str.deleteCharAt(str.length() - 1);
        }
        return str.toString();
    }
}

