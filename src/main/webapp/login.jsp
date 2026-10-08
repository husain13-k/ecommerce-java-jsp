<%@ include file="partials/header.jsp" %>
<div class="card narrow">
  <h2>Login</h2>
  <form method="post" action="<%= ctx %>/auth">
    <input type="hidden" name="action" value="login">
    <input type="email" name="email" placeholder="Email" required>
    <input type="password" name="password" placeholder="Password" required>
    <button>Login</button>
  </form>
  <p>New here? <a href="register.jsp">Create an account</a></p>
</div>
<%@ include file="partials/footer.jsp" %>