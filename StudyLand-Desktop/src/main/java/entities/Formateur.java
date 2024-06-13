package entities;

public class Formateur extends User{
    public Formateur(){
        super();
    }
    //avecID
    public Formateur(int id,String nom, String prenom, String email, String password) {
        super( id,nom, prenom, email, password, "Formateur");
    }
    //sansID
    public Formateur(String nom, String prenom, String email, String password){
        super(nom, prenom, email, password, "Formateur");
    }
    //pour modification
    public Formateur(String nom, String prenom, String email,String  role,int id) {
        super( nom, prenom, email,"Formateur",id);
    }
    public Formateur(String nom, String prenom, String email, String password,String confirmer_password){
        super(nom, prenom, email, password, "Apprenant",confirmer_password);
    }
}
