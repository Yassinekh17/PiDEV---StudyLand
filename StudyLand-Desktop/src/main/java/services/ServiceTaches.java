package services;

import entities.Project;
import entities.StatutTache;
import entities.taches_projet;
import utils.MyDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
public class ServiceTaches implements IService<taches_projet> {
    private Connection connection ;
    public ServiceTaches(){
        connection = MyDB.getInstance().getConnection();
    }
    @Override
    public void ajouter(taches_projet tachesProjet) throws SQLException {
        String req = "INSERT INTO taches_projet(nom_tache, Desc_tache, Date_D, Date_F, statut,Responsable,id_projet) VALUES (?, ?, ?, ?, ?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(req);
        preparedStatement.setString(1, tachesProjet.getNom_tache());
        preparedStatement.setString(2, tachesProjet.getDesc_tache());
        preparedStatement.setDate(3, new java.sql.Date(tachesProjet.getDate_D().getTime()));
        preparedStatement.setDate(4, new java.sql.Date(tachesProjet.getDate_F().getTime()));
        preparedStatement.setObject(5, tachesProjet.getStatut().name());
        preparedStatement.setString(6, tachesProjet.getResponsable());
        preparedStatement.setInt(7, tachesProjet.getId_projet());

        preparedStatement.executeUpdate();
    }
    @Override
    public void modifier(taches_projet tachesProjet) throws SQLException {
        String req = "UPDATE taches_projet SET nom_tache=?, Desc_tache=?, Date_D=?, Date_F=?, statut=?, Responsable=?, id_projet=?   WHERE id_taches=?";
        try (PreparedStatement pre = connection.prepareStatement(req)) {
            pre.setString(1, tachesProjet.getNom_tache());
            pre.setString(2, tachesProjet.getDesc_tache());
            pre.setDate(3, new java.sql.Date(tachesProjet.getDate_D().getTime()));
            pre.setDate(4, new java.sql.Date(tachesProjet.getDate_F().getTime()));
            pre.setObject(5, tachesProjet.getStatut());
            pre.setString(6, tachesProjet.getResponsable());
            pre.setInt(7, tachesProjet.getId_projet());
            pre.setInt(8, tachesProjet.getId_taches());
            pre.executeUpdate();
        }
    }
    @Override
    public void supprimer(taches_projet tachesProjet) throws SQLException {
        String req ="delete from taches_projet where id_taches=?";
        PreparedStatement pre =connection.prepareStatement(req);
        pre.setInt(1,tachesProjet.getId_taches());
        pre.executeUpdate();
    }

    @Override
    public List<taches_projet> afficher() throws SQLException {
        String req = "select * from taches_projet";
        Statement ste = connection.createStatement();
        ResultSet res = ste.executeQuery(req);
        List<taches_projet> list = new ArrayList<>();
        while (res.next()){
            taches_projet tp = new taches_projet();
            tp.setId_taches(res.getInt(1));
            tp.setNom_tache(res.getString(2));
            tp.setDesc_tache(res.getString(3));
            tp.setDate_D(res.getDate(4));
            tp.setDate_F(res.getDate(5));
            tp.setStatut(StatutTache.valueOf(res.getString(6)));
            tp.setResponsable(res.getString(7));
            tp.setId_projet(res.getInt(8));

            list.add(tp);
        }
        return list;
    }

    public List<taches_projet> getTasksByProjectId(int projectId) throws SQLException {
        String req = "SELECT * FROM taches_projet WHERE id_projet=?";
        List<taches_projet> tasks = new ArrayList<>();

        try (PreparedStatement st = connection.prepareStatement(req)) {
            st.setInt(1, projectId);

            try (ResultSet res = st.executeQuery()) {
                while (res.next()) {
                    taches_projet tp = new taches_projet();
                    tp.setId_taches(res.getInt(1));
                    tp.setNom_tache(res.getString(2));
                    tp.setDesc_tache(res.getString(3));
                    tp.setDate_D(res.getDate(4));
                    tp.setDate_F(res.getDate(5));
                    tp.setStatut(StatutTache.valueOf(res.getString(6)));
                    tp.setResponsable(res.getString(7));
                    tp.setId_projet(res.getInt(8));

                    tasks.add(tp);
                }
            }
        }

        return tasks;
    }
}
