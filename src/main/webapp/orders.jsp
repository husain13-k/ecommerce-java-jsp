<% if (session.getAttribute("user") == null) { response.sendRedirect(request.getContextPath() + "/login.jsp"); return; } %>
<%@ include file="partials/header.jsp" %>
<h2>My Orders</h2>
<%
    List<Order> myOrders = OrderDAO.forUser(me.id());
    for (Order o : myOrders) {
%>
  <div class="card">
    <div class="row">
      <b>Order #<%= o.id() %></b>
      <span class="badge"><%= o.status() %></span>
    </div>
    <p>Placed on: <%= o.date() %></p>
    <ul>
      <% for (OrderItem it : OrderDAO.items(o.id())) { %>
        <li><%= Util.esc(it.name()) %> &times; <%= it.quantity() %> &mdash; &#8377;<%= String.format("%.2f", it.price()) %></li>
      <% } %>
    </ul>
    <p>Deliver to: <%= Util.esc(o.address()) %></p>
    <b>Total: &#8377;<%= String.format("%.2f", o.total()) %></b>
  </div>
<% } %>
<% if (myOrders.isEmpty()) { %><p>You have no orders yet.</p><% } %>
<%@ include file="partials/footer.jsp" %>