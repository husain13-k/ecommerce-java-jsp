package servlet;

import dao.OrderDAO;
import dao.ProductDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Product;
import model.User;

import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@WebServlet("/admin-action")
public class AdminServlet extends HttpServlet {

    private Product readProduct(HttpServletRequest req) {
        String id = req.getParameter("id");
        return new Product(
            id == null || id.isEmpty() ? 0 : Integer.parseInt(id),
            req.getParameter("name").trim(),
            req.getParameter("description"),
            Double.parseDouble(req.getParameter("price")),
            Integer.parseInt(req.getParameter("stock")),
            req.getParameter("image"),
            Integer.parseInt(req.getParameter("categoryId")),
            null);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession();

        // only admins can use this servlet
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.role())) {
            resp.sendError(403);
            return;
        }

        String action = req.getParameter("action");
        String back = "status".equals(action) ? "/admin/orders.jsp" : "/admin/products.jsp";

        try {
            switch (action) {
                case "addCategory":
                    ProductDAO.addCategory(req.getParameter("name").trim());
                    session.setAttribute("flash", "Category added");
                    break;
                case "addProduct":
                    ProductDAO.add(readProduct(req));
                    session.setAttribute("flash", "Product added");
                    break;
                case "updateProduct":
                    ProductDAO.update(readProduct(req));
                    session.setAttribute("flash", "Product updated");
                    break;
                case "deleteProduct":
                    ProductDAO.delete(Integer.parseInt(req.getParameter("id")));
                    session.setAttribute("flash", "Product deleted");
                    break;
                case "status":
                    String status = req.getParameter("status");
                    if (!List.of("PENDING", "SHIPPED", "DELIVERED").contains(status))
                        throw new Exception("Invalid status");
                    OrderDAO.updateStatus(Integer.parseInt(req.getParameter("orderId")), status);
                    session.setAttribute("flash", "Order status updated");
                    break;
                default:
                    break;
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            session.setAttribute("flash", "Cannot delete: this product is part of an existing order. Set its stock to 0 instead.");
        } catch (Exception e) {
            session.setAttribute("flash", "Error: " + e.getMessage());
        }
        resp.sendRedirect(req.getContextPath() + back);
    }
}