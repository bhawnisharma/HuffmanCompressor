package servlet;

import java.io.IOException;

import dao.CompressionDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import util.HtmlUtil;

/**
 * DeleteServlet (URL: /delete, method: POST)
 * Record id leta hai, database se delete karta hai, phir history page par bhej deta hai.
 */
public class DeleteServlet extends HttpServlet {

    private final CompressionDAO dao = new CompressionDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");
        String ctx = req.getContextPath();

        String idStr = req.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            resp.getWriter().print(HtmlUtil.errorPage(ctx, "Record id is missing."));
            return;
        }

        try {
            int id = Integer.parseInt(idStr.trim());
            boolean deleted = dao.deleteRecord(id);
            if (deleted) {
                resp.sendRedirect(ctx + "/history");
            } else {
                resp.getWriter().print(HtmlUtil.errorPage(ctx, "Record not found, nothing deleted."));
            }
        } catch (NumberFormatException e) {
            resp.getWriter().print(HtmlUtil.errorPage(ctx, "Invalid record id."));
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().print(HtmlUtil.errorPage(ctx, "Delete failed: " + e.getMessage()));
        }
    }
}