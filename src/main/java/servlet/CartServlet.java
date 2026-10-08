package servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

// The cart is a Map<productId, quantity> stored in the user's session.
@WebServlet("/cart-action")
public class CartServlet extends HttpServlet {

    @Override
    @SuppressWarnings("unchecked")
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("cart");
        if (cart == null) {
            cart = new LinkedHashMap<>();
            session.setAttribute("cart", cart);
        }

        try {
            String action = req.getParameter("action");
            int productId = Integer.parseInt(req.getParameter("productId"));
            int qty = 1;
            if (req.getParameter("qty") != null) qty = Integer.parseInt(req.getParameter("qty"));

            switch (action) {
                case "add":
                    cart.merge(productId, Math.max(qty, 1), Integer::sum);
                    session.setAttribute("flash", "Added to cart");
                    break;
                case "update":
                    if (qty <= 0) cart.remove(productId);
                    else cart.put(productId, qty);
                    break;
                case "remove":
                    cart.remove(productId);
                    break;
                default:
                    break;
            }
        } catch (NumberFormatException e) {
            session.setAttribute("flash", "Invalid quantity");
        }
        resp.sendRedirect(req.getContextPath() + "/cart.jsp");
    }
}