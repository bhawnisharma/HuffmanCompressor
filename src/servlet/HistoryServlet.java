package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import dao.CompressionDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.CodeEntry;
import model.CompressionRecord;
import util.HtmlUtil;

/**
 * HistoryServlet (method: GET)
 *   /history      -> saare saved records ki list
 *   /record?id=5  -> ek record ki poori detail (code table ke saath)
 */
public class HistoryServlet extends HttpServlet {

    private final CompressionDAO dao = new CompressionDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");
        String ctx = req.getContextPath();
        PrintWriter out = resp.getWriter();

        try {
            if ("/record".equals(req.getServletPath())) {
                showRecord(req, out, ctx);
            } else {
                showHistory(out, ctx);
            }
        } catch (NumberFormatException e) {
            out.print(HtmlUtil.errorPage(ctx, "Invalid record id."));
        } catch (Exception e) {
            e.printStackTrace();
            out.print(HtmlUtil.errorPage(ctx, "Something went wrong: " + e.getMessage()));
        }
    }

    // Saare records ki table
    private void showHistory(PrintWriter out, String ctx) throws Exception {
        List<CompressionRecord> list = dao.getAllRecords();

        out.print(HtmlUtil.pageStart(ctx, "History"));
        if (list == null || list.isEmpty()) {
            out.print("<p>No saved records yet.</p>\n");
        } else {
            out.print("<table class=\"table\"><tr><th>ID</th><th>Text</th><th>Original</th>"
                    + "<th>Compressed</th><th>Saved %</th><th>Date</th><th>Action</th></tr>\n");
            for (CompressionRecord r : list) {
                String shortText = r.getOriginalText();
                if (shortText.length() > 40) {
                    shortText = shortText.substring(0, 40) + "...";
                }
                out.print("<tr>"
                        + "<td>" + r.getId() + "</td>"
                        + "<td>" + HtmlUtil.esc(shortText) + "</td>"
                        + "<td>" + r.getOriginalBits() + "</td>"
                        + "<td>" + r.getCompressedBits() + "</td>"
                        + "<td>" + String.format("%.2f", r.getSavedPercent()) + "%</td>"
                        + "<td>" + HtmlUtil.esc(String.valueOf(r.getCreatedAt())) + "</td>"
                        + "<td><a href=\"" + ctx + "/record?id=" + r.getId() + "\">View</a> "
                        + deleteForm(ctx, r.getId()) + "</td>"
                        + "</tr>\n");
            }
            out.print("</table>\n");
        }
        out.print(HtmlUtil.pageEnd());
    }

    // Ek record ki detail
    private void showRecord(HttpServletRequest req, PrintWriter out, String ctx) throws Exception {
        String idStr = req.getParameter("id");
        if (idStr == null) {
            out.print(HtmlUtil.errorPage(ctx, "Record id is missing."));
            return;
        }
        CompressionRecord rec = dao.getRecordById(Integer.parseInt(idStr.trim()));
        if (rec == null) {
            out.print(HtmlUtil.errorPage(ctx, "Record not found."));
            return;
        }

        out.print(HtmlUtil.pageStart(ctx, "Record #" + rec.getId()));

        out.print("<div class=\"stats\">\n");
        out.print("<div class=\"card\"><span>Original size</span><b>" + rec.getOriginalBits() + " bits</b></div>\n");
        out.print("<div class=\"card\"><span>Compressed size</span><b>" + rec.getCompressedBits() + " bits</b></div>\n");
        out.print("<div class=\"card\"><span>Space saved</span><b>" + String.format("%.2f", rec.getSavedPercent()) + "%</b></div>\n");
        out.print("</div>\n");

        out.print("<h2>Original Text</h2>\n<pre class=\"output\">" + HtmlUtil.esc(rec.getOriginalText()) + "</pre>\n");

        out.print("<h2>Code Table</h2>\n");
        out.print("<table class=\"table\"><tr><th>Symbol</th><th>Frequency</th><th>Huffman Code</th></tr>\n");
        for (CodeEntry ce : rec.getCodeEntries()) {
            out.print("<tr><td>" + HtmlUtil.esc(ce.getSymbol()) + "</td>"
                    + "<td>" + ce.getFrequency() + "</td>"
                    + "<td>" + HtmlUtil.esc(ce.getHuffmanCode()) + "</td></tr>\n");
        }
        out.print("</table>\n");

        out.print("<h2>Encoded Bits</h2>\n<pre class=\"bits\">" + HtmlUtil.esc(rec.getEncodedBits()) + "</pre>\n");

        // Is record ko decode karne ka button
        out.print("<form method=\"post\" action=\"" + ctx + "/decode\" style=\"display:inline\">"
                + "<input type=\"hidden\" name=\"recordId\" value=\"" + rec.getId() + "\">"
                + "<button type=\"submit\">Decode</button></form> ");
        out.print(deleteForm(ctx, rec.getId()));
        out.print(" <a href=\"" + ctx + "/history\">&larr; Back to History</a>\n");

        out.print(HtmlUtil.pageEnd());
    }

    // Delete button (POST /delete) - confirm popup ke saath
    private static String deleteForm(String ctx, int id) {
        return "<form method=\"post\" action=\"" + ctx + "/delete\" style=\"display:inline\" "
                + "onsubmit=\"return confirm('Delete this record?');\">"
                + "<input type=\"hidden\" name=\"id\" value=\"" + id + "\">"
                + "<button type=\"submit\">Delete</button></form>";
    }
}