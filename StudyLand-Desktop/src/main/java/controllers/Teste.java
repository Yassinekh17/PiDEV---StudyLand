        package controllers;

        import com.mashape.unirest.http.HttpResponse;
        import com.mashape.unirest.http.JsonNode;
        import com.mashape.unirest.http.Unirest;
        import entities.evaluation;
        import entities.question;
        import entities.response;
        import javafx.application.Platform;
        import javafx.collections.FXCollections;
        import javafx.collections.ObservableList;
        import javafx.event.ActionEvent;
        import javafx.fxml.FXML;
        import javafx.fxml.FXMLLoader;
        import javafx.scene.Node;
        import javafx.scene.Parent;
        import javafx.scene.Scene;
        import javafx.scene.control.*;
        import javafx.scene.image.ImageView;
        import javafx.scene.input.MouseEvent;
        import javafx.scene.layout.VBox;
        import javafx.scene.media.Media;
        import javafx.scene.media.MediaPlayer;
        import javafx.scene.text.Text;
        import javafx.stage.Stage;
        import services.EvalService;
        import services.Reponseservice;
        import services.quesservice;
        import tray.notification.NotificationType;
        import tray.notification.TrayNotification;

        import java.io.File;
        import java.io.IOException;
        import java.sql.SQLException;
        import java.sql.Time;
        import java.text.DecimalFormat;
        import java.text.SimpleDateFormat;
        import java.time.LocalTime;
        import java.util.*;

        public class Teste {
            @FXML
            private ImageView gift;

            private List<response> selectedResponses = new ArrayList<>();
            @FXML
            private Label titre;
            @FXML
            private ComboBox<String> languageComboBox;
            @FXML
            private Text date;

            @FXML
            private Text descriptionTextArea;

            @FXML
            private Text domaine;

            @FXML
            private Text duree;


            @FXML
            private Label labelTimer;
            @FXML
            private Text nbquestion;

            @FXML
            private Text resultat;



            @FXML
            private VBox vbxquesrep;
            @FXML
            private List<CheckBox> responseCheckBoxes = new ArrayList<>();

    static int  ids;
             static double score;
            List<response> responses =new ArrayList<>();

            quesservice ques=new quesservice();
            EvalService EvalService = new EvalService();
            quesservice quesservice = new quesservice(); // Instantiate quesservice
        Reponseservice reponseservice=new Reponseservice();
    int getNb_questions;


            private int retrievedId;  // Attribute to store the retrieved ID

            // ... other fields and methods ...

            public int getRetrievedId() {
                return retrievedId;
            }
            private Timer timer;
            private LocalTime remainingTimeInSeconds;

            private TimerTask timerTask;
            private ActionEvent actionEvent;
            private ActionEvent finalActionEvent;
            @FXML
            private Text motivationalQuoteText;


            private MediaPlayer mediaPlayer;



            private final ObservableList<String> languages = FXCollections.observableArrayList("Français", "Anglais");
            @FXML
            public void initialize() throws SQLException {
                try {

                    String audioFilePath = "C:\\pidev\\StudyLand\\src\\main\\resources\\src\\Y2meta.app - 1 Minute Timer Relaxing Music Lofi Fish Background (128 kbps).mp3";
                    Media audioMedia = new Media(new File(audioFilePath).toURI().toString());

                    // Initialisation du lecteur multimédia
                    mediaPlayer = new MediaPlayer(audioMedia);


                    fetchAndDisplayMotivationalQuote();

                    evaluation eval = getEvaluationWithQuestionsById(ids);

                    System.out.println("Evaluation Object: " + eval);  // Check if evaluation is retrieved successfully

                    if (eval != null) {
                        remainingTimeInSeconds = Time.valueOf(LocalTime.of(eval.getDuree().getHours(), eval.getDuree().getMinutes(), eval.getDuree().getSeconds())).toLocalTime();
                        System.out.println("remainingTimeInSeconds 11111111&" + remainingTimeInSeconds);

                        getNb_questions = eval.getNb_questions();

                        System.out.println("getNb_questions " + getNb_questions);

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                        // Set the values in the UI fields
                        titre.setText(eval.getTitre_evaluation());
                        descriptionTextArea.setText(eval.getDescription());
                        domaine.setText(eval.getDomaine());
                        duree.setText(String.valueOf(eval.getDuree()));
                        resultat.setText(String.valueOf(eval.getResultat()));
                        nbquestion.setText(String.valueOf(eval.getNb_questions()));
                        System.out.println(domaine + "domaine aaaaaa");
                        // Clear existing content in the VBox
                        vbxquesrep.getChildren().clear();

                        vbxquesrep.getStyleClass().add("vbxquesrep");

                        for (question q : eval.getQuestion()) {
                            VBox questionVBox = new VBox();

                            // Appliquer le style à la questionLabel
                            Label questionLabel = new Label(q.getIdQuestion() + ". " + q.getEnonce());
                            questionLabel.getStyleClass().add("question-label");

                            // Fetch and display responses for the question using CheckBox
                            List<response> responses = reponseservice.getResponsesByQuestionId(q.getIdQuestion());
                            for (response r : responses) {
                                CheckBox responseCheckBox = new CheckBox(r.getContenu());

                                // Appliquer le style à la case à cocher
                                responseCheckBox.getStyleClass().add("checkbox");

                                // Ajouter un gestionnaire d'événements pour la sélection de la case à cocher
                                responseCheckBox.setOnAction(event -> handleCheckBoxSelection(responseCheckBox, r));
                                questionVBox.getChildren().add(responseCheckBox);
                            }

                            vbxquesrep.getChildren().add(questionLabel);
                            vbxquesrep.getChildren().add(questionVBox);
                        }
                    }

                    timer = new Timer();

                    timerTask = new TimerTask() {


                        @Override
                        public void run() {
                            if (!remainingTimeInSeconds.equals(LocalTime.of(0, 0, 1))) {
                                Platform.runLater(() -> {
                                    remainingTimeInSeconds = remainingTimeInSeconds.minusSeconds(1);
                                    labelTimer.setText(String.valueOf(remainingTimeInSeconds));

                                    System.out.println("remainingTimeInSeconds: " + remainingTimeInSeconds);

                                });
                            }
                            else  {
                                System.out.println("finalActionEvent"+finalActionEvent);
                                timerTask.cancel();
                                timer.cancel();


                                Platform.runLater(() -> {
                                    TrayNotification tray = new TrayNotification();
                                    tray.setTitle("Title");
                                    tray.setMessage("Le temps de evaluation est écoulé");
                                    tray.setNotificationType(NotificationType.INFORMATION);
                                    tray.showAndWait();


                                });
                                score = calculateScore();
                                resultat.setText(String.valueOf(score));


                            }

                        }

                    };

                    // Schedule the TimerTask to run every second
                    timer.scheduleAtFixedRate(timerTask, 0, 2000);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }



            public static void afficherNotification() {
                TrayNotification tray = new TrayNotification();
                tray.setTitle("Date de retour dépassée");
                tray.setMessage("Votre date de retour est dépassée");
                tray.setNotificationType(NotificationType.WARNING);
                tray.showAndWait();
            }

            private void showWarningWithButton(String message) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Avertissement");
                alert.setHeaderText(null);
                alert.setContentText(message);

                // Ajouter un bouton personnalisé à la boîte de dialogue
                ButtonType buttonType = new ButtonType("resultat d'evaluation ");
                alert.getButtonTypes().setAll(buttonType, ButtonType.CLOSE);

                // Gérer l'événement du bouton personnalisé
                alert.showAndWait().ifPresent(response -> {
                    if (response == buttonType) {
                        try {
                            double score = calculateScore();
                            System.out.println("Score: " + score + "/100");
                            resultat.setText(String.valueOf(score));
                            System.out.println(score);

                            // Load the Opss.fxml file
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/opss.fxml"));
                            Parent root = loader.load();

                            // Get the controller instance
                            Opss controller = loader.getController();

                            // Pass the score to the Opss controller
                            controller.scoreTest = score;
                            controller.initialize();

                            // Create a new scene with the loaded Parent
                            Scene scene = new Scene(root);


                                // Get the current stage from the alert
                                Stage currentStage = (Stage) alert.getDialogPane().getScene().getWindow();

                                // Set the new scene on the current stage
                                currentStage.setScene(scene);
                                currentStage.show();  // Show the new scene

                        } catch (IOException e ) {
                            e.printStackTrace();
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });


            }







            // ...
            private void handleCheckBoxSelection(CheckBox checkBox, response r) {
                System.out.println("ID de la réponse choisie : " + r.getIdReponse());
                System.out.println("Statut de la réponse : " + r.getStatus());

                // Check if the checkbox is selected
                if (checkBox.isSelected()) {
                    // Add the response to the list when it is selected
                    selectedResponses.add(r);
                } else {
                    // Remove the response from the list when it is deselected
                    selectedResponses.remove(r);
                }
            }






            public evaluation getEvaluationWithQuestionsById(int id) throws SQLException {
                retrievedId = ids;  // Set the retrieved ID

                evaluation eval = EvalService.getEvaluationById(id);

                if (eval != null) {
                    List<question> questions = quesservice.getQuestionsByEvaluationId(id); // Call the method on the instance
                    eval.setQuestion(questions);
                    ids= eval.getId_evaluation();
                    // Print evaluation details
                    System.out.println("Evaluation ID: " + eval.getId_evaluation());
                    System.out.println("Title: " + eval.getTitre_evaluation());
                    System.out.println("Description: " + eval.getDescription());
                    System.out.println("Domaine: " + eval.getDomaine());
                    System.out.println("Duree: " + eval.getDuree());
                    System.out.println("Number of Questions: " + eval.getNb_questions());
                    System.out.println("Resultat: " + eval.getResultat());
                    // Determine and print the number of questions
                    int numberOfQuestions = questions.size();
                    // Print questions
                    System.out.println("Questions:");
                    for (question q : questions) {
                        System.out.println("Question ID: " + q.getIdQuestion());
                        System.out.println("Enonce: " + q.getEnonce());
                        System.out.println("Domaine: " + q.getDomaine());
                        System.out.println("--------------------");
                    }
                }

                return eval;
            }



            @FXML
            public void validerAction(ActionEvent actionEvent) {
                try {
                     score = calculateScore();
                    System.out.println("Score: " + score + "/100");
                    resultat.setText(String.valueOf(score));
                    System.out.println(score);

                    // Load the Opss.fxml file
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Opss.fxml"));
                    Parent root = loader.load();

                    // Get the controller instance
                    Opss controller = loader.getController();

                    // Pass the score to the Opss controller
                    //controller.setScore(score);
                    controller.scoreTest=score;
                    controller.initialize();
                    // Create a new scene with the loaded Parent
                    Scene scene = new Scene(root);

                    // Get the current stage from the button
                    Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

                    // Set the new scene on the current stage
                    currentStage.setScene(scene);
                    currentStage.show();
                    if (timerTask != null) {
                        timerTask.cancel();
                    }
                    if (timer != null) {
                        timer.cancel();
                    }// Show the new scene
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }




            private double calculateScore() {
                int totalQuestions = 3;
                int correctResponses = 0;

                for (response r : selectedResponses) {
                    if (r.getStatus() == response.status.ONE) {
                        correctResponses++;

                    }

                }

                double percentage = (double) correctResponses / totalQuestions * 100;
                DecimalFormat decimalFormat = new DecimalFormat("#0.00");
                String formattedPercentage = decimalFormat.format(percentage);

                System.out.println(formattedPercentage);
                return Double.parseDouble(formattedPercentage); // I
            }
            private void fetchAndDisplayMotivationalQuote() {
                try {
                    // Faire une requête GET à l'API des citations motivantes
                    HttpResponse<JsonNode> response = Unirest.get("https://type.fit/api/quotes")
                            .header("accept", "application/json")
                            .asJson();

                    // Vérifier si la requête a réussi (code de statut 200)
                    if (response.getStatus() == 200) {
                        // Récupérer la liste des citations motivantes
                        JsonNode jsonResponse = response.getBody();

                        // Choisir une citation aléatoire de la liste
                        String motivationalQuote = getRandomQuote(jsonResponse);
                        System.out.println(motivationalQuote);
                        // Afficher la citation dans votre application JavaFX
                        motivationalQuoteText.setText(motivationalQuote);
                    } else {
                        System.err.println("Erreur lors de la récupération des citations. Code de statut : " + response.getStatus());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            private String getRandomQuote(JsonNode jsonResponse) {
                // Extraire une citation aléatoire de la liste
                int numberOfQuotes = jsonResponse.getArray().length();
                int randomIndex = (int) (Math.random() * numberOfQuotes);

                // Récupérer la citation à l'index aléatoire
                return jsonResponse.getArray().getJSONObject(randomIndex).getString("text");
            }

            public void playMusic(ActionEvent actionEvent) {
                mediaPlayer.play();
            }

            public void pauseMusic(ActionEvent actionEvent) {
                mediaPlayer.pause();
            }


            public void lesevaluation(ActionEvent actionEvent) {
                stopTimer();
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/ProfileApprenant.fxml"));
                    Parent root = loader.load();

                    // Access the controller of Teste after loading the FXML file
                    ProfileApprenant testeController = loader.getController();


                    Scene scene = new Scene(root);

                    // Get the current stage from the button's scene
                    Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

                    // Replace the current scene with the new scene
                    currentStage.setScene(scene);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            private void stopTimer() {
                if (timerTask != null) {
                    timerTask.cancel();
                }
                if (timer != null) {
                    timer.cancel();
                }
            }

            public void retour(MouseEvent mouseEvent) {


            }
        }







