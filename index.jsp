<%@ page pageEncoding="UTF-8" %>
<%@ page import="security.OAuthProvider" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Login</title>
</head>
<body>
<h1>Login</h1>
<% for (OAuthProvider provider : OAuthProvider.values()) { %>
    <a href="<%= provider.buildAuthorizeUrl() %>"><%= provider.getDisplayName() %></a>
<% } %>
</body>
</html>