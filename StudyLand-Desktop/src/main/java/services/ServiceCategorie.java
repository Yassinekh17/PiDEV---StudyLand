package services;

import entities.Categorie;
import entities.Formation;
import utils.MyDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceCategorie implements IService<Categorie> {

    private Connection connection;

    public ServiceCategorie() {
        connection = MyDB.getInstance().getConnection();
    }
    public boolean checkCategoryExists(String categoryName) throws SQLException {
        String query = "SELECT COUNT(*) AS count FROM categorie WHERE nomCategorie = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, categoryName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt("count");
                return count > 0; // If count is greater than 0, category exists
            }
        }
        return false; // If no category with the given name is found
    }
    @Override
    public void ajouter(Categorie categorie) throws SQLException {
        String req = "INSERT INTO categorie(nomCategorie) VALUES (?)";
        PreparedStatement preparedStatement = connection.prepareStatement(req);
        preparedStatement.setString(1, categorie.getNomCategorie());


        preparedStatement.executeUpdate();
    }

    @Override
    public void modifier(Categorie categorie) throws SQLException {
        String req = "UPDATE categorie SET nomCategorie=? WHERE idCategorie=?";
        try (PreparedStatement pre = connection.prepareStatement(req)) {
            pre.setString(1, categorie.getNomCategorie());
            pre.setInt(2, categorie.getIdCategorie());

            pre.executeUpdate();
        }
    }

    @Override
    public void supprimer(Categorie categorie) throws SQLException {
        String req = "delete from categorie where idCategorie=?";
        PreparedStatement pre = connection.prepareStatement(req);
        pre.setInt(1, categorie.getIdCategorie());
        pre.executeUpdate();

    }

    @Override
    public List<Categorie> afficher() throws SQLException {
        String req = "SELECT * FROM categorie";
        Statement ste = connection.createStatement();
        ResultSet res = ste.executeQuery(req);
        List<Categorie> list = new ArrayList<>(); // Corrected to store Categorie objects

        int rowCount = 0; // Variable to track the number of rows processed

        while (res.next()) {
            Categorie c = new Categorie();
            c.setIdCategorie(res.getInt("idCategorie"));
            c.setNomCategorie(res.getString("nomCategorie"));

            list.add(c);

            // Print the current row being processed
            System.out.println("Row " + (++rowCount) + ": " + c.toString() + "\n");
        }

        System.out.println("Total rows processed: " + rowCount); // Print total rows processed
        return list;
    }
}

