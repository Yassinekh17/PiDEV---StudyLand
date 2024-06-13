package services;

import entities.Cours;
import entities.Formation;
import utils.MyDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiceCours implements IService<Cours>{
    private Connection connection ;
    public ServiceCours(){
        connection = MyDB.getInstance().getConnection();
    }
    @Override
    public void ajouter(Cours cours) throws SQLException {
        // Préparer la requête SQL
        String sql = "INSERT INTO cour_formation (Nom_Cours, Description_Cours, idFormation) VALUES (?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);

        // Définir les valeurs des paramètres
        statement.setString(1, cours.getNom_Cours());
        statement.setBytes(2, cours.getDescription_Cours());
        statement.setInt(3, cours.getIdFormation());

        // Exécuter la requête
        statement.executeUpdate();
    }
    @Override
    public void modifier(Cours cours) throws SQLException {
        String sql = "UPDATE cour_formation SET Nom_Cours = ? WHERE idCour = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, cours.getNom_Cours());
        statement.setInt(2, cours.getIdCour());
        statement.executeUpdate();
    }
    public boolean checkCourseExists(String courseName, int formationId) throws SQLException {
        String query = "SELECT COUNT(*) AS count FROM cour_formation WHERE Nom_Cours = ? AND idFormation = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, courseName);
            statement.setInt(2, formationId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt("count");
                return count > 0; // If count is greater than 0, course exists
            }
        }
        return false; // If no course with the given name and formation ID is found
    }
    @Override
    public void supprimer(Cours cours) throws SQLException {
        // Préparer la requête SQL
        String sql = "DELETE FROM cour_formation WHERE idCour = ?";
        PreparedStatement statement = connection.prepareStatement(sql);

        // Définir les valeurs des paramètres
        statement.setInt(1, cours.getIdCour());

        // Exécuter la requête
        statement.executeUpdate();

    }
    // New method to delete related records by idFormation
    public void deleteCourById(int IdCour) throws SQLException {
        String sql = "DELETE FROM cour_formation WHERE IdCour = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, IdCour);
        statement.executeUpdate();
    }
    @Override
    public List<Cours> afficher() throws SQLException {
        String sql = "SELECT * FROM cour_formation";
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();

        List<Cours> coursList = new ArrayList<>();

        while (resultSet.next()) {
            Cours cours = new Cours();
            cours.setIdCour(resultSet.getInt("idCour"));
            cours.setNom_Cours(resultSet.getString("Nom_Cours"));
            cours.setDescription_Cours(resultSet.getBytes("Description_Cours"));
            cours.setIdFormation(resultSet.getInt("idFormation"));
            coursList.add(cours);
        }

        return coursList;
    }

}
