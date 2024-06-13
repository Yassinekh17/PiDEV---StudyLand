package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import entities.response;
import utils.MyDB;
import entities.response.status ; // Importer l'énumération Status


public class Reponseservice implements EvaluationService<response> {



    public Connection connection;

    public Reponseservice() {
        connection = MyDB.getInstance().getConnection();
    }

    @Override
    public int ajouter(response response) throws SQLException {
        String req = "insert into reponse (contenu,idQuestion,status) values(?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(req);
        preparedStatement.setString(1, response.getContenu());
        preparedStatement.setInt(2, response.getIdQuestion());
        preparedStatement.setString(3, response.getStatus().toString());

        preparedStatement.executeUpdate();

        return 0;
    }

    @Override
    public void modifier(response response) throws SQLException {

            StringBuilder reqBuilder = new StringBuilder("UPDATE reponse SET");

            if (response.getContenu() != null && !response.getContenu().isEmpty()) {
                reqBuilder.append(" contenu=?,");
            }


            if (response.getStatus() != null) {
                reqBuilder.append(" status=?,");
            }

            // Supprimer la virgule finale, si elle existe
            if (reqBuilder.charAt(reqBuilder.length() - 1) == ',') {
                reqBuilder.deleteCharAt(reqBuilder.length() - 1);
            }

            reqBuilder.append(" WHERE idReponse=?");

            String req = reqBuilder.toString();
            PreparedStatement pre = connection.prepareStatement(req);

            int parameterIndex = 1;

            if (response.getContenu() != null && !response.getContenu().isEmpty()) {
                pre.setString(parameterIndex++, response.getContenu());
            }



            if (response.getStatus() != null) {
                pre.setString(parameterIndex++, response.getStatus().toString());
            }

            pre.setInt(parameterIndex, response.getIdReponse());

            pre.executeUpdate();
        }



    @Override
    public void supprimer(response response) throws SQLException {
        String req="delete from reponse where idReponse=?";
        PreparedStatement  pre=connection.prepareStatement(req);
        pre.setInt(1,response.getIdReponse());
        pre.executeUpdate();



    }

    @Override

    public List<response> afficher() throws SQLException {
        List<response> replist = new ArrayList<>();
        String req = "select * from reponse ";
        PreparedStatement pre = connection.prepareStatement(req);
        ResultSet res = pre.executeQuery();
        while (res.next()) {
            response resp = new response();
            resp.setIdReponse(res.getInt("idReponse"));
            resp.setContenu(res.getString("contenu"));
            resp.setIdQuestion(res.getInt("idQuestion"));
            String statusValue = res.getString("status");



            status status;

            if ("ONE".equalsIgnoreCase(statusValue)) {
                status = response.status.ONE;
            } else if ("ZERO".equalsIgnoreCase(statusValue)) {
                status = response.status.ZERO;
            } else {

                throw new IllegalArgumentException("Valeur de statut non valide dans la base de données");
            }

            resp.setStatus(status);
            replist.add(resp);
        }
        return replist;
    }
    public response getResponseById(int idReponse) throws SQLException {
        String req = "SELECT * FROM reponse WHERE idReponse = ?";

        try (PreparedStatement pre = connection.prepareStatement(req)) {
            pre.setInt(1, idReponse);

            try (ResultSet res = pre.executeQuery()) {
                if (res.next()) {
                    response resp = new response();
                    resp.setIdReponse(res.getInt("idReponse"));
                    resp.setContenu(res.getString("contenu"));
                    resp.setIdQuestion(res.getInt("idQuestion"));

                    String statusValue = res.getString("status");
                    status status;

                    if ("ONE".equalsIgnoreCase(statusValue)) {
                        status = response.status.ONE;
                    } else if ("ZERO".equalsIgnoreCase(statusValue)) {
                        status = response.status.ZERO;
                    } else {
                        throw new IllegalArgumentException("Invalid status value in the database");
                    }

                    resp.setStatus(status);
                    return resp;
                }
            }
        }

        return null;  // Return null if no response with the given ID is found
    }
    public List<response> rechercherParCaractere(String caractereRecherche) throws SQLException {
        List<response> responsesTrouvees = new ArrayList<>();
        String req = "SELECT * FROM reponse WHERE contenu LIKE ?";

        try (PreparedStatement st = connection.prepareStatement(req)) {
            st.setString(1, "%" + caractereRecherche + "%");

            try (ResultSet res = st.executeQuery()) {
                while (res.next()) {
                    response resp = new response();
                    resp.setIdReponse(res.getInt("idReponse"));
                    resp.setContenu(res.getString("contenu"));
                    resp.setIdQuestion(res.getInt("idQuestion"));

                    String statusValue = res.getString("status");
                    status status;

                    if ("ONE".equalsIgnoreCase(statusValue)) {
                        status = response.status.ONE;
                    } else if ("ZERO".equalsIgnoreCase(statusValue)) {
                        status = response.status.ZERO;
                    } else {
                        throw new IllegalArgumentException("Invalid status value in the database");
                    }

                    resp.setStatus(status);
                    responsesTrouvees.add(resp);
                }
            }
        }

        return responsesTrouvees;
    }

    public List<response> getResponsesByQuestionId(int idQuestion) throws SQLException {
        List<response> responses = new ArrayList<>();
        String req = "SELECT * FROM reponse WHERE idQuestion = ?";

        try (PreparedStatement pre = connection.prepareStatement(req)) {
            pre.setInt(1, idQuestion);

            try (ResultSet res = pre.executeQuery()) {
                while (res.next()) {
                    response resp = new response();
                    resp.setIdReponse(res.getInt("idReponse"));
                    resp.setContenu(res.getString("contenu"));
                    resp.setIdQuestion(res.getInt("idQuestion"));

                    String statusValue = res.getString("status");
                    status status;

                    if ("ONE".equalsIgnoreCase(statusValue)) {
                        status = response.status.ONE;
                    } else if ("ZERO".equalsIgnoreCase(statusValue)) {
                        status = response.status.ZERO;
                    } else {
                        throw new IllegalArgumentException("Invalid status value in the database");
                    }

                    resp.setStatus(status);
                    responses.add(resp);
                }
            }
        }

        return responses;
    }
}
