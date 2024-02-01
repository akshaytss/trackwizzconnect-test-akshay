package app.trackwizz.connect.helper.massager;

import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.Year;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class RemoveSpecialCharactersDOB {

    private final String[] formats = {
            "dd MMM yyyy", "dd MM yyyy", "dd MM yy", "d M yyyy", "d M yy", "dd MMM yy",
            "d-M-yyyy", "dd-MM-yyyy", "dd-M-yyyy", "dd-MMM-yy", "dd-MM-yy", "d-M-yy",
            "d-MMM-yy", "dd-MMM-yyyy", "d-MMM-yyyy", "d/M/yyyy", "dd/MM/yyyy", "dd/M/yyyy",
            "dd/MMM/yy", "dd/MM/yy", "d/M/yy", "d/MMM/yy", "dd/MMM/yyyy", "d/MMM/yyyy"
    };

    public String clear(String input) {
        if (!StringUtils.hasText(input))
            return "";

        StringBuilder year = new StringBuilder();
        StringBuilder month = new StringBuilder();
        StringBuilder day = new StringBuilder();
        int counter = 0;
        String monthString;
        String inputStr = input.trim();
        Date dob;
        LocalDate tempDob;

        try {
            tempDob = parseDate(inputStr);
            monthString = getMonthByNumber(String.valueOf(tempDob.getMonth().getValue()));
            return tempDob.getDayOfMonth() + "/" + monthString + "/" + (tempDob.getYear());
        } catch (Exception e) {
            // Parsing failed, continue with the manual extraction
        }

        for (int i = inputStr.length() - 1; i >= 0; i--) {
            char c = inputStr.charAt(i);
            if (c >= '0' && c <= '9') {
                counter++;
                if (counter <= 4) {
                    year.insert(0, c);
                } else if (counter <= 6) {
                    month.insert(0, c);
                } else if (counter <= 8) {
                    day.insert(0, c);
                } else {
                    break;
                }
            }
        }

        if (day.isEmpty() && month.isEmpty() && year.length() == 4) {
            return year.toString();
        }

        monthString = getMonthByNumber(month.toString());
        if (day.length() == 2 && month.length() == 2 && year.length() == 4 && monthString != null) {
            return day.toString() + "/" + monthString + "/" + year.toString();
        } else {
            return removeNoiseDOB(input);
        }
    }

    private LocalDate parseDate(String dateString) {
        for (String format : formats) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
                return LocalDate.parse(dateString, formatter);
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    private String removeNoiseDOB(String input) {
        StringBuilder sb = new StringBuilder();
        for (char c : input.toCharArray()) {
            if ((c >= '0' && c <= '9') || c == '-' || c == '/') {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private String getMonthByNumber(String monthNumber) {
        return switch (monthNumber) {
            case "01", "1" -> "JAN";
            case "02", "2" -> "FEB";
            case "03", "3" -> "MAR";
            case "04", "4" -> "APR";
            case "05", "5" -> "MAY";
            case "06", "6" -> "JUN";
            case "07", "7" -> "JUL";
            case "08", "8" -> "AUG";
            case "09", "9" -> "SEP";
            case "10" -> "OCT";
            case "11" -> "NOV";
            case "12" -> "DEC";
            default -> null;
        };
    }
}

