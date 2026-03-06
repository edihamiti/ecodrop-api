package dto;

/**
 * DTO normalisant les informations utilisateur provenant de n'importe quel provider OAuth.
 * Tous les champs sont garantis non-null après extraction.
 */
public record UserInfo(String email, String pseudo) {}
