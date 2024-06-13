package entities;
import java.time.LocalDate;
import java.util.Date;

public class Project {
    private int id_Projet;
    private String NomProjet;
    private String DescProjet;
    private Date Date_D;
    private Date Date_F;
    private int nb_taches;

    public int getId_Projet() {
        return id_Projet;
    }

    public void setId_Projet(int id_Projet) {
        this.id_Projet = id_Projet;
    }

    public String getNomProjet() {
        return NomProjet;
    }

    public void setNomProjet(String nomProjet) {
        NomProjet = nomProjet;
    }

    public String getDescProjet() {
        return DescProjet;
    }

    public void setDescProjet(String descProjet) {
        DescProjet = descProjet;
    }

    public Date getDate_D() {
        return Date_D;
    }

    public void setDate_D(Date date_D) {
        Date_D = date_D;
    }

    public Date getDate_F() {
        return Date_F;
    }

    public void setDate_F(Date date_F) {
        Date_F = date_F;
    }

    public int getNb_taches() {
        return nb_taches;
    }

    public void setNb_taches(int nb_taches) {
        this.nb_taches = nb_taches;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id_Projet=" + id_Projet +
                ", NomProjet='" + NomProjet + '\'' +
                ", DescProjet='" + DescProjet + '\'' +
                ", Date_D=" + Date_D +
                ", Date_F=" + Date_F +
                ", nb_taches=" + nb_taches +
                '}';
    }

    public Project() {
    }

    public Project(int id_Projet, String nomProjet, String descProjet, Date date_D, Date date_F, int nb_taches) {
        this.id_Projet = id_Projet;
        NomProjet = nomProjet;
        DescProjet = descProjet;
        Date_D = date_D;
        Date_F = date_F;
        this.nb_taches = nb_taches;
    }

    public Project(String nomProjet, String descProjet, Date date_D, Date date_F, int nb_taches) {
        NomProjet = nomProjet;
        DescProjet = descProjet;
        Date_D = date_D;
        Date_F = date_F;
        this.nb_taches = nb_taches;
    }
}

