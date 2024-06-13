package services;

import entities.Apprenant;
import utils.MyDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceApprenant implements IUserService<Apprenant> {
    private static Connection connection;

    public ServiceApprenant() {
        connection = MyDB.getInstance().getConnection();
    }

    @Override
    public void ajouter(Apprenant apprenant) throws SQLException {
        if (apprenant.isEmailValid(apprenant.getEmail()) && apprenant.getConfirmerPassword().equals(apprenant.getPassword())) {
            String req = "INSERT INTO user (nom, prenom, email, password, confirmer_password, Role) " +
                    "VALUES (?, ?, ?, ?, ?, 'Apprenant')"; // Utilisation de PreparedStatement pour les valeurs paramétrées
            PreparedStatement preparedStatement = connection.prepareStatement(req);
            preparedStatement.setString(1, apprenant.getNom());
            preparedStatement.setString(2, apprenant.getPrenom());
            preparedStatement.setString(3, apprenant.getEmail());
            preparedStatement.setString(4, apprenant.getPassword());
            preparedStatement.setString(5, apprenant.getConfirmerPassword());
            preparedStatement.executeUpdate();
            preparedStatement.close();
            //    EmailSender.sendNotificationEmail(apprenant.getEmail());
        } else {
            System.out.println("L'e-mail n'est pas valide ou le mot de passe ne correspond pas au mot de passe de confirmation.");
        }
    }


    //Modifier sans MPdp:
    @Override
    public void modifier(Apprenant apprenant) throws SQLException {
        String req = "UPDATE user SET nom=?, prenom=? WHERE id_user=?";
        PreparedStatement pre = connection.prepareStatement(req);
        pre.setString(1, apprenant.getNom());
        pre.setString(2, apprenant.getPrenom());
        pre.setInt(3, apprenant.getId());
        pre.executeUpdate();
    }


    @Override
    public void supprimer(Apprenant apprenant) throws SQLException {
        String req = "delete from user where id_user=?";
        PreparedStatement pre = connection.prepareStatement(req);
        pre.setInt(1, apprenant.getId());
        pre.executeUpdate();
    }


    @Override
    public List<Apprenant> afficher() throws SQLException {
        String req = "SELECT * FROM user WHERE Role=?";
        PreparedStatement pre = connection.prepareStatement(req);
        pre.setString(1, "Apprenant");
        ResultSet resultSet = pre.executeQuery();

        List<Apprenant> ApprenatList = new ArrayList<>();
        while (resultSet.next()) {
            Apprenant apprenant = new Apprenant();
            apprenant.setId(resultSet.getInt("id_user"));
            apprenant.setNom(resultSet.getString("nom"));
            apprenant.setPrenom(resultSet.getString("prenom"));
            apprenant.setEmail(resultSet.getString("email"));
            apprenant.setRole(resultSet.getString("role"));
            apprenant.setPassword(resultSet.getString("password"));
            ApprenatList.add(apprenant);
        }
        return ApprenatList;
    }

    //Rechercher  rechercheApprenantParEmail
    public Apprenant rechercheApprenantParEmail(String email) throws SQLException {
        String req = "SELECT * FROM user WHERE role = 'Apprenant' AND email = ?";
        PreparedStatement pre = connection.prepareStatement(req);
        pre.setString(1, email);
        ResultSet result = pre.executeQuery();
        if (result.next()) {
            int id = result.getInt("id_user");
            String nom = result.getString("nom");
            String prenom = result.getString("prenom");
            String adresseEmail = result.getString("email");
            String password = result.getString("password");
            return new Apprenant(id, nom, prenom, adresseEmail, password);
        } else {
            return null;
        }
    }

    // ApprenantExiste
    public int existeApprenant(Apprenant apprenant) throws SQLException {
        String query = "SELECT COUNT(*) FROM user WHERE id_user=?";
        PreparedStatement pre = connection.prepareStatement(query);
        pre.setInt(1, apprenant.getId());
        ResultSet resultSet = pre.executeQuery();

        int count = 1;
        return count;
    }
    //Upgrate
    public void Upgrate(int id, String email) throws Exception {
        String req = "UPDATE user SET role=? WHERE email=?  And  id_user=?";
        PreparedStatement pre = connection.prepareStatement(req);
        pre.setString(1, "Formateur");
        pre.setString(2, email);
        pre.setInt(3,id);
        pre.executeUpdate();
    }

}