<%@ include file="partials/header.jsp" %>
<%
    String q = request.getParameter("q");
    int cat = 0;
    try { cat = Integer.parseInt(request.getParameter("cat")); } catch (Exception e) { }
    List<Product> products = ProductDAO.search(q, cat);
    List<Category> catList = ProductDAO.categories();
%>

<form class="card search" method="get" action="index.jsp">
  <input name="q" placeholder="Search products..." value="<%= Util.esc(q) %>">
  <select name="cat">
    <option value="0">All categories</option>
    <% for (Category c : catList) { %>
      <option value="<%= c.id() %>" <%= c.id() == cat ? "selected" : "" %>><%= Util.esc(c.name()) %></option>
    <% } %>
  </select>
  <button>Search</button>
</form>

<div class="grid">
<% for (Product p : products) { %>
  <div class="card product">
    <a href="product.jsp?id=<%= p.id() %>">
      <% if (p.image() != null && !p.image().isBlank()) { %>
        <img src="<%= Util.esc(p.image()) %>" alt="">
      <% } else { %>
        <div class="noimg"><%= Util.esc(p.name().substring(0, 1)) %></div>
      <% } %>
      <h3><%= Util.esc(p.name()) %></h3>
    </a>
    <p class="price">&#8377;<%= String.format("%.2f", p.price()) %></p>
    <% if (p.stock() > 0) { %>
      <form method="post" action="<%= ctx %>/cart-action">
        <input type="hidden" name="action" value="add">
        <input type="hidden" name="productId" value="<%= p.id() %>">
        <input type="hidden" name="qty" value="1">
        <button>Add to Cart</button>
      </form>
    <% } else { %>
      <p class="out">Out of stock</p>
    <% } %>
  </div>
<% } %>
</div>
<% if (products.isEmpty()) { %><p>No products found.</p><% } %>

<%@ include file="partials/footer.jsp" %>
