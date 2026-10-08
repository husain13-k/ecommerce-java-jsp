<%@ page import="model.User" %>
<%
    // Include this BEFORE header.jsp on admin pages: blocks non-admins
    User guardUser = (User) session.getAttribute("user");
    if (guardUser == null || !"ADMIN".equals(guardUser.role())) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
%>