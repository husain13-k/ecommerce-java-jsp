<%@ include file="../partials/guard.jsp" %>
<%@ include file="../partials/header.jsp" %>
<%
    List<Product> allProducts = ProductDAO.search("", 0);
    List<Category> allCats = ProductDAO.categories();
%>
<h2>Manage Products</h2>

<div class="card">
  <h3>Add Category</h3>
  <form method="post" action="<%= ctx %>/admin-action">
    <input type="hidden" name="action" value="addCategory">
    <input name="name" placeholder="Category name" required>
    <button>Add Category</button>
  </form>
</div>

<div class="card">
  <h3>Add Product</h3>
  <form method="post" action="<%= ctx %>/admin-action">
    <input type="hidden" name="action" value="addProduct">
    <input name="name" placeholder="Product name" required>
    <textarea name="description" placeholder="Description"></textarea>
    <input type="number" step="0.01" min="0" name="price" placeholder="Price" required>
    <input type="number" min="0" name="stock" placeholder="Stock quantity" required>
    <input name="image" placeholder="Image URL (optional)">
    <select name="categoryId">
      <% for (Category c : allCats) { %>
        <option value="<%= c.id() %>"><%= Util.esc(c.name()) %></option>
      <% } %>
    </select>
    <button>Add Product</button>
  </form>
</div>

<h3>All Products</h3>
<% for (Product p : allProducts) { %>
  <div class="card">
    <form method="post" action="<%= ctx %>/admin-action">
      <input type="hidden" name="id" value="<%= p.id() %>">
      <input name="name" value="<%= Util.esc(p.name()) %>" required>
      <textarea name="description"><%= Util.esc(p.description()) %></textarea>
      <input type="number" step="0.01" min="0" name="price" value="<%= p.price() %>" required>
      <input type="number" min="0" name="stock" value="<%= p.stock() %>" required>
      <input name="image" value="<%= Util.esc(p.image()) %>" placeholder="Image URL">
      <select name="categoryId">
        <% for (Category c : allCats) { %>
          <option value="<%= c.id() %>" <%= c.id() == p.categoryId() ? "selected" : "" %>><%= Util.esc(c.name()) %></option>
        <% } %>
      </select>
      <button name="action" value="updateProduct">Update</button>
      <button name="action" value="deleteProduct" class="red" onclick="return confirm('Delete this product?')">Delete</button>
    </form>
  </div>
<% } %>
<%@ include file="../partials/footer.jsp" %>