package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import algorithm.HuffmanDecoder;
import dao.CompressionDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.CodeEntry;
import model.CompressionRecord;
import util.HtmlUtil;
import util.ValidationUtil;

/**
 * DecodeServlet (URL: /decode, method: POST)
 * Do tarike se input aa sakta hai:
 *   1) recordId           -> bits aur code table database se
 *   2) bits + codeTable   -> user ne khud likha (codeTable ki har line: symbol=code, jaise A=0)
 */
public class DecodeServlet extends HttpServlet {

    private final HuffmanDecoder decoder = new HuffmanDecoder();
    private final CompressionDAO dao = new CompressionDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");
        String ctx = req.getContextPath();
        PrintWriter out = resp.getWriter();

        try {
            String bits;
            Map<Character, String> codeTable = new HashMap<>();

            String recordId = req.getParameter("recordId");
            if (recordId != null && !recordId.trim().isEmpty()) {
                // Tarika 1: saved record se
                CompressionRecord rec = dao.getRecordById(Integer.parseInt(recordId.trim()));
                if (rec == null) {
                    out.print(HtmlUtil.errorPage(ctx, "Record not found."));
                    return;
                }
                bits = rec.getEncodedBits();
                for (CodeEntry ce : rec.getCodeEntries()) {
                    codeTable.put(symbolToChar(ce.getSymbol()), ce.getHuffmanCode());
                }
            } else {
                // Tarika 2: user ke diye bits + code table se
                bits = req.getParameter("bits");
                if (bits != null) {
                    bits = bits.replaceAll("\\s", "");   // space / new line hata do
                }
                if (!ValidationUtil.isValidBits(bits)) {
                    out.print(HtmlUtil.errorPage(ctx, "Bits must contain only 0 and 1."));
                    return;
                }
                codeTable = parseCodeTable(req.getParameter("codeTable"));
                if (codeTable.isEmpty()) {
                    out.print(HtmlUtil.errorPage(ctx, "Code table is empty. Write one entry per line, like A=0"));
                    return;
                }
            }

            // Algorithm part: bits -> original text
            String decoded = decoder.decode(bits, codeTable);

            out.print(HtmlUtil.pageStart(ctx, "Decode Result"));
            out.print("<h2>Encoded Bits</h2>\n<pre class=\"bits\">" + HtmlUtil.esc(bits) + "</pre>\n");
            out.print("<h2>Original Text</h2>\n<pre class=\"output\">" + HtmlUtil.esc(decoded) + "</pre>\n");
            out.print(HtmlUtil.pageEnd());

        } catch (NumberFormatException e) {
            out.print(HtmlUtil.errorPage(ctx, "Invalid record id."));
        } catch (Exception e) {
            e.printStackTrace();
            out.print(HtmlUtil.errorPage(ctx, "Decoding failed: " + e.getMessage()));
        }
    }

    // "A=0" jaisi lines ko Map<Character,String> mein badalta hai
    private Map<Character, String> parseCodeTable(String text) {
        Map<Character, String> map = new HashMap<>();
        if (text == null) {
            return map;
        }
        for (String line : text.split("\\r?\\n")) {
            if (line.trim().isEmpty()) {
                continue;
            }
            int idx = line.lastIndexOf('=');   // code sirf 0/1 hota hai, isliye aakhri '=' hi separator hai
            if (idx <= 0) {
                throw new IllegalArgumentException("Invalid line: " + line);
            }
            String sym = line.substring(0, idx).trim();
            String code = line.substring(idx + 1).trim();
            if (!code.matches("[01]+")) {
                throw new IllegalArgumentException("Code must be 0/1 only: " + line);
            }
            map.put(symbolToChar(sym), code);
        }
        return map;
    }

    // SymbolUtil.toSymbolName ka ulta: "SPACE" -> ' ', "NEWLINE" -> '\n'
    private char symbolToChar(String sym) {
        if ("SPACE".equals(sym)) return ' ';
        if ("NEWLINE".equals(sym)) return '\n';
        if ("TAB".equals(sym)) return '\t';
        if (sym != null && sym.length() == 1) return sym.charAt(0);
        throw new IllegalArgumentException("Unknown symbol: " + sym);
    }
}