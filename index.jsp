<%@ page pageEncoding="UTF-8" %>
<%@ page import="security.OAuthProvider" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Connexion — EcoDrop</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="res/style.css">
</head>
<body>
    <div class="login-card">
        <i class="bi bi-recycle logo-icon"></i>
        <h1>Bienvenue</h1>
        <p class="subtitle">Connectez-vous à votre espace EcoDrop</p>

        <% for (OAuthProvider p : OAuthProvider.values()) { %>
            <a href="<%= p.buildAuthorizeUrl() %>" class="btn-oauth">
                <i class="bi <%= p.getIcon() %>" style="color: <%= p.getColor() %>"></i>
                Continuer avec <%= p.getDisplayName() %>
            </a>
        <% } %>

        <div class="footer">&copy; 2026 EcoDrop</div>
    </div>
</body>
</html>