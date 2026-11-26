package util;

public class ValidationUtil {

    public static boolean isValidText(String text) {
        return text != null && !text.isEmpty();
    }

    public static void validateText(String text) {
        if (!isValidText(text)) {
            throw new IllegalArgumentException(
                "Input text cannot be empty or null."
            );
        }
    }
        // Bits string valid hai ya nahi: khali nahi honi chahiye, sirf 0 aur 1 hone chahiye
    public static boolean isValidBits(String s) {
        return s != null && !s.trim().isEmpty() && s.trim().matches("[01\\s]+");
    }
}
