package util;

/**
 * HtmlUtil - servlets ke liye chhota helper.
 * HTML escape karta hai aur har page ka common upar/neeche wala part deta hai.
 */
public class HtmlUtil {

    // User ka text HTML mein safe dikhane ke liye (<, >, & etc. ko escape karta hai)
    public static String esc(String s) {
        if (s == null) {
            return "";
        }
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    // Page ka upar wala part: head, css link, navigation tabs, heading
    public static String pageStart(String ctx, String title) {
        return "<!DOCTYPE html>\n<html lang=\"en\">\n<head>\n"
                + "<meta charset=\"UTF-8\">\n"
                + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n"
                + "<title>" + esc(title) + "</title>\n"
                + "<link rel=\"stylesheet\" href=\"" + ctx + "/css/style.css\">\n"
                + "</head>\n<body>\n<div class=\"container\">\n"
                + "<nav class=\"tabs\">"
                + "<a href=\"" + ctx + "/index.html\">Encode</a> "
                + "<a href=\"" + ctx + "/decode.html\">Decode</a> "
                + "<a href=\"" + ctx + "/history\">History</a>"
                + "</nav>\n"
                + "<h1>" + esc(title) + "</h1>\n";
    }

    // Page ka neeche wala part
    public static String pageEnd() {
        return "</div>\n</body>\n</html>";
    }

    // Error message wala poora page
    public static String errorPage(String ctx, String message) {
        return pageStart(ctx, "Error")
                + "<p class=\"error\">" + esc(message) + "</p>\n"
                + "<p><a href=\"javascript:history.back()\">&larr; Go back</a></p>\n"
                + pageEnd();
    }
}