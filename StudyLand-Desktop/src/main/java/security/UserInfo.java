package security;

public class UserInfo {
   public String nom;
    public String prenom;
    public String role;
    public String email;
    public String mdp;
    public String image;
  public Integer id;

   public UserInfo(int id,String nom, String prenom, String role, String email, String mdp) {
      this.id=id;
      this.nom = nom;
      this.prenom = prenom;
      this.role = role;
      this.email = email;
      this.mdp = mdp;
   }
    public UserInfo(int id,String nom, String prenom, String role, String email, String mdp,String image ) {
        this.id=id;
        this.nom = nom;
        this.prenom = prenom;
        this.role = role;
        this.email = email;
        this.image = image;
        this.mdp = mdp;
    }

}
