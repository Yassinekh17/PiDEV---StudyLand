package entities;

public class Apprenant extends User{
    public Apprenant(){
        super();
    }
    //avecID
    public Apprenant(int id,String nom, String prenom, String email, String password) {
        super(id,nom, prenom, email, password, "Apprenant");
    }
    //San mdp pour la modification
    public Apprenant(String nom, String prenom, String email,String role,int id) {
        super(nom, prenom, email,"Apprenant",id);
    }
    //sansID
    public Apprenant(String nom, String prenom, String email, String password,String confirmer_password){
        super(nom, prenom, email, password, "Apprenant",confirmer_password);
    }
}
