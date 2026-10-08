package servlet;

import dao.OrderDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.User;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@WebServlet("/place-order")
public class OrderServlet extends HttpServlet {

    @Override
    @SuppressWarnings("unchecked")
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession();
        String ctx = req.getContextPath();

        User user = (User) session.getAttribute("user");
        if (user == null) {
            resp.sendRedirect(ctx + "/login.jsp");
            return;
        }

        Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("cart");
        String address = req.getParameter("address");
        if (cart == null || cart.isEmpty() || address == null || address.isBlank()) {
            session.setAttribute("flash", "Your cart is empty or the address is missing");
            resp.sendRedirect(ctx + "/cart.jsp");
            return;
        }

        try {
            OrderDAO.place(user.id(), new LinkedHashMap<>(cart), address.trim());
            session.removeAttribute("cart");
            session.setAttribute("flash", "Order placed successfully!");
            resp.sendRedirect(ctx + "/orders.jsp");
        } catch (Exception e) {
            session.setAttribute("flash", e.getMessage());
            resp.sendRedirect(ctx + "/cart.jsp");
        }
    }
}