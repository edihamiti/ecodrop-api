<%@ page pageEncoding="UTF-8" %>
<%@ page import="utils.Config" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="java.net.URLEncoder" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Login</title>
</head>
<body>
<%
    String gitlabURL = "https://gitlab.univ-lille.fr/oauth/authorize?client_id="+Config.get("gitlab.client_id")+"&redirect_uri="+ URLEncoder.encode(Config.get("redirect_uri"))+"%3Ffrom%3Dgitlab&scope=email+profile&response_type=code";
    String googleURL = "https://accounts.google.com/o/oauth2/v2/auth?client_id="+Config.get("google.client_id")+"&redirect_uri="+URLEncoder.encode(Config.get("redirect_uri"))+"%3Ffrom%3Dgoogle&scope=openid%20email%20profile&response_type=code";
    String discordURL = "https://discord.com/oauth2/authorize?client_id="+Config.get("discord.client_id")+"&response_type=code&redirect_uri="+URLEncoder.encode(Config.get("redirect_uri"))+"%3Ffrom%3Ddiscord&scope=identify+email";
%>
<h1>Login</h1>
<a href=<%=gitlabURL%>>GitLab Universitaire</a>
<a href=<%=discordURL%>>Discord</a>
<a href=<%=googleURL%>>Goggle</a>
</body>
</html>