package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import algorithm.HuffmanEncoder;
import dao.CompressionDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.CodeEntry;
import model.CompressionRecord;
import model.CompressionResult;
import util.HtmlUtil;
import util.SymbolUtil;
import util.ValidationUtil;

/**
 * EncodeServlet (URL: /encode, method: POST)
 * 1) action nahi hai  -> text encode karke result dikhata hai (Save button ke saath)
 * 2) action = "save"  -> wahi text dobara encode karke MySQL mein save karta hai
 */
public class EncodeServlet extends HttpServlet {

    private final HuffmanEncoder encoder = new HuffmanEncoder();
    private final CompressionDAO dao = new CompressionDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");
        String ctx = req.getContextPath();

        String text = req.getParameter("text");
        String action = req.getParameter("action");

        // Validation: empty ya galat input
        if (!ValidationUtil.isValidText(text)) {
            resp.getWriter().print(HtmlUtil.errorPage(ctx, "Please enter some text to encode."));
            return;
        }

        try {
            // Algorithm part: text -> Huffman result
            CompressionResult result = encoder.encode(text);

            if ("save".equals(action)) {
                int id = saveRecord(result);                    // DAO part
                resp.sendRedirect(ctx + "/record?id=" + id);    // saved record dikhao
            } else {
                showResult(resp.getWriter(), ctx, text, result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().print(HtmlUtil.errorPage(ctx, "Encoding failed: " + e.getMessage()));
        }
    }

    // CompressionResult se CompressionRecord banake database mein insert karta hai
    private int saveRecord(CompressionResult r) throws Exception {
        CompressionRecord rec = new CompressionRecord();
        rec.setOriginalText(r.getOriginalText());
        rec.setEncodedBits(r.getEncodedBits());
        rec.setOriginalBits(r.getOriginalBits());
        rec.setCompressedBits(r.getCompressedBits());
        rec.setSavedPercent(r.getSavedPercent());

        List<CodeEntry> entries = new ArrayList<>();
        for (Map.Entry<Character, Integer> e : r.getFrequencyMap().entrySet()) {
            char ch = e.getKey();
            // ' ' -> "SPACE", '\n' -> "NEWLINE"
            CodeEntry ce = new CodeEntry(
                    SymbolUtil.toSymbolName(ch),
                    e.getValue(),
                    r.getCodeTable().get(ch));
            entries.add(ce);
        }
        rec.setCodeEntries(entries);

        return dao.insertRecord(rec);
    }

    // Encode ka result page: stats cards, code table, encoded bits, save button
    private void showResult(PrintWriter out, String ctx, String text, CompressionResult r) {
        out.print(HtmlUtil.pageStart(ctx, "Encode Result"));

        // Stats cards
        out.print("<div class=\"stats\">\n");
        out.print("<div class=\"card\"><span>Original size</span><b>" + r.getOriginalBits() + " bits</b></div>\n");
        out.print("<div class=\"card\"><span>Compressed size</span><b>" + r.getCompressedBits() + " bits</b></div>\n");
        out.print("<div class=\"card\"><span>Space saved</span><b>" + String.format("%.2f", r.getSavedPercent()) + "%</b></div>\n");
        out.print("<div class=\"card\"><span>Ratio</span><b>" + String.format("%.2f", r.getRatio()) + "</b></div>\n");
        out.print("</div>\n");

        // Code table (zyada frequency wale pehle)
        List<Map.Entry<Character, Integer>> rows = new ArrayList<>(r.getFrequencyMap().entrySet());
        rows.sort((a, b) -> {
            int c = Integer.compare(b.getValue(), a.getValue());
            return c != 0 ? c : Character.compare(a.getKey(), b.getKey());
        });

        out.print("<h2>Code Table</h2>\n");
        out.print("<table class=\"table\"><tr><th>Symbol</th><th>Frequency</th><th>Huffman Code</th></tr>\n");
        for (Map.Entry<Character, Integer> e : rows) {
            char ch = e.getKey();
            out.print("<tr><td>" + HtmlUtil.esc(SymbolUtil.toSymbolName(ch)) + "</td>"
                    + "<td>" + e.getValue() + "</td>"
                    + "<td>" + HtmlUtil.esc(r.getCodeTable().get(ch)) + "</td></tr>\n");
        }
        out.print("</table>\n");

        // Poora encoded bit string
        out.print("<h2>Encoded Bits</h2>\n");
        out.print("<pre class=\"bits\">" + HtmlUtil.esc(r.getEncodedBits()) + "</pre>\n");

        // Save form: text hidden field mein wapas bhejte hain
        out.print("<form method=\"post\" action=\"" + ctx + "/encode\">\n");
        out.print("<input type=\"hidden\" name=\"text\" value=\"" + HtmlUtil.esc(text) + "\">\n");
        out.print("<input type=\"hidden\" name=\"action\" value=\"save\">\n");
        out.print("<button type=\"submit\">Save Record</button>\n");
        out.print("</form>\n");

        out.print(HtmlUtil.pageEnd());
    }
}