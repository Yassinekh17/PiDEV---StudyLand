package controllers;

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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;
import services.quesservice;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class Gerequestion {
    quesservice sq =new quesservice();

    @FXML
    private TextField idchercher;
    @FXML
    private TextField ajoutdomain;

    @FXML
    private TextArea ajouteneonce;

    @FXML
    private TextField iddelete;

    @FXML
    private TextField modfdomaine;

    @FXML
    private TextArea modifeneonce;



    @FXML
    private TableColumn<question, String> columndom;

    @FXML
    private TableColumn<question, String> columnenonce;
    @FXML
    private TableView<question> tab;



    public void btnsupprimer(javafx.event.ActionEvent actionEvent) {
        try{
            sq.supprimer(new question(Integer.parseInt(iddelete.getText())));
        }catch (SQLException e ){
            throw new RuntimeException(e);

        }
    }




    public void btnajouter(javafx.event.ActionEvent actionEvent) {
        try {
            // Récupérer les valeurs des champs
            String enonce = ajouteneonce.getText().trim();
            String domain = ajoutdomain.getText().trim();

            // Vérifier que les champs ne sont pas vides et respectent les types
            if (enonce.isEmpty() || domain.isEmpty()) {
                Notification("Veuillez remplir tous les champs.",NotificationType.WARNING);
                return;
            }

            // Vérifier que les champs sont des chaînes de caractères
            if (!enonce.matches("^[a-zA-Z0-9]+$") || !domain.matches("^[a-zA-Z0-9]+$")) {
                Notification("Veuillez saisir des chaînes de caractères valides.",NotificationType.WARNING);
                return;
            }

            // Ajouter la question dans la base de données
            sq.ajouter(new question(enonce, domain));
            Notification ("L'ajout  a été effectuée avec succès.", NotificationType.SUCCESS)  ;

        } catch (SQLException e) {
            Notification("Erreur lors de l'ajout de la question.",NotificationType.WARNING);
            e.printStackTrace();
        }
    }

    public void btnmodifier(javafx.event.ActionEvent actionEvent) {
        try {
            question selectedQuestion = tab.getSelectionModel().getSelectedItem();

            if (selectedQuestion != null) {
                int questionIdToUpdate = selectedQuestion.getIdQuestion();

                // Récupérer les valeurs des champs de modification
                String newEnonce = modifeneonce.getText().trim();
                String newDomain = modfdomaine.getText().trim();

                // Vérifier que les champs ne sont pas vides et respectent les types
                if (newEnonce.isEmpty() || newDomain.isEmpty()) {
                    Notification("Veuillez remplir tous les champs de modification.",NotificationType.WARNING);
                    return;
                }

                // Modifier la question dans la base de données
                sq.modifier(new question(questionIdToUpdate, newEnonce, newDomain));
                Notification("La modification a été effectuée avec succès.", NotificationType.SUCCESS);

            } else {
                Notification("Aucune question sélectionnée.",NotificationType.WARNING);
            }
        } catch (SQLException e) {
            Notification("Erreur lors de la modification de la question.",NotificationType.WARNING);
            e.printStackTrace();
        }
    }


    @FXML
    public void initialize() {
        // Initialisez les colonnes ici
        columnenonce.setCellValueFactory(new PropertyValueFactory<>("enonce"));
        tab.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                int selectedQuestionId = newValue.getIdQuestion();

                // Utilisez l'ID comme nécessaire
                System.out.println("Question ID selected: " + selectedQuestionId);
            }
        });

        // Appelez la méthode pour charger les données dans le tableau
        initTable();
    }

    private void initTable() {
        try {
            // Appelez la méthode afficher de votre service pour récupérer les questions
            List<question> questions = sq.afficher();

            // Convertissez la liste en une liste observable pour le TableView
            ObservableList<question> observableList = FXCollections.observableArrayList(questions);

            // Associez la liste observable au TableView
            tab.setItems(observableList);
        } catch (SQLException e) {
            e.printStackTrace(); // Gérez l'exception de manière appropriée
        }
    }

    public void affichagequetion(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajouterQuestion.fxml"));
            Parent root = loader.load();

            // Obtenez le contrôleur après avoir chargé le fichier FXML
            AjouterQuestion affichepre = loader.getController();

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

    public void récupérer(ActionEvent actionEvent) {

        quesservice quesservice = new quesservice();

        try {
            // Get the evaluation ID from the user input or any other source
            question selectedQuestion = tab.getSelectionModel().getSelectedItem();

            if (selectedQuestion != null) {
                // Display the retrieved question's data in the modification fields
                int questionIdToRetrieve = selectedQuestion.getIdQuestion();
                question retrievedQuestion = quesservice.getQuestionById(questionIdToRetrieve);

                if (retrievedQuestion != null) {
                    // Display the retrieved question's data in the modification fields
                    modifeneonce.setText(retrievedQuestion.getEnonce());
                    modfdomaine.setText(retrievedQuestion.getDomaine());
                } else {
                    System.out.println("Question not found with ID: " + questionIdToRetrieve);
                    // Optionally, clear the modification fields if the question is not found
                    modifeneonce.clear();
                    modfdomaine.clear();
                }
            } else {
                System.out.println("No question selected.");
            }

        } catch (NumberFormatException e) {
            e.printStackTrace(); // Handle the exception appropriately in your application
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

}

    public void recherche(ActionEvent actionEvent) {
        try {
            // Obtenir le texte de recherche
            String caractereRecherche = idchercher.getText();

            // Vérifier si le champ de recherche n'est pas vide
            if (!caractereRecherche.isEmpty()) {
                // Effectuer la recherche en utilisant le service quesservice
                List<question> questionsTrouvees = sq.rechercherParCaractere(caractereRecherche);

                // Mettre à jour la liste observable pour le TableView
                ObservableList<question> observableQuestions = FXCollections.observableArrayList(questionsTrouvees);

                // Associez la liste observable au TableView
                tab.setItems(observableQuestions);
            } else {
                // Si le champ de recherche est vide, afficher toutes les questions
                initTable(); // Vous pouvez également appeler votre méthode initTable() pour charger toutes les questions
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Gérer l'exception de manière appropriée dans votre application
        }
    }
    private void Notification(String message, NotificationType type) {
        TrayNotification tray = new TrayNotification();
        tray.setTitle("Avertissement");
        tray.setMessage(message);
        tray.setNotificationType(type);
        tray.showAndWait();
    }

}
