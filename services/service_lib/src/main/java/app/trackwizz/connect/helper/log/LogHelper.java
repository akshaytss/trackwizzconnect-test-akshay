package app.trackwizz.connect.helper.log;

import java.util.Arrays;

public class LogHelper {
    public static String getExceptionDetails(Exception e) {
        return "\nException Message: " + e.getMessage() + "\nStack Trace: " + Arrays.toString(e.getStackTrace());
    }
}
