<%@ include file="partials/header.jsp" %>
<%
    Product prod = null;
    try { prod = ProductDAO.find(Integer.parseInt(request.getParameter("id"))); } catch (Exception e) { }
%>
<% if (prod == null) { %>
  <p>Product not found. <a href="index.jsp">Back to shop</a></p>
<% } else { %>
  <div class="card">
    <% if (prod.image() != null && !prod.image().isBlank()) { %>
      <img src="<%= Util.esc(prod.image()) %>" alt="" style="max-width:100%;max-height:320px;">
    <% } %>
    <h2><%= Util.esc(prod.name()) %></h2>
    <span class="badge"><%= Util.esc(prod.category()) %></span>
    <p><%= Util.esc(prod.description()) %></p>
    <p class="price">&#8377;<%= String.format("%.2f", prod.price()) %></p>
    <% if (prod.stock() > 0) { %>
      <p>In stock: <%= prod.stock() %></p>
      <form method="post" action="<%= ctx %>/cart-action">
        <input type="hidden" name="action" value="add">
        <input type="hidden" name="productId" value="<%= prod.id() %>">
        <input type="number" name="qty" value="1" min="1" max="<%= prod.stock() %>" style="width:100px">
        <button>Add to Cart</button>
      </form>
    <% } else { %>
      <p class="out">Out of stock</p>
    <% } %>
  </div>
<% } %>
<%@ include file="partials/footer.jsp" %>