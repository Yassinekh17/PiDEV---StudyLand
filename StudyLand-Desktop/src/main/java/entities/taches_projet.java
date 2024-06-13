package entities;
import entities.StatutTache;
import java.util.Date;

public class taches_projet {
    private int id_taches;
    private String nom_tache;
    private String Desc_tache;
    private Date Date_D;
    private Date Date_F;
    private StatutTache statut;
    private String Responsable;
    private int id_projet;

    public int getId_taches() {
        return id_taches;
    }

    public void setId_taches(int id_taches) {
        this.id_taches = id_taches;
    }

    public String getNom_tache() {
        return nom_tache;
    }

    public void setNom_tache(String nom_tache) {
        this.nom_tache = nom_tache;
    }

    public String getDesc_tache() {
        return Desc_tache;
    }

    public void setDesc_tache(String desc_tache) {
        Desc_tache = desc_tache;
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

    public StatutTache getStatut() {
        return statut;
    }

    public void setStatut(StatutTache statut) {
        this.statut = statut;
    }

    public String getResponsable() {
        return Responsable;
    }

    public void setResponsable(String responsable) {
        Responsable = responsable;
    }

    public int getId_projet() {
        return id_projet;
    }

    public void setId_projet(int id_projet) {
        this.id_projet = id_projet;
    }

    @Override
    public String toString() {
        return "taches_projet{" +
                "id_taches=" + id_taches +
                ", nom_tache='" + nom_tache + '\'' +
                ", Desc_tache='" + Desc_tache + '\'' +
                ", Date_D=" + Date_D +
                ", Date_F=" + Date_F +
                ", statut=" + statut +
                ", Responsable='" + Responsable + '\'' +
                ", id_projet=" + id_projet +
                '}';
    }

    public taches_projet() {
    }

    public taches_projet(int id_taches, String nom_tache, String desc_tache, Date date_D, Date date_F, StatutTache statut, String responsable, int id_projet) {
        this.id_taches = id_taches;
        this.nom_tache = nom_tache;
        Desc_tache = desc_tache;
        Date_D = date_D;
        Date_F = date_F;
        this.statut = statut;
        Responsable = responsable;
        this.id_projet = id_projet;
    }
    public taches_projet( String nom_tache, String desc_tache, Date date_D, Date date_F, StatutTache statut, String responsable, int id_projet) {
        this.nom_tache = nom_tache;
        Desc_tache = desc_tache;
        Date_D = date_D;
        Date_F = date_F;
        this.statut = statut;
        Responsable = responsable;
        this.id_projet = id_projet;
    }

    public taches_projet(String nom_tache, String desc_tache, Date date_D, Date date_F, StatutTache statut, String responsable) {
        this.nom_tache = nom_tache;
        Desc_tache = desc_tache;
        Date_D = date_D;
        Date_F = date_F;
        this.statut = statut;
        Responsable = responsable;

    }
}
