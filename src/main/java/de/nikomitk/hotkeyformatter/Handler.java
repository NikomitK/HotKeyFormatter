package de.nikomitk.hotkeyformatter;

public class Handler {

    public static String toSarcastic(String pText) {
        String rText = "";
        for (int i = 0; i < pText.length(); i++) {
            // char has no uppercase method and can somehow not be casted into string wtf
            if (i % 2 == 0) rText += ("" + pText.charAt(i)).toUpperCase();
            else rText += ("" + pText.charAt(i)).toLowerCase();
        }
        return rText;
    }

    public static String toUppercase(String pText) {
        return pText.toUpperCase();
    }

    public static String toLowercase(String pText) {
        return pText.toLowerCase();
    }
}
