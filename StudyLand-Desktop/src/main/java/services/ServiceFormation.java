package services;

import entities.Formation;
import utils.MyDB;

import java.sql.*;
import java.sql.Date;
import java.util.*;


public class ServiceFormation implements IService<Formation> {

    private Connection connection ;

    public ServiceFormation(){
        connection = MyDB.getInstance().getConnection();
    }
    public boolean checkFormationExists(String titre) throws SQLException {
        String req = "SELECT COUNT(*) FROM formation WHERE titre=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(req)) {
            preparedStatement.setString(1, titre);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        }
        return false; // Return false if no result or an error occurs
    }
    @Override
    public void ajouter(Formation formation) throws SQLException {

        String req = "INSERT INTO formation(nomCategorie,titre, description, duree, dateDebut, dateFin, prix, niveau,id_user) VALUES (?, ?, ?, ?, ?, ?,?, ?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(req);
        preparedStatement.setString(1, formation.getNomCategorie());
        preparedStatement.setString(2, formation.getTitre());
        preparedStatement.setString(3, formation.getDescription());
        preparedStatement.setInt(4, formation.getDuree());
        preparedStatement.setDate(5, new java.sql.Date(formation.getDateDebut().getTime()));
        preparedStatement.setDate(6, new java.sql.Date(formation.getDateFin().getTime()));
        preparedStatement.setFloat(7, formation.getPrix());
        preparedStatement.setString(8, formation.getNiveau());
        preparedStatement.setInt(9, formation.getId_user());


        preparedStatement.executeUpdate();
    }

    @Override
    public void modifier(Formation formation) throws SQLException {
        String req = "UPDATE formation SET titre=? WHERE idFormation=?";
        try (PreparedStatement pre = connection.prepareStatement(req)) {
            // Log the values being set in the SQL statement
            System.out.println("Updating formation with ID: " + formation.getIdFormation() + ", New title: " + formation.getTitre());

            pre.setString(1, formation.getTitre());
            pre.setInt(2, formation.getIdFormation());
            pre.executeUpdate();

            System.out.println("Formation updated successfully.");
        } catch (SQLException e) {
            System.out.println("Error updating formation: " + e.getMessage());
            throw e; // Rethrow the exception to propagate it up the call stack
        }
    }


    public void modifierById(int id ,Formation formation) throws SQLException {
        String req = "UPDATE formation SET titre=? WHERE idFormation=?";
        try (PreparedStatement pre = connection.prepareStatement(req)) {
            // Log the values being set in the SQL statement
            System.out.println("Updating formation with ID:  syrine");
            pre.setString(1, formation.getTitre());
            pre.setInt(2, formation.getIdFormation());
            pre.executeUpdate();

            System.out.println("Formation updated successfully.");
        } catch (SQLException e) {
            System.out.println("Error updating formation: " + e.getMessage());
            throw e; // Rethrow the exception to propagate it up the call stack
        }
    }


    @Override
    public void supprimer(Formation formation) throws SQLException {
        // Delete related records in the cour_formation table
        deleteCourFormationByFormationId(formation.getIdFormation());

        // Delete the Formation from the formation table
        String req = "DELETE FROM formation WHERE idFormation=?";
        try (PreparedStatement pre = connection.prepareStatement(req)) {
            pre.setInt(1, formation.getIdFormation());
            pre.executeUpdate();
        }
    }
    public  void deleteCourFormationByFormationId(int formationId) throws SQLException {
        String req = "DELETE FROM cour_formation WHERE idFormation=?";
        try (PreparedStatement pre = connection.prepareStatement(req)) {
            pre.setInt(1, formationId);
            pre.executeUpdate();
        }
    }
    @Override
    public List<Formation> afficher() throws SQLException {
        String req = "SELECT * FROM formation";
        Statement ste = connection.createStatement();
        ResultSet res = ste.executeQuery(req);
        List<Formation> list = new ArrayList<>();

        int rowCount = 0; // Variable to track the number of rows processed

        while (res.next()) {
            Formation f = new Formation();
            f.setIdFormation(res.getInt("idFormation"));
            f.setTitre(res.getString("titre"));
            f.setDescription(res.getString("description"));
            f.setDuree(res.getInt("duree"));
            f.setDateDebut(res.getDate("dateDebut"));
            f.setDateFin(res.getDate("dateFin"));
            f.setPrix(res.getFloat("prix"));
            f.setNiveau(res.getString("niveau"));
            f.setNomCategorie(res.getString("nomCategorie")); // Set nomCategorie

            list.add(f);

            // Print the current row being processed
            System.out.println("Row " + (++rowCount) + ": " + f.toString() + "\n");
        }

        System.out.println("Total rows processed: " + rowCount); // Print total rows processed
        return list;
    }


    public static class TitreComparator implements Comparator<Formation> {
        @Override
        public int compare(Formation f1, Formation f2) {
            return f1.getTitre().compareTo(f2.getTitre());
        }
    }

    public Formation rechercherParTitre(String titre) throws SQLException {
        String req = "SELECT * FROM formation WHERE titre = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(req);
        preparedStatement.setString(1, titre);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            // If a result is found, construct a Formation object and return it
            Formation formation = new Formation();
            formation.setIdFormation(resultSet.getInt("idFormation"));
            formation.setTitre(resultSet.getString("titre"));
            formation.setDescription(resultSet.getString("description"));
            formation.setDuree(resultSet.getInt("duree"));
            formation.setDateDebut(resultSet.getDate("dateDebut"));
            formation.setDateFin(resultSet.getDate("dateFin"));
            formation.setPrix(resultSet.getFloat("prix"));
            formation.setNiveau(resultSet.getString("niveau"));
            return formation;
        } else {
            // If no result is found, return null or throw an exception
            return null;
            // Or throw new IllegalArgumentException("Formation with titre " + titre + " not found");
        }
    }
    public Formation rechercherParId(int id) throws SQLException {
        String req = "SELECT * FROM formation WHERE idFormation = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(req);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            // If a result is found, construct a Formation object and return it
            Formation formation = new Formation();
            formation.setIdFormation(resultSet.getInt("idFormation"));
            formation.setTitre(resultSet.getString("titre"));
            formation.setDescription(resultSet.getString("description"));
            formation.setDuree(resultSet.getInt("duree"));
            formation.setDateDebut(resultSet.getDate("dateDebut"));
            formation.setDateFin(resultSet.getDate("dateFin"));
            formation.setPrix(resultSet.getFloat("prix"));
            formation.setNiveau(resultSet.getString("niveau"));
            return formation;
        } else {
            // If no result is found, return null or throw an exception
            return null;
            // Or throw new IllegalArgumentException("Formation with ID " + id + " not found");
        }
    }
    //Syrine statistique ::
    public int statistiqueFormation(String catgorie) throws Exception {
        String req = "SELECT COUNT(*) FROM formation WHERE nomCategorie=?";
        PreparedStatement pre = connection.prepareStatement(req);
        pre.setString(1, catgorie);
        ResultSet resultSet = pre.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt(1); // Retourne le nombre d'utilisateurs
        } else {
            throw new Exception("La requête n'a pas retourné de résultat");
        }
    }
    public int statistiqueFormationFormateur(int id,String catgorie) throws Exception {
        String req = "SELECT COUNT(*) FROM formation WHERE id_user=? and nomCategorie=? ";
        PreparedStatement pre = connection.prepareStatement(req);
        pre.setInt(1, id);
        pre.setString(2, catgorie);
        ResultSet resultSet = pre.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt(1); // Retourne le nombre d'utilisateurs
        } else {
            throw new Exception("La requête n'a pas retourné de résultat");
        }
    }

}
