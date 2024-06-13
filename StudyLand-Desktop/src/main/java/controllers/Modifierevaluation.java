package controllers;

import entities.evaluation;
import entities.question;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;
import services.EvalService;
import services.quesservice;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;
import utils.MyDB;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

public class Modifierevaluation {
    @FXML
    private Button ajouterevaluation;

    @FXML
    private Button button;

    @FXML
    private Button button1;

    @FXML
    private TextField createur;

    @FXML
    private DatePicker date;
    @FXML
    private TextField textrecherche;
    @FXML
    private TextField description;

    @FXML
    private TextField dificulter;

    @FXML
    private TextField domaine;

    @FXML
    private TextField duree;

    @FXML
    private TextField nbquestion;

    @FXML
    private TextField prix;

    @FXML
    private ListView<String> questionListView;

    @FXML
    private ListView<question> questionselectioner;

    @FXML
    private TextField resultat;

    @FXML
    private TextField titre;

    EvalService eval = new EvalService();
    @FXML
    private ListView<String> evaluationListView;
    public Connection connection;
    EvalService evalService = new EvalService();


    public Modifierevaluation(quesservice questionService) {
        this.questionService = questionService;
    }

    List<question> selectedQuestions = new ArrayList<>();

    public Modifierevaluation() {
        this.connection = MyDB.getInstance().getConnection();
    }

    private quesservice questionService = new quesservice();
    private Map<String, Integer> evaluationIdMap = new HashMap<>();
    private Map<String, Integer> questionIdMap = new HashMap<>();

    @FXML
    public void ajouter(ActionEvent actionEvent) {
        // Obtenir les éléments sélectionnés
        ObservableList<String> selectedQuestionStrings = questionListView.getSelectionModel().getSelectedItems();


        // Afficher les IDs des questions sélectionnées
        System.out.println("IDs des questions sélectionnées :");
        ObservableList<question> selectedQuestions = FXCollections.observableArrayList(); // Create a new ObservableList

        for (String selectedQuestion : selectedQuestionStrings) {
            int id = extractSelectedQuestionId();
            System.out.println(id);

            // Vérifier si la question est déjà dans la liste selectedQuestions
            if (selectedQuestions.stream().noneMatch(q -> q.getIdQuestion() == id)) {
                // Récupérer la question complète de la liste des questions chargées initialement
                question fullQuestion = getQuestionById(id);

                // Ajouter l'objet question à la liste des questions sélectionnées
                selectedQuestions.add(fullQuestion);
            }
        }

        // Ajouter toutes les questions sélectionnées à la liste questionselectioner
        questionselectioner.getItems().addAll(selectedQuestions);

        // Afficher les IDs des questions sélectionnées à partir de la liste selectedQuestions
        System.out.println("IDs des questions sélectionnées :");
        for (question selectedQuestion : selectedQuestions) {
            System.out.println(selectedQuestion.getIdQuestion());
        }
    }



    private question getQuestionById(int questionId) {
        // Appelez le service ou la méthode appropriée pour obtenir la question par son ID
        try {
            return questionService.getQuestionById(questionId);
        } catch (SQLException e) {
            e.printStackTrace(); // Gérer l'exception de manière appropriée dans votre application
        }
        return null;
    }



