package entities;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class Formation {
    private int idFormation;
    private String titre;
    private String description;
    private int duree;
    private Date dateDebut;
    private Date dateFin;
    private float prix;
    private String niveau;
    private String nomCategorie;
    private  int id_user;

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    //Constructeur pour l'ajout par Id


    public String getNomCategorie() {
        return nomCategorie;
    }

    public void setNomCategorie(String nomCategorie) {
        this.nomCategorie = nomCategorie;
    }

    public int getIdFormation() {
        return idFormation;
    }

    public void setIdFormation(int idFormation) {
        this.idFormation = idFormation;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public String getNiveau() {
        return niveau;
    }

    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }
    private String dateToString(Date date) {
        // Format the date as "dd/MM/yyyy"
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(date);
    }

    @Override
    public String toString() {
        return "Formation{" +
                "idFormation=" + idFormation +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", duree=" + duree +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", prix=" + prix +
                ", niveau='" + niveau  +
                ", nomCategorie='" + nomCategorie + '\'' +
                '}';
    }

    public Formation() {
    }

    public Formation(int idFormation,String nomCategorie, String titre, String description, int duree, Date dateDebut, Date dateFin, float prix, String niveau) {
        this.idFormation = idFormation;
        this.titre = titre;
        this.description = description;
        this.duree = duree;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.prix = prix;
        this.niveau = niveau;
        this.nomCategorie = nomCategorie;

    }
    public Formation(String titre, String description, int duree, Date dateDebut, Date dateFin, float prix, String niveau, String nomCategorie,int id_user) {
        this.titre = titre;
        this.description = description;
        this.duree = duree;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.prix = prix;
        this.id_user=id_user;
        this.niveau = niveau;
        this.nomCategorie = nomCategorie;
    }
    public Formation(String titre, String description, int duree, Date dateDebut, Date dateFin, float prix, String niveau, String nomCategorie) {
        this.titre = titre;
        this.description = description;
        this.duree = duree;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.prix = prix;
        this.niveau = niveau;
        this.nomCategorie = nomCategorie;
    }

    public Formation(int idFormation, String titre) {
        this.idFormation = idFormation;
        this.titre = titre;
    }
}
