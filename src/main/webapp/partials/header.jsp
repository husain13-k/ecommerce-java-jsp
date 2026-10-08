<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" import="java.util.*,model.*,dao.*,util.*" %>
<%
    User me = (User) session.getAttribute("user");
    String ctx = request.getContextPath();
    Map<Integer, Integer> cartMap = (Map<Integer, Integer>) session.getAttribute("cart");
    int cartCount = 0;
    if (cartMap != null) for (int n : cartMap.values()) cartCount += n;
%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>ShopEasy</title>
  <link rel="stylesheet" href="<%= ctx %>/css/style.css">
</head>
<body>
<header class="nav">
  <a class="brand" href="<%= ctx %>/index.jsp">ShopEasy</a>
  <nav>
    <a href="<%= ctx %>/index.jsp">Home</a>
    <a href="<%= ctx %>/cart.jsp">Cart (<%= cartCount %>)</a>
    <% if (me == null) { %>
      <a href="<%= ctx %>/login.jsp">Login</a>
      <a href="<%= ctx %>/register.jsp">Register</a>
    <% } else { %>
      <a href="<%= ctx %>/orders.jsp">My Orders</a>
      <% if ("ADMIN".equals(me.role())) { %>
        <a href="<%= ctx %>/admin/products.jsp">Admin Products</a>
        <a href="<%= ctx %>/admin/orders.jsp">Admin Orders</a>
      <% } %>
      <a href="<%= ctx %>/auth?action=logout">Logout (<%= Util.esc(me.name()) %>)</a>
    <% } %>
  </nav>
</header>
<main class="container">
<%
    String flash = (String) session.getAttribute("flash");
    if (flash != null) {
        session.removeAttribute("flash");
%>
  <div class="flash"><%= Util.esc(flash) %></div>
<% } %>