package entities;

import utils.MyDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class question {
    public int idQuestion;
    public String enonce;
    public String domaine;
    public List<response> reponses;
    public Connection connection;



    public question(int idQuestion, String enonce, String domaine, List<response> reponses) {
        this.idQuestion = idQuestion;
        this.enonce = enonce;
        this.domaine = domaine;
        this.reponses = new ArrayList<>();
    }

    public question(int idQuestion) {
        this.idQuestion = idQuestion;
    }

    public question() {
        connection = MyDB.getInstance().getConnection();

    }

    public question(String enonce, String domaine) {
        this.enonce = enonce;
        this.domaine = domaine;
    }

    public question(int idQuestion, String enonce, String domaine) {
        this.idQuestion = idQuestion;
        this.enonce = enonce;
        this.domaine = domaine;
    }

    public int getIdQuestion() {
        return idQuestion;
    }

    @Override
    public String toString() {
        return enonce;
    }
    public void loadResponses(Connection connection) {
        List<response> matchingResponses = new ArrayList<>();

        try {
            String query = "SELECT * FROM reponse WHERE idQuestion = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, this.idQuestion);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int idResponse = resultSet.getInt("idReponse");
                        String content = resultSet.getString("contenu");
                        // Add other fields as needed

                        response r = new response(idResponse, content);
                        matchingResponses.add(r);
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        this.reponses = matchingResponses;
    }

    public void displayResponses() {
        if (reponses != null) {
            System.out.println("Responses for Question " + idQuestion + ":");
            for (response response : reponses) {
                System.out.println("  - Response ID: " + response.getIdReponse());
                System.out.println("    Content: " + response.getContenu());
                System.out.println("    Status: " + response.getStatus());
            }
        } else {
            System.out.println("No responses available for Question " + idQuestion);
        }
    }
    public String getEnonce() {
        return enonce;
    }

    public String getDomaine() {
        return domaine;
    }

    public List<response> getReponses() {
        return reponses;
    }

    public void setIdQuestion(int idQuestion) {
        this.idQuestion = idQuestion;
    }

    public void setEnonce(String enonce) {
        this.enonce = enonce;
    }

    public void setDomaine(String domaine) {
        this.domaine = domaine;
    }

    public void setReponses(List<response> reponses) {
        this.reponses = reponses;
    }

    public question(String enonce, String domaine, List<response> reponses) {
        this.enonce = enonce;
        this.domaine = domaine;
        this.reponses = reponses;
    }
}
