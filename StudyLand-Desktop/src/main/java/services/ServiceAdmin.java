package services;
import entities.Admin;
import utils.MyDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceAdmin implements IUserService<Admin>{
    private Connection connection;
    public ServiceAdmin(){
        connection= MyDB.getInstance().getConnection();
    }
    @Override
    public void ajouter(Admin Admin) throws SQLException {
        String req = "INSERT INTO user (nom, prenom, email, password, Role) " +
                "VALUES ('" + Admin.getNom() + "', '" + Admin.getPrenom() + "', '" + Admin.getEmail() + "', '" + Admin.getPassword() + "', 'Admin')";
        Statement ste =connection.createStatement();
        ste.executeUpdate(req);

    }
    @Override
    public void modifier(Admin admin) throws SQLException {
        String req = "UPDATE user SET nom=?, prenom=? WHERE id_user=? ";
        PreparedStatement pre = connection.prepareStatement(req);
        pre.setString(1, admin.getNom());
        pre.setString(2, admin.getPrenom());
        pre.setInt(3, admin.getId());
        pre.executeUpdate();
    }

    @Override
    public void supprimer(Admin admin) throws SQLException {
            String req ="delete from user where id_user=?";
            PreparedStatement pre=connection.prepareStatement(req);
            pre.setInt(1,admin.getId());
            pre.executeUpdate();
    }
    @Override
    public List<Admin> afficher() throws SQLException {
        String req = "SELECT * FROM user WHERE Role=?";
        PreparedStatement pre = connection.prepareStatement(req);
        pre.setString(1, "Admin");
        ResultSet resultSet = pre.executeQuery();

        List<Admin> adminList = new ArrayList<>();
        while (resultSet.next()) {
            Admin admin = new Admin();
            admin.setId(resultSet.getInt("id_user"));
            admin.setNom(resultSet.getString("nom"));
            admin.setPrenom(resultSet.getString("prenom"));
            admin.setEmail(resultSet.getString("email"));
            admin.setRole(resultSet.getString("role"));
            admin.setPassword(resultSet.getString("password"));
            adminList.add(admin);
        }
        return adminList;
    }
}
