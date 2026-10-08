package servlet;

import dao.UserDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.User;

import java.io.IOException;

@WebServlet("/auth")
public class AuthServlet extends HttpServlet {

    // logout: /auth?action=logout
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if ("logout".equals(req.getParameter("action"))) {
            req.getSession().invalidate();
        }
        resp.sendRedirect(req.getContextPath() + "/index.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession();
        String ctx = req.getContextPath();
        String action = req.getParameter("action");
        String email = req.getParameter("email") == null ? "" : req.getParameter("email").trim().toLowerCase();
        String password = req.getParameter("password") == null ? "" : req.getParameter("password");

        if ("register".equals(action)) {
            String name = req.getParameter("name") == null ? "" : req.getParameter("name").trim();
            if (name.isEmpty() || email.isEmpty() || password.length() < 6) {
                session.setAttribute("flash", "Fill all fields. Password must be at least 6 characters.");
                resp.sendRedirect(ctx + "/register.jsp");
                return;
            }
            String error = UserDAO.register(name, email, password);
            if (error != null) {
                session.setAttribute("flash", error);
                resp.sendRedirect(ctx + "/register.jsp");
            } else {
                session.setAttribute("flash", "Registration successful. Please login.");
                resp.sendRedirect(ctx + "/login.jsp");
            }

        } else if ("login".equals(action)) {
            User user = UserDAO.login(email, password);
            if (user == null) {
                session.setAttribute("flash", "Invalid email or password");
                resp.sendRedirect(ctx + "/login.jsp");
                return;
            }
            req.changeSessionId();            // security: new session id after login
            session = req.getSession();
            session.setAttribute("user", user);
            resp.sendRedirect(ctx + ("ADMIN".equals(user.role()) ? "/admin/products.jsp" : "/index.jsp"));

        } else {
            resp.sendRedirect(ctx + "/index.jsp");
        }
    }
}
