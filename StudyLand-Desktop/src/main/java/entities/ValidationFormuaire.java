package entities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationFormuaire {
    public static boolean isEmail(String str) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }
    public static boolean isValidPassword(String str) {
        return str != null && str.length() > 5;
    }
}

