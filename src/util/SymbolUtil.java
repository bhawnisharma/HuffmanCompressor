
package util;

public class SymbolUtil {

    public static String toSymbolName(char ch) {

        if (ch == ' ') {
            return "SPACE";
        }

        if (ch == '\n') {
            return "NEWLINE";
        }

        if (ch == '\t') {
            return "TAB";
        }

        return String.valueOf(ch);
    }
}

