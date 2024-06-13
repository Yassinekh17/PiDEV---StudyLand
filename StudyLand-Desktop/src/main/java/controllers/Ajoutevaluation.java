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
    import javafx.util.converter.FloatStringConverter;
    import javafx.util.converter.IntegerStringConverter;
    import org.controlsfx.control.Notifications;
    import services.EvalService;
    import services.quesservice;
    import tray.notification.NotificationType;
    import tray.notification.TrayNotification;
    import utils.MyDB;

    import java.io.IOException;
    import java.nio.charset.StandardCharsets;
    import java.sql.Connection;
    import java.sql.PreparedStatement;
    import java.sql.SQLException;
    import java.sql.Time;
    import java.text.ParseException;
    import java.text.SimpleDateFormat;
    import java.time.LocalDate;
    import java.util.Date;
    import java.util.List;
    public class Ajoutevaluation {
        @FXML
        private TextField recherchequestion;
        public Button ajouterevaluation;
        EvalService eval =new EvalService();
        evaluation evaluation =new evaluation();
        @FXML
        private TextField createur;

        @FXML
        private DatePicker date;

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
        private ListView<question> questionListView;
        @FXML
        private ListView<question> questionselectioner;

        @FXML
        private TextField titre;
        @FXML
        private TextField resultat;



        private quesservice questionService = new quesservice();
        EvalService  EvalService =new EvalService();
        public Connection connection;

        public Ajoutevaluation(Connection connection) {
            this.connection = connection;
        }
        public Ajoutevaluation() {
            // Utilisez la connexion par défaut ou tout autre mécanisme que vous utilisez pour obtenir une connexion
            this.connection = MyDB.getInstance().getConnection();
        }

        @FXML
        private void setupQuestionListViews() {
            // Configurer la ListView pour permettre la sélection multiple
            questionListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

            // Configurer la factory pour afficher uniquement l'enoncé dans questionListView
            questionListView.setCellFactory(param -> new ListCell<>() {
                @Override
                protected void updateItem(question item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getEnonce()); // Only display the question text
                    }
                }
            });

            // Configurer la factory pour afficher uniquement l'enoncé dans questionselectioner
            questionselectioner.setCellFactory(param -> new ListCell<>() {
                @Override
                protected void updateItem(question item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getEnonce()); // Only display the question text
                    }
                }
            });
        }

        @FXML
        public void initialize() {
            setupQuestionListViews();
            chargerQuestions();
            // Configurer la ListView pour permettre la sélection multiple
            questionListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            setupInputValidation();
        }
        private void setupInputValidation() {
            setupIntegerInputValidation(nbquestion);
            setupFloatInputValidation(prix);
            setupFloatInputValidation(resultat);
        }

        private void setupIntegerInputValidation(TextField textField) {
            textField.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 0,
                    change -> {
                        String newText = change.getControlNewText();
                        if (newText.matches("\\d*")) {
                            return change;
                        } else {
                            showWarning("Veuillez saisir un nombre valide pour le champ 'Nombre de questions'.");
                            return null;
                        }
                    }));
        }

        private void setupFloatInputValidation(TextField textField) {
            textField.setTextFormatter(new TextFormatter<>(new FloatStringConverter(), 0.0f,
                    change -> {
                        String newText = change.getControlNewText();
                        if (newText.matches("\\d*(\\.\\d*)?")) {
                            return change;
                        } else {
                            showWarning("Veuillez saisir un nombre décimal valide pour le champ.");
                            return null;
                        }
                    }));
        }

        private void showWarning(String message) {
            TrayNotification tray = new TrayNotification();
            tray.setTitle("Title");
            tray.setMessage(message);
            tray.setNotificationType(NotificationType.WARNING);
            tray.showAndWait();
        }
        private void succes(String message) {
            TrayNotification tray = new TrayNotification();
            tray.setTitle("Title");
            tray.setMessage(message);
            tray.setNotificationType(NotificationType.SUCCESS);
            tray.showAndWait();
        }

        private void chargerQuestions() {
            try {
                List<question> questions = questionService.afficher();
                ObservableList<question> observableQuestions = FXCollections.observableArrayList(questions);

                // Remplir la ListView avec les questions
                questionListView.setItems(observableQuestions);
            } catch (SQLException e) {
                e.printStackTrace(); // Gérer l'exception de manière appropriée dans votre application
            }
        }

        public void ajouter(javafx.event.ActionEvent actionEvent) {
            // Obtenir les éléments sélectionnés
            ObservableList<question> selectedQuestions = questionListView.getSelectionModel().getSelectedItems();

            // Afficher les IDs des questions sélectionnées
            System.out.println("IDs des questions sélectionnées :");
            for (question selectedQuestion : selectedQuestions) {
                System.out.println(selectedQuestion.getIdQuestion()); // Remplacez getId() par la méthode réelle pour obtenir l'ID de la question

                // Ajouter l'objet question complet à questionselectioner
                questionselectioner.getItems().add(selectedQuestion);
            }
        }



        public void ajouterevaluation(ActionEvent actionEvent) {
            try {
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

                Date parsedDate = dateFormat.parse(duree.getText());

                // Créer un objet evaluation à partir des valeurs dans l'interface utilisateur
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

                // Récupérer l'ID de l'évaluation ajoutée
                int idEvaluation = EvalService.ajouter(evaluation);
                System.out.println(idEvaluation + " idEvaluation est");

                // Ajouter les IDs des questions sélectionnées dans la table d'association
                for (question selectedQuestion : questionselectioner.getItems()) {
                    int idQuestion = selectedQuestion.getIdQuestion();
                    System.out.println(idQuestion);
                    ajoutIdEvaluationQuestion(idEvaluation, idQuestion);
                }
                succes("l'évaluation a été ajoutée avec succès");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

        }
        private boolean isFieldEmpty(TextField field, String fieldName) {
            if (field.getText().isEmpty()) {
                showWarning("Le champ '" + fieldName + "' ne peut pas être vide.");
                return true;
            }
            return false;
        }
        private boolean isValidDuration(TextField field, String fieldName) {
            String durationPattern = "([01]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]";
            String input = field.getText().trim();

            if (!input.matches(durationPattern)) {
                showWarning("Veuillez saisir une durée valide au format HH:mm:ss pour le champ '" + fieldName + "'.");
                return false;
            }
            return true;
        }
        private boolean isValidInteger(TextField field, String fieldName) {
            try {
                Integer.parseInt(field.getText());
                return true;
            } catch (NumberFormatException e) {
                showWarning("Veuillez saisir un nombre valide pour le champ '" + fieldName + "'.");
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


        private void ajoutIdEvaluationQuestion(int idEvaluation, int idQuestion) throws SQLException {
            String req = "INSERT INTO evaluationquestion (id_evaluation, id_question) VALUES (?, ?)";
            try (PreparedStatement st = connection.prepareStatement(req)) {
                st.setInt(1, idEvaluation);
                st.setInt(2, idQuestion);
                st.executeUpdate();
            }
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
                // Obtenir le texte de recherche
                String caractereRecherche = recherchequestion.getText();

                // Vérifier si le champ de recherche n'est pas vide
                if (!caractereRecherche.isEmpty()) {
                    // Effectuer la recherche en utilisant le service quesservice
                    List<question> questionsTrouvees = questionService.rechercherParCaractere(caractereRecherche);

                    // Mettre à jour la liste questionListView
                    ObservableList<question> observableQuestions = FXCollections.observableArrayList(questionsTrouvees);
                    questionListView.setItems(observableQuestions);
                } else {
                    // Si le champ de recherche est vide, afficher toutes les questions
                    chargerQuestions();
                }
            } catch (SQLException e) {
                e.printStackTrace(); // Gérer l'exception de manière appropriée dans votre application
            }
        }

    }
