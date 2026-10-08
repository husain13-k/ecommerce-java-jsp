<%@ include file="partials/header.jsp" %>
<div class="card narrow">
  <h2>Register</h2>
  <form method="post" action="<%= ctx %>/auth">
    <input type="hidden" name="action" value="register">
    <input name="name" placeholder="Full name" required>
    <input type="email" name="email" placeholder="Email" required>
    <input type="password" name="password" placeholder="Password (min 6 characters)" required>
    <button>Register</button>
  </form>
  <p>Already have an account? <a href="login.jsp">Login</a></p>
</div>
<%@ include file="partials/footer.jsp" %>