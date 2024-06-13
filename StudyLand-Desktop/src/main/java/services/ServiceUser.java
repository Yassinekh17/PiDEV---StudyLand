package services;

import entities.User;
import utils.MyDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ServiceUser {
    private Connection connection;
    ServiceAdmin serviceAdmin=new ServiceAdmin();
    ServiceFormateur serviceFormateur=new ServiceFormateur();
    ServiceApprenant serviceApprenant=new ServiceApprenant();
    public ServiceUser() {
        connection = MyDB.getInstance().getConnection();
    }


    //Modifier User

    //RecherUser by email
    public User rechercheUserParEmail(String email) throws SQLException {
        String req = "SELECT * FROM user WHERE  email = ?";
        PreparedStatement pre = connection.prepareStatement(req);
        pre.setString(1, email);
        ResultSet result = pre.executeQuery();
        if (result.next()) {
            int id = result.getInt("id_user");
            String nom = result.getString("nom");
            String prenom = result.getString("prenom");
            String adresseEmail = result.getString("email");
            String password = result.getString("password");
            String Confirme_password = result.getString("password");
            String role = result.getString("Role");
            String image=result.getString("image");
            return new User(id, nom, prenom, adresseEmail, password,role,Confirme_password,image);
        } else {
            return null;
        }
    }

//Update
public  User UpdateMdp(User user,String password) throws  Exception{
    String req = "UPDATE user SET password=?, confirmer_password=? WHERE id_user=?";
    PreparedStatement pre = connection.prepareStatement(req);
    pre.setString(1, password);
    pre.setString(2, password);
    pre.setInt(3, user.getId());
    pre.executeUpdate();
    return new User(user.getId(), user.getNom(), user.getPrenom(), user.getEmail(), password, user.getRole(),password);
    }

    public User connexion(String email,String password)throws  Exception{
        String req = "SELECT * FROM user WHERE email=? and password=?";
        PreparedStatement pre = connection.prepareStatement(req);
        pre.setString(1, email);
        pre.setString(2, password);
        ResultSet resultSet = pre.executeQuery();
        while (resultSet.next()){
            return new User(resultSet.getInt("id_user"),resultSet.getString("nom"),resultSet.getString("prenom"),resultSet.getString("email"),resultSet.getString("password"),resultSet.getString("role"));
        }
        return  null;
    }
    // fct pour dashboard
    public int countApp() throws Exception {
        String req = "SELECT COUNT(*) FROM user WHERE role=?";
        PreparedStatement pre = connection.prepareStatement(req);
        pre.setString(1, "Apprenant");
        ResultSet resultSet = pre.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt(1); // Retourne le nombre d'utilisateurs
        } else {
            throw new Exception("La requête n'a pas retourné de résultat");
        }
    }
    public int countFomateur() throws Exception {
        String req = "SELECT COUNT(*) FROM user WHERE role=?";
        PreparedStatement pre = connection.prepareStatement(req);
        pre.setString(1, "Formateur");
        ResultSet resultSet = pre.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt(1); // Retourne le nombre d'utilisateurs
        } else {
            throw new Exception("La requête n'a pas retourné de résultat");
        }
    }
    public int countAdmin() throws Exception {
        String req = "SELECT COUNT(*) FROM user WHERE role=?";
        PreparedStatement pre = connection.prepareStatement(req);
        pre.setString(1, "Admin");
        ResultSet resultSet = pre.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt(1); // Retourne le nombre d'utilisateurs
        } else {
            throw new Exception("La requête n'a pas retourné de résultat");
        }
    }
    //get all user
    public List<User> afficher() throws SQLException {
        String req = "SELECT * FROM user  ";
        PreparedStatement pre = connection.prepareStatement(req);
        ResultSet resultSet = pre.executeQuery();
        List<User> UserList = new ArrayList<>();
        while (resultSet.next()) {
            User user = new User();
            user.setNom(resultSet.getString("nom"));
            user.setPrenom(resultSet.getString("prenom"));
            user.setEmail(resultSet.getString("email"));
            user.setRole(resultSet.getString("role"));
            user.setPassword(resultSet.getString("password"));
            UserList.add(user);
        }        return UserList;
    }

    public List<User> afficherByRole(String role) throws SQLException {
        String req = "SELECT * FROM user   WHERE role=?";
        PreparedStatement pre = connection.prepareStatement(req);
        pre.setString(1, role);
        ResultSet resultSet = pre.executeQuery();
        List<User> UserList = new ArrayList<>();
        while (resultSet.next()) {
            User user = new User();
            user.setNom(resultSet.getString("nom"));
            user.setPrenom(resultSet.getString("prenom"));
            user.setEmail(resultSet.getString("email"));
            user.setRole(resultSet.getString("role"));
            user.setPassword(resultSet.getString("password"));
            UserList.add(user);
        }        return UserList;
    }

    public void modifierEmail(User user , String Email ) throws SQLException {
        String req = "UPDATE user SET email=? WHERE id_user=? ";
        PreparedStatement pre = connection.prepareStatement(req);
        pre.setString(1, Email);
        pre.setInt(2, user.getId());
        pre.executeUpdate();
    }
// uplode Image
    public  void modifierImage(int id_user , String image)throws SQLException {
        String req = "UPDATE user SET image=? WHERE id_user=? ";
        PreparedStatement pre = connection.prepareStatement(req);
        pre.setString(1, image);
        pre.setInt(2, id_user);
        pre.executeUpdate();
    }
}