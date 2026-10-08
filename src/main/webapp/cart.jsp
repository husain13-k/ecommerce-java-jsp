<%@ include file="partials/header.jsp" %>
<h2>Your Cart</h2>
<%
    double cartTotal = 0;
    if (cartMap == null || cartMap.isEmpty()) {
%>
  <p>Your cart is empty. <a href="index.jsp">Continue shopping</a></p>
<%
    } else {
        for (Map.Entry<Integer, Integer> en : cartMap.entrySet()) {
            Product cp = ProductDAO.find(en.getKey());
            if (cp == null) continue;
            double line = cp.price() * en.getValue();
            cartTotal += line;
%>
  <div class="card row">
    <div>
      <b><%= Util.esc(cp.name()) %></b><br>
      &#8377;<%= String.format("%.2f", cp.price()) %> each
    </div>
    <form class="inline" method="post" action="<%= ctx %>/cart-action">
      <input type="hidden" name="action" value="update">
      <input type="hidden" name="productId" value="<%= cp.id() %>">
      <input type="number" name="qty" value="<%= en.getValue() %>" min="0" max="<%= cp.stock() %>">
      <button>Update</button>
    </form>
    <div>&#8377;<%= String.format("%.2f", line) %></div>
    <form class="inline" method="post" action="<%= ctx %>/cart-action">
      <input type="hidden" name="action" value="remove">
      <input type="hidden" name="productId" value="<%= cp.id() %>">
      <button class="red">Remove</button>
    </form>
  </div>
<%  } %>

  <h3>Total: &#8377;<%= String.format("%.2f", cartTotal) %></h3>

  <% if (me == null) { %>
    <p><a href="login.jsp">Login</a> to place your order.</p>
  <% } else { %>
    <form class="card" method="post" action="<%= ctx %>/place-order">
      <h3>Delivery address</h3>
      <textarea name="address" placeholder="House no, street, city, pincode" required></textarea>
      <button>Place Order (Cash on Delivery)</button>
    </form>
  <% } %>
<% } %>
<%@ include file="partials/footer.jsp" %>