    @FXML
    public void initialize() {
        try {
            List<question> questions = questionService.afficher();
            displayQuestions(questions);
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
        // Configurer la ListView pour permettre la sélection multiple
        questionListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        try {
            List<evaluation> evaluations = evalService.afficher();
            displayEvaluations(evaluations);
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }

    }


    private void displayEvaluations(List<evaluation> evaluations) {
        for (evaluation eval : evaluations) {
            String evaluationText = eval.getTitre_evaluation(); // Affiche uniquement le titre de l'évaluation
            evaluationListView.getItems().add(evaluationText);

            // Ajoutez l'ID de l'évaluation comme un objet invisible dans la liste
            evaluationIdMap.put(evaluationText, eval.getId_evaluation());
        }
    }



    private void displayQuestions(List<question> questions) {
        for (question q : questions) {
            String questionText = q.getEnonce(); // Assurez-vous d'adapter cela à votre classe Question
            questionListView.getItems().add(questionText);

            // Ajoutez l'ID de la question comme un objet invisible dans la liste
            questionIdMap.put(questionText, q.getIdQuestion());
        }
    }

//pour avoire le id selectionner de evaluation
    private int extractSelectedEvaluationId() {
        int selectedEvaluationId = -1; // Valeur par défaut si aucune évaluation n'est sélectionnée

        // Obtenez le modèle de sélection de la liste
        MultipleSelectionModel<String> selectionModel = evaluationListView.getSelectionModel();

        // Vérifiez si la sélection n'est pas vide
        if (!selectionModel.isEmpty()) {
            // Obtenez l'élément sélectionné (le texte de l'évaluation)
            String selectedEvaluationText = selectionModel.getSelectedItem();

            // Vérifiez si l'ID correspondant existe dans la map
            if (evaluationIdMap.containsKey(selectedEvaluationText)) {
                // Si l'ID correspondant existe, extrayez l'ID
                selectedEvaluationId = evaluationIdMap.get(selectedEvaluationText);
            }
        }

        return selectedEvaluationId;
    }
    private int extractSelectedQuestionId() {
        int selectedQuestionId = -1; // Valeur par défaut si aucune question n'est sélectionnée

        // Obtenez le modèle de sélection de la liste
        MultipleSelectionModel<String> selectionModel = questionListView.getSelectionModel();

        // Vérifiez si la sélection n'est pas vide
        if (!selectionModel.isEmpty()) {
            // Obtenez l'élément sélectionné (le texte de la question)
            String selectedQuestionText = selectionModel.getSelectedItem();

            // Vérifiez si l'ID correspondant existe dans la map
            if (questionIdMap.containsKey(selectedQuestionText)) {
                // Si l'ID correspondant existe, extrayez l'ID
                selectedQuestionId = questionIdMap.get(selectedQuestionText);
            }
        }

        return selectedQuestionId;
    }


    public void ajouterevaluation(ActionEvent actionEvent) throws ParseException {
        // Créer un objet evaluation à partir des valeurs dans l'interface utilisateur
        evaluation evaluation = new evaluation();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        // Validation des champs obligatoires
        if (isFieldEmpty(titre, "Titre") ||
                isFieldEmpty(description, "Description") ||
                isFieldEmpty(dificulter, "Difficulté") ||
                isFieldEmpty(nbquestion, "Nombre de questions") ||
                isFieldEmpty(duree, "Durée HH:mm:ss") ||
                isFieldEmpty(resultat, "Résultat") ||
                date.getValue() == null ||
                isFieldEmpty(createur, "Créateur") ||
                isFieldEmpty(prix, "Prix") ||
                isFieldEmpty(domaine, "Domaine")) {
            return;
        }
        if (!isValidDuration(duree, "Durée")) {
            return;
        }
        // Validation des formats de saisie
        if (!isValidInteger(nbquestion, "Nombre de questions") ||
                !isValidFloat(resultat, "Résultat") ||
                !isValidFloat(prix, "Prix")) {
            return;
        }

        // Validation de la date
        if (date.getValue().isBefore(LocalDate.now())) {
            showWarning("La date de l'évaluation ne peut pas être antérieure à la date actuelle.");
            return;
        }

        // Parse the date string from the TextField
        Date parsedDate = dateFormat.parse(duree.getText());

        // Set the parsedDate in the evaluation object
        evaluation.setDuree(new Time(parsedDate.getTime()));
        evaluation.setTitre_evaluation(titre.getText());
        evaluation.setDescription(description.getText());
        evaluation.setDifficulte(dificulter.getText());
        evaluation.setNb_questions(Integer.parseInt(nbquestion.getText()));
        evaluation.setDuree(new Time(parsedDate.getTime()));
        evaluation.setResultat(Float.parseFloat(resultat.getText()));
        evaluation.setTestDate(java.sql.Date.valueOf(date.getValue()));
        evaluation.setCreateur(createur.getText());
        evaluation.setPrix(Float.parseFloat(prix.getText()));
        evaluation.setDomaine(domaine.getText());

        int idEvaluation = extractSelectedEvaluationId();
        evaluation.setId_evaluation(idEvaluation);

        try {
            // Appel de la méthode de modification dans le service
            eval.modifier(evaluation);

            // Fetch question IDs associated with the evaluation
            List<Integer> existingQuestionIds = eval.getQuestionsByEvaluationId(evaluation.getId_evaluation());

            // Fetch corresponding questions using the getQuestionById method
            List<Integer> selectedQuestionIds = new ArrayList<>();
            for (question selectedQuestion : questionselectioner.getItems()) {
                selectedQuestionIds.add(selectedQuestion.getIdQuestion());
            }

            // Ajouter uniquement les nouvelles questions sélectionnées à la table evaluationquestion
            for (int questionId : selectedQuestionIds) {
                // Vérifier si la questionId n'existe pas déjà dans la table
                if (!existingQuestionIds.contains(questionId)) {
                    ajoutIdEvaluationQuestion(evaluation.getId_evaluation(), questionId);
                }
            }
            afficherNotification("Modification réussie", "La modification a été effectuée avec succès.", NotificationType.SUCCESS);

            System.out.println("Modification réussie avec succès");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }



    private void displaySelectedqeustion(List<question> selectedQuestions) {

        questionselectioner.getItems().clear();

        // Afficher les questions sélectionnées
        System.out.println("Questions sélectionnées :");
        for (question selectedQuestion : selectedQuestions) {
            String questionText = selectedQuestion.getEnonce() + " - " + selectedQuestion.getIdQuestion();
            System.out.println("ID de la question : " + selectedQuestion.getIdQuestion());
            questionselectioner.getItems().add(selectedQuestion);
        }
    }


    private void ajoutIdEvaluationQuestion(int idEvaluation, int idQuestion) throws SQLException {
        String req = "INSERT INTO evaluationquestion (id_evaluation, id_question) VALUES (?, ?)";
        try (PreparedStatement st = connection.prepareStatement(req)) {
            st.setInt(1, idEvaluation);
            st.setInt(2, idQuestion);
            st.executeUpdate();
        }
    }



    public void recuperer(ActionEvent actionEvent) throws SQLException {
        EvalService eval = new EvalService();


        try {
            // Get the evaluation ID from the user input or any other source
            int evaluationIdToRetrieve = afficherIdEvaluationSelectionnee();
            System.out.println("voila id selectinner " + evaluationIdToRetrieve);

            // Call the getEvaluationById method to retrieve the evaluation
            evaluation retrievedEvaluation = eval.getEvaluationById(evaluationIdToRetrieve);

            if (retrievedEvaluation != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                // Populate UI fields with the retrieved evaluation data
                titre.setText(retrievedEvaluation.getTitre_evaluation());
                description.setText(retrievedEvaluation.getDescription());
                dificulter.setText(retrievedEvaluation.getDifficulte());
                nbquestion.setText(String.valueOf(retrievedEvaluation.getNb_questions()));
                duree.setText(retrievedEvaluation.getDuree().toString());
                resultat.setText(String.valueOf(retrievedEvaluation.getResultat()));
                date.setValue(LocalDate.parse(dateFormat.format(retrievedEvaluation.getTestDate())));
                createur.setText(retrievedEvaluation.getCreateur());
                prix.setText(String.valueOf(retrievedEvaluation.getPrix()));
                domaine.setText(retrievedEvaluation.getDomaine());

                // You may also need to handle the question-related parts if needed

                System.out.println("Evaluation retrieved: " + retrievedEvaluation);
            } else {
                System.out.println("Evaluation not found with ID: " + evaluationIdToRetrieve);
            }
        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately in your application
        }
        evaluation evaluation = new evaluation();
        int idEvaluation = extractSelectedEvaluationId();
        List<Integer> questionIds = eval.getQuestionsByEvaluationId(idEvaluation);

        // Fetch corresponding questions using the getQuestionById method
        for (int questionId : questionIds) {
            question q = questionService.getQuestionById(questionId);
            if (q != null) {
                selectedQuestions.add(q);
            }
        }

        // Display questions in the questionselectioner ListView
        ObservableList<question> observableSelectedQuestions = FXCollections.observableArrayList(selectedQuestions);
        questionselectioner.setItems(observableSelectedQuestions);
        displaySelectedqeustion(selectedQuestions);

    }



    public int afficherIdEvaluationSelectionnee() {
        // Obtenez le modèle de sélection de la ListView
        MultipleSelectionModel<String> selectionModel = evaluationListView.getSelectionModel();

        // Obtenez l'élément sélectionné (ici, une chaîne représentant une évaluation)
        String selectedEvaluationText = selectionModel.getSelectedItem();

        if (selectedEvaluationText != null && evaluationIdMap.containsKey(selectedEvaluationText)) {
            // Retournez directement l'ID à partir de la map
            return evaluationIdMap.get(selectedEvaluationText);
        }

        return 0;
    }






    public void recherchequestion(ActionEvent actionEvent) {
        try {
            // Obtenez le texte de recherche pour les questions
            String rechercheText = textrecherche.getText();

            // Appeler la méthode de recherche dans le service
            List<question> questionsTrouvees = questionService.rechercherParCaractere(rechercheText);

            // Effacer la liste actuelle avant d'afficher les résultats de la recherche
            questionListView.getItems().clear();

            // Afficher uniquement les questions trouvées dans la questionListView
            for (question q : questionsTrouvees) {
                String questionText = q.getEnonce() + " - " + q.getIdQuestion();
                questionListView.getItems().add(questionText);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Gérer l'exception de manière appropriée dans votre application
        }
    }





    private boolean isFieldEmpty(TextField field, String fieldName) {
        if (field.getText().isEmpty()) {
            Notification( "Le champ '" + fieldName + "' ne peut pas être vide.", NotificationType.WARNING);
            return true;
        }
        return false;
    }


    private boolean isValidDuration(TextField field, String fieldName) {
        String durationPattern = "([01]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]";
        String input = field.getText().trim();

        if (!input.matches(durationPattern)) {
            Notification( "Veuillez saisir une durée valide au format HH:mm:ss pour le champ '" + fieldName + "'.", NotificationType.WARNING);
            return false;
        }
        return true;
    }


    private boolean isValidInteger(TextField field, String fieldName) {
        try {
            Integer.parseInt(field.getText());
            return true;
        } catch (NumberFormatException e) {
            Notification("Veuillez saisir un nombre valide pour le champ '" + fieldName + "'.",  NotificationType.WARNING);
            return false;
        }
    }


    private boolean isValidFloat(TextField field, String fieldName) {
        try {
            Float.parseFloat(field.getText());
            return true;
        } catch (NumberFormatException e) {
            showWarning("Veuillez saisir un nombre décimal valide pour le champ '" + fieldName + "'.");
            return false;
        }
    }
    private void showWarning(String message) {
        Notifications.create()
                .title("Avertissement")
                .text(message)
                .showWarning();
    }
    public void lesevaluation(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lesevaluation.fxml"));
            Parent root = loader.load();

            // Obtenez le contrôleur après avoir chargé le fichier FXML
            Lesevaluation affichepre = loader.getController();

            // Créez une nouvelle scène avec le Parent chargé
            Scene scene = new Scene(root);

            // Récupérez la scène actuelle à partir du bouton
            Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            // Remplacez la scène actuelle par la nouvelle scène
            currentStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void recherche(ActionEvent actionEvent) {
        try {
            // Obtenez le texte de recherche
            String rechercheText = textrecherche.getText();

            // Appeler la méthode de recherche dans le service
            List<evaluation> evaluationsTrouvees = evalService.rechercherParCaractere(rechercheText);

            // Effacer la liste actuelle avant d'afficher les résultats de la recherche
            evaluationListView.getItems().clear();

            // Afficher les évaluations trouvées
            displayEvaluations(evaluationsTrouvees);
        } catch (SQLException e) {
            e.printStackTrace(); // Gérer l'exception de manière appropriée dans votre application
        }


    }

    public static void afficherNotification(String titre, String message, NotificationType type) {
        TrayNotification tray = new TrayNotification();
        tray.setTitle(titre);
        tray.setMessage(message);
        tray.setNotificationType(type);
        tray.showAndWait();
    }
    private void Notification(String message, NotificationType type) {
        TrayNotification tray = new TrayNotification();
        tray.setTitle("Avertissement");
        tray.setMessage(message);
        tray.setNotificationType(type);
        tray.showAndWait();
    }

}

