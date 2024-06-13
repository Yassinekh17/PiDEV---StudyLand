package services;

import entities.Project;
import utils.MyDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
public class ServiceProject implements IService<Project> {
    private Connection connection ;
    public ServiceProject(){
        connection = MyDB.getInstance().getConnection();
    }
    @Override
    public void ajouter(Project project) throws SQLException {
        String req = "INSERT INTO projet(NomProjet, DescProjet, Date_D, Date_F, nb_taches) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(req);
        preparedStatement.setString(1, project.getNomProjet());
        preparedStatement.setString(2, project.getDescProjet());
        preparedStatement.setDate(3, new java.sql.Date(project.getDate_D().getTime()));
        preparedStatement.setDate(4, new java.sql.Date(project.getDate_F().getTime()));
        preparedStatement.setInt(5, project.getNb_taches());


        preparedStatement.executeUpdate();
    }


    @Override
    public void modifier(Project project) throws SQLException {
        String req = "UPDATE projet SET NomProjet=?, DescProjet=?, Date_D=?, Date_F=?, nb_taches=? WHERE id_Projet=?";
        try (PreparedStatement pre = connection.prepareStatement(req)) {
            pre.setString(1, project.getNomProjet());
            pre.setString(2, project.getDescProjet());
            if (project.getDate_D() != null) {
                pre.setDate(3, new java.sql.Date(project.getDate_D().getTime()));
            } else {
                pre.setNull(3, java.sql.Types.DATE);
            }
            if (project.getDate_F() != null) {
                pre.setDate(4, new java.sql.Date(project.getDate_F().getTime()));
            } else {
                pre.setNull(4, java.sql.Types.DATE);
            }
            pre.setInt(5, project.getNb_taches());
            pre.setInt(6, project.getId_Projet());
            pre.executeUpdate();
        }
    }

    @Override
    public void supprimer(Project project) throws SQLException {
        String req ="delete from projet where id_Projet=?";
        PreparedStatement pre =connection.prepareStatement(req);
        pre.setInt(1,project.getId_Projet());
        pre.executeUpdate();
    }

    @Override
    public List<Project> afficher() throws SQLException {
        String req = "select * from projet";
        Statement ste = connection.createStatement();
        ResultSet res = ste.executeQuery(req);
        List<Project> list = new ArrayList<>();
        while (res.next()){
            Project p = new Project();
            p.setId_Projet(res.getInt(1));
            p.setNomProjet(res.getString(2));
            p.setDescProjet(res.getString(3));
            p.setDate_D(res.getDate(4));
            p.setDate_F(res.getDate(5));
            p.setNb_taches(res.getInt(6));
            list.add(p);
        }
        return list;
    }

//Reccher by name
public int recherche(String nom) throws SQLException {
    String req = "SELECT id_Projet FROM projet WHERE NomProjet=?";
    PreparedStatement pre = connection.prepareStatement(req);
    pre.setString(1, nom);
    ResultSet resultSet = pre.executeQuery();

    int id_Projet = -1; // Assuming -1 is a default value if the project is not found

    if (resultSet.next()) {
        // Retrieve the id_Projet from the result set
        id_Projet = resultSet.getInt("id_Projet");
    }

    // Close the result set and prepared statement
    resultSet.close();
    pre.close();

    return id_Projet;
}
    public List<Project> rechercherParCaractere(String caractereRecherche) throws SQLException {
        List<Project> ProjectTrouvees = new ArrayList<>();
        String req = "SELECT * FROM projet WHERE NomProjet LIKE ?";

        try (PreparedStatement st = connection.prepareStatement(req)) {
            st.setString(1, "%" + caractereRecherche + "%");

            try (ResultSet res = st.executeQuery()) {
                while (res.next()) {
                    Project P = new Project();
                    P.setId_Projet(res.getInt(1));
                    P.setNomProjet(res.getString(2));
                    P.setDescProjet(res.getString(3));
                    P.setDate_D(res.getDate(4));
                    P.setDate_F(res.getDate(5));
                    P.setNb_taches(res.getInt(6));

                    ProjectTrouvees.add(P);
                }
            }
        }

        return ProjectTrouvees;
    }

    public Project findProjectById(int projectId) throws SQLException {
        String req = "SELECT * FROM projet WHERE id_Projet=?";
        try (PreparedStatement pre = connection.prepareStatement(req)) {
            pre.setInt(1, projectId);

            try (ResultSet res = pre.executeQuery()) {
                if (res.next()) {
                    Project project = new Project();
                    project.setId_Projet(res.getInt(1));
                    project.setNomProjet(res.getString(2));
                    project.setDescProjet(res.getString(3));
                    project.setDate_D(res.getDate(4));
                    project.setDate_F(res.getDate(5));
                    project.setNb_taches(res.getInt(6));

                    return project;
                }
            }
        }

        return null; // Return null if the project is not found
    }

}
