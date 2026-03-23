package dto;

// Non-utilisation d'un record pour pouvoir utiliser la methode merge de MergeUtils (record met les attributs en final)
public class User {
    private Integer id;
    private String login;
    private String role;

    public User() {
    }

    public User(Integer id, String login, String role) {
         this.id = id;
         this.login = login;
         this.role = role;
    }

    public Integer getId(){
        return this.id;
    }

    public void setId(Integer id){
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
