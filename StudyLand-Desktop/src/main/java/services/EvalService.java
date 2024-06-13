package services;

import entities.evaluation;
import utils.MyDB;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class EvalService implements EvaluationService<evaluation> {
    public Connection connection;


    public EvalService() {
        connection = MyDB.getInstance().getConnection();

    }

   @Override
    public int ajouter(evaluation evaluation) throws SQLException {
        String req = "insert into evaluation (titre_evaluation, description, difficulte, nb_questions, duree, resultat, testDate, createur, prix, domaine) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement st = connection.prepareStatement(req, Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, evaluation.getTitre_evaluation());
            st.setString(2, evaluation.getDescription());
            st.setString(3, evaluation.getDifficulte());
            st.setInt(4, evaluation.getNb_questions());
            st.setTime(5, evaluation.getDuree());
            st.setFloat(6, evaluation.getResultat());
            st.setDate(7, new java.sql.Date(evaluation.getTestDate().getTime()));
            st.setString(8, evaluation.getCreateur());
            st.setFloat(9, evaluation.getPrix());
            st.setString(10, evaluation.getDomaine());

            int rowsAffected = st.executeUpdate();

            // Récupérer les clés générées
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = st.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        long id = generatedKeys.getLong(1);
                        System.out.println("ID généré : " + id);
                        return (int) id;

                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            return rowsAffected;
        }
    }


    public List<Integer> getQuestionsByEvaluationId(int idEvaluation) throws SQLException {
        List<Integer> questionIds = new ArrayList<>();
        String query = "SELECT id_question FROM evaluationquestion WHERE id_evaluation = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idEvaluation);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int questionId = resultSet.getInt("id_question");
                    questionIds.add(questionId);
                }
            }
        }

        return questionIds;
    }
    @Override
    public void modifier(evaluation evaluation) throws SQLException {
        String req="UPDATE  evaluation SET  titre_evaluation=? ,description=?,difficulte=? ,nb_questions=?,duree=?,resultat=?,testDate=?,createur=?,prix=?,domaine=? WHERE id_evaluation=?";
PreparedStatement pre=connection.prepareStatement(req);
pre.setString(1,evaluation.getTitre_evaluation());
pre.setString(2, evaluation.getDescription());
pre.setString(3 ,evaluation.getDescription());
pre.setInt(4,evaluation.getNb_questions());
pre.setTimestamp(5,new Timestamp(evaluation.getDuree().getTime()));
pre.setFloat(6,evaluation.getResultat());
pre.setDate(7,new java.sql.Date(evaluation.getTestDate().getTime()));
pre.setString(8,evaluation.getCreateur());
pre.setFloat(9,evaluation.getPrix());
        pre.setInt(11,evaluation.getId_evaluation());
        pre.setString(10,evaluation.getDomaine());



pre.executeUpdate(  );
        System.out.println("modification avec succée");
    }

    @Override
    public void supprimer(evaluation evaluation) throws SQLException {
        String req = "delete from  evaluation where id_evaluation=?";
        PreparedStatement pre =connection.prepareStatement(req);
        pre.setInt(1,evaluation.getId_evaluation());
        pre.executeUpdate();

    }

    @Override
    public List<evaluation> afficher()throws SQLException {

        String req = "SELECT * FROM evaluation";
        Statement st = connection.createStatement();
        ResultSet res = st.executeQuery(req);
        List<evaluation> list = new ArrayList<>();

        while (res.next()) {
            evaluation f = new evaluation();
            f.setId_evaluation(res.getInt("id_evaluation"));
            f.setTitre_evaluation(res.getString("titre_evaluation"));
            f.setDescription(res.getString("description"));
            f.setDuree(res.getTime("Duree"));
            f.setTestDate(res.getDate("testDate"));
            f.setPrix(res.getFloat("prix"));
            f.setResultat(res.getFloat("Resultat"));
            f.setCreateur(res.getString("Createur"));
            f.setNb_questions(res.getInt("nb_questions"));
            f.setDifficulte(res.getString("Difficulte"));
            f.setDomaine(res.getString("domaine"));

            list.add(f);
        }
        return list;
    }
    public evaluation getEvaluationById(int evaluationId) throws SQLException {
        String req = "SELECT * FROM evaluation WHERE id_evaluation=?";
        try (PreparedStatement st = connection.prepareStatement(req)) {
            st.setInt(1, evaluationId);
            ResultSet res = st.executeQuery();

            if (res.next()) {
                evaluation f = new evaluation();
                f.setId_evaluation(res.getInt("id_evaluation"));
                f.setTitre_evaluation(res.getString("titre_evaluation"));
                f.setDescription(res.getString("description"));
                f.setDuree(res.getTime("Duree"));
                f.setTestDate(res.getDate("testDate"));
                f.setPrix(res.getFloat("prix"));
                f.setResultat(res.getFloat("Resultat"));
                f.setCreateur(res.getString("Createur"));
                f.setNb_questions(res.getInt("nb_questions"));
                f.setDifficulte(res.getString("Difficulte"));
                f.setDomaine(res.getString("domaine"));

                return f;
            }
        }
        return null; // Return null if no evaluation with the given ID is found
    }

    public static evaluation getEvaluationByName(String evaluationName) {
        Connection connection = MyDB.getInstance().getConnection();
        evaluation foundEvaluation = null;

        try {
            String query = "SELECT * FROM evaluation WHERE titre_evaluation = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, evaluationName);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int id_evaluation = resultSet.getInt("id_evaluation");
                        String titre_evaluation = resultSet.getString("titre_evaluation");
                        String description = resultSet.getString("description");
                        String difficulte = resultSet.getString("Difficulte");
                        int nb_questions = resultSet.getInt("nb_questions");
                        Time duree = resultSet.getTime("Duree");
                        float resultat = resultSet.getFloat("Resultat");
                        Date testDate = resultSet.getDate("testDate");
                        String createur = resultSet.getString("createur");
                        float prix = resultSet.getFloat("prix");
                        String domaine = resultSet.getString("domaine");

                        foundEvaluation = new evaluation(id_evaluation, titre_evaluation, description, difficulte, nb_questions, duree, resultat, testDate, createur, prix, domaine);
                    }else {
                        // Debugging statement
                        System.out.println("Not found in the database.");
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return foundEvaluation;
    }
    public List<evaluation> rechercherParCaractere(String caractereRecherche) throws SQLException {
        List<evaluation> evaluationsTrouvees = new ArrayList<>();
        String req = "SELECT * FROM evaluation WHERE titre_evaluation LIKE ?";

        try (PreparedStatement st = connection.prepareStatement(req)) {
            st.setString(1, "%" + caractereRecherche + "%");

            try (ResultSet res = st.executeQuery()) {
                while (res.next()) {
                    evaluation f = new evaluation();
                    f.setId_evaluation(res.getInt("id_evaluation"));
                    f.setTitre_evaluation(res.getString("titre_evaluation"));
                    f.setDescription(res.getString("description"));
                    f.setDuree(res.getTime("Duree"));
                    f.setTestDate(res.getDate("testDate"));
                    f.setPrix(res.getFloat("prix"));
                    f.setResultat(res.getFloat("Resultat"));
                    f.setCreateur(res.getString("Createur"));
                    f.setNb_questions(res.getInt("nb_questions"));
                    f.setDifficulte(res.getString("Difficulte"));
                    f.setDomaine(res.getString("domaine"));

                    evaluationsTrouvees.add(f);
                }
            }
        }

        return evaluationsTrouvees;
    }
    public Map<String, Long> getStatisticsByDomaine() throws SQLException {
        Map<String, Long> statistics = new HashMap<>();

        String req = "SELECT domaine, COUNT(*) as count FROM evaluation GROUP BY domaine";
        try (PreparedStatement st = connection.prepareStatement(req)) {
            try (ResultSet res = st.executeQuery()) {
                while (res.next()) {
                    String domaine = res.getString("domaine");
                    long count = res.getLong("count");
                    statistics.put(domaine, count);
                }
            }
        }

        return statistics;
    }

}