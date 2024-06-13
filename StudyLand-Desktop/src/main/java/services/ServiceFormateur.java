package services;

import entities.Formateur;
import utils.MyDB;

import java.sql.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiceFormateur implements IUserService<Formateur>{
    private Connection connection;
    public ServiceFormateur(){
        connection= MyDB.getInstance().getConnection();
    }
    private boolean estAdminConnecte() throws SQLException {
        String req = "SELECT Role FROM user WHERE id_user = ?";
        try (PreparedStatement statement = connection.prepareStatement(req)) {
            statement.setInt(1, 1);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() && resultSet.getString("Role").equals("Admin");
            }
        }
    }
    @Override
    public void ajouter(Formateur formateur) throws SQLException {
        if (!estAdminConnecte()) {
            throw new IllegalStateException("Seul l'administrateur est autorisé à ajouter un nouvel  Formateur .");
        }

        String req = "INSERT INTO user (nom, prenom, email, password, Role) " +
                "VALUES ('" + formateur.getNom() + "', '" + formateur.getPrenom() + "', '" + formateur.getEmail() + "', '" + formateur.getPassword() + "', 'Formateur')";
        Statement ste =connection.createStatement();
        ste.executeUpdate(req);
    }

    public void modifier(Formateur formateur) throws SQLException {
        String req = "UPDATE user SET nom=?, prenom=? WHERE id_user=? ";
        PreparedStatement pre = connection.prepareStatement(req);
        pre.setString(1, formateur.getNom());
        pre.setString(2, formateur.getPrenom());
        pre.setInt(3, formateur.getId());
        pre.executeUpdate();
    }


    @Override
    public void supprimer(Formateur formateur) throws SQLException {
        String req ="delete from user where id_user=?";
        PreparedStatement pre=connection.prepareStatement(req);
        pre.setInt(1,formateur.getId());
        pre.executeUpdate();
    }

    @Override
    public List<Formateur> afficher() throws SQLException {
        String req = "SELECT * FROM user WHERE Role=?";
        PreparedStatement pre = connection.prepareStatement(req);
        pre.setString(1, "Formateur");
        ResultSet resultSet = pre.executeQuery();
        List<Formateur> formateurList = new ArrayList<>();
        while (resultSet.next()) {
            Formateur formateur = new Formateur();
            formateur.setId(resultSet.getInt("id_user"));
            formateur.setNom(resultSet.getString("nom"));
            formateur.setPrenom(resultSet.getString("prenom"));
            formateur.setEmail(resultSet.getString("email"));
            formateur.setRole(resultSet.getString("role"));
            formateur.setPassword(resultSet.getString("password"));
            formateurList.add(formateur);
        }        return formateurList;
    }

    //rechercheFormateurParEmail
    public Formateur rechercheFormateurParEmail(String email) throws SQLException {
        String req = "SELECT * FROM user WHERE role = 'Formateur' AND email = ?";
        PreparedStatement pre = connection.prepareStatement(req);
        pre.setString(1, email);
        ResultSet result = pre.executeQuery();
        if (result.next()) {
            int id = result.getInt("id_user");
            String nom = result.getString("nom");
            String prenom = result.getString("prenom");
            String adresseEmail = result.getString("email");
            String password = result.getString("password");
            return new Formateur(id, nom, prenom, adresseEmail, password);
        } else {
            return null;
        }
    }

}
