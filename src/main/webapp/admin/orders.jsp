<%@ include file="../partials/guard.jsp" %>
<%@ include file="../partials/header.jsp" %>
<h2>All Orders</h2>
<%
    String[] statuses = {"PENDING", "SHIPPED", "DELIVERED"};
    List<Order> everyOrder = OrderDAO.all();
    for (Order o : everyOrder) {
%>
  <div class="card">
    <div class="row">
      <b>Order #<%= o.id() %> &mdash; <%= Util.esc(o.customer()) %></b>
      <form class="inline" method="post" action="<%= ctx %>/admin-action">
        <input type="hidden" name="action" value="status">
        <input type="hidden" name="orderId" value="<%= o.id() %>">
        <select name="status" style="width:150px" onchange="this.form.submit()">
          <% for (String s : statuses) { %>
            <option <%= s.equals(o.status()) ? "selected" : "" %>><%= s %></option>
          <% } %>
        </select>
      </form>
    </div>
    <p><%= o.date() %></p>
    <ul>
      <% for (OrderItem it : OrderDAO.items(o.id())) { %>
        <li><%= Util.esc(it.name()) %> &times; <%= it.quantity() %> &mdash; &#8377;<%= String.format("%.2f", it.price()) %></li>
      <% } %>
    </ul>
    <p>Address: <%= Util.esc(o.address()) %></p>
    <b>Total: &#8377;<%= String.format("%.2f", o.total()) %></b>
  </div>
<% } %>
<% if (everyOrder.isEmpty()) { %><p>No orders yet.</p><% } %>
<%@ include file="../partials/footer.jsp" %>