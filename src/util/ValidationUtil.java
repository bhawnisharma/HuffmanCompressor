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
}
