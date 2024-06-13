package entities;

import utils.MyDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class evaluation {
    public int id_evaluation;
    public String titre_evaluation;
    public String description;

    public String Difficulte;
    public int  nb_questions;
    public Time Duree;
    public float Resultat;
    public Date testDate;
    public String createur;
    public float prix;
    public String domaine;
    public List<question> question;
    public Connection connection;
    public List<response> reponses;






    public evaluation(int id_evaluation, String titre_evaluation, String description, String difficulte, int nb_questions, Time duree, float resultat, Date testDate, String createur, float prix ,String domaine) {
        this.id_evaluation = id_evaluation;
        this.titre_evaluation = titre_evaluation;
        this.description = description;
        Difficulte = difficulte;
        this.nb_questions = nb_questions;
        Duree = duree;
        Resultat = resultat;
        this.testDate = testDate;
        this.createur = createur;
        this.prix = prix;
        this.domaine= domaine;
    }

    public evaluation(String titre_evaluation, String description, String difficulte, int nb_questions, Time duree, float resultat, Date testDate, String createur , float prix,String domaine) {
        this.titre_evaluation = titre_evaluation;
        this.description = description;
        Difficulte = difficulte;
        this.nb_questions = nb_questions;
        Duree = duree;
        Resultat = resultat;
        this.testDate = testDate;
       this.createur=createur;
       this.prix=prix;
        this.domaine= domaine;
    }

    public evaluation(int id_evaluation, String titre_evaluation, String description, String difficulte, int nb_questions, Time duree, float resultat, Date testDate, String createur, float prix, String domaine, List<question> question) {
        this.id_evaluation = id_evaluation;
        this.titre_evaluation = titre_evaluation;
        this.description = description;
        Difficulte = difficulte;
        this.nb_questions = nb_questions;
        Duree = duree;
        Resultat = resultat;
        this.testDate = testDate;
        this.createur = createur;
        this.prix = prix;
        this.domaine = domaine;
        this.question = question;
    }

    public evaluation( ) {
        connection = MyDB.getInstance().getConnection();

    }

    public int getId_evaluation() {
        return id_evaluation;
    }

    public String getTitre_evaluation() {
        return titre_evaluation;
    }

    public String getDescription() {
        return description;
    }

    public String getDifficulte() {
        return Difficulte;
    }

    public int getNb_questions() {
        return nb_questions;
    }

    public Time getDuree() {
        return Duree;
    }

    public float getResultat() {
        return Resultat;
    }

    public Date getTestDate() {
        return testDate;
    }

    public String getCreateur() {
        return this.createur;
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public void setId_evaluation(int id_evaluation) {
        this.id_evaluation = id_evaluation;
    }

    public void setTitre_evaluation(String titre_evaluation) {
        this.titre_evaluation = titre_evaluation;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDifficulte(String difficulte) {
        Difficulte = difficulte;
    }

    public void setNb_questions(int nb_questions) {
        this.nb_questions = nb_questions;
    }

    public void setDuree(Time duree) {
        this.Duree = duree;
    }

    public void setResultat(float resultat) {
        Resultat = resultat;
    }

    public void setTestDate(Date testDate) {
        this.testDate = testDate;
    }

    public void setCreateur(String createur) {
        this.createur = createur;
    }

    public String getDomaine() {
        return domaine;
    }

    public void setDomaine(String domaine) {
        this.domaine = domaine;
    }

    public evaluation(List<response> reponses) {
        this.reponses = reponses;
    }

    @Override
    public String toString() {
        return "evaluation{" +
                "id_evaluation=" + id_evaluation +
                ", titre_evaluation='" + titre_evaluation + '\'' +
                ", description='" + description + '\'' +
                ", Difficulte='" + Difficulte + '\'' +
                ", nb_questions=" + nb_questions +
                ", Duree=" + Duree +
                ", Resultat=" + Resultat +
                ", testDate=" + testDate +
                ", createur='" + createur + '\'' +
                ", prix=" + prix +
                ", domaine='" + domaine + '\'' +
                ", question=" + question +
                '}';
    }

    public void loadQuestionsAndResponsesFromDatabase(Connection connection) {

        List<question> matchingQuestions = new ArrayList<>();

        try {
            String query = "SELECT * FROM question WHERE domaine = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, this.domaine);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int idQuestion = resultSet.getInt("idQuestion");
                        String enonce = resultSet.getString("enonce");
                        String domaine = resultSet.getString("domaine");

                        question q = new question(idQuestion, enonce, domaine);

                        matchingQuestions.add(q);
q.loadResponses(connection);

                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        this.question = matchingQuestions;  // Assign the matching questions to the instance variable
    }

    public evaluation(int id_evaluation) {
        this.id_evaluation = id_evaluation;
    }

    public void loadResponses(question q, List<response> allResponses) {
        for (response response : allResponses) {
            if (response.getIdQuestion() == q.getIdQuestion()) {
                q.getReponses().add(response);
            }
        }}
    public void displayQuestionsAndResponses() {
        if (question != null && !question.isEmpty()) {
            System.out.println("Questions for Evaluation " + id_evaluation + ":");
            for (question question : question) {
                System.out.println("  - Question ID: " + question.getIdQuestion());
                System.out.println("    Enonce: " + question.getEnonce());
                System.out.println("    Domaine: " + question.getDomaine());

                // Display responses for the question

                question.displayResponses();

            }
        } else {
            System.out.println("No questions available for Evaluation " + id_evaluation);
        }

    }

    public List<entities.question> getQuestion() {
        return question;
    }

    public void setQuestion(List<entities.question> question) {
        this.question = question;
    }
}



