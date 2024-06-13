package entities;

import java.util.Arrays;

public class Cours {
    private int IdCour;
    private String Nom_Cours;
    private  byte[] Description_Cours;
    private int idFormation;

    public int getIdCour() {
        return IdCour;
    }

    public void setIdCour(int idCour) {
        IdCour = idCour;
    }

    public String getNom_Cours() {
        return Nom_Cours;
    }

    public void setNom_Cours(String nom_Cours) {
        Nom_Cours = nom_Cours;
    }

    public byte[] getDescription_Cours() {
        return Description_Cours;
    }

    public void setDescription_Cours(byte[] description_Cours) {
        Description_Cours = description_Cours;
    }

    public int getIdFormation() {
        return idFormation;
    }

    public void setIdFormation(int idFormation) {
        this.idFormation = idFormation;
    }

    public Cours(String nom_Cours, byte[] description_Cours, int idFormation) {
        this.Nom_Cours = nom_Cours;
        this.Description_Cours = description_Cours;
        this.idFormation = idFormation;
    }

    @Override
    public String toString() {
        return "Cours{" +
                "IdCour=" + IdCour +
                ", Nom_Cours='" + Nom_Cours + '\'' +
                ", Description_Cours=" + Arrays.toString(Description_Cours) +
                ", idFormation=" + idFormation +
                '}';
    }

    public Cours(int idCour, String nom_Cours, byte[] description_Cours, int idFormation) {
        IdCour = idCour;
        Nom_Cours = nom_Cours;
        Description_Cours = description_Cours;
        this.idFormation = idFormation;
    }

    public Cours() {
    }
    public byte[] getPdfContent() {
        return Description_Cours;
    }

}
