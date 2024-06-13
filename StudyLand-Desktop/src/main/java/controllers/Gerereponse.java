package controllers;

import entities.evaluation;
import entities.question;
import entities.response;
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
import services.Reponseservice;
import services.quesservice;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class Gerereponse {
    Reponseservice reponseservice =new Reponseservice();
    @FXML
    private TextField recherchequestion;

    @FXML
    private TextField recherchereponse;
    @FXML
    private TableColumn<response, Void> supprimer;
    @FXML
    private TextArea idtep12;

    @FXML
    private TextArea idtep121;

    @FXML
    private TableColumn<response, String> colreponse;
    @FXML
    private TableView<response> tabreponse;
    @FXML
    private TableColumn<question, String> colquestion;
    @FXML
    private TableView<question> tabquestion;

    @FXML
    private TextField iddelete;

    @FXML
    private TextField idques;

    @FXML
    private TextField idques1;

    @FXML
    private TextField idtep1;



    @FXML
    private CheckBox non;

    @FXML
    private CheckBox non1;

    @FXML
    private CheckBox oui;

    @FXML
    private CheckBox oui1;
    private int selectedQuestionId;
    private int selectedReponseId;


    Reponseservice res = new Reponseservice();
Reponseservice rep = new Reponseservice();
quesservice ques= new quesservice();


    @FXML
    public void initialize() {
        colreponse.setCellValueFactory(new PropertyValueFactory<>("contenu"));
        colquestion.setCellValueFactory(new PropertyValueFactory<>("enonce"));
        initTable();


        }
    private void initTable() {
        tabreponse.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // Lorsqu'une réponse est sélectionnée, mettez à jour selectedReponseId
                selectedReponseId = newSelection.getIdReponse();
                System.out.println("Réponse ID: " + selectedReponseId);
            }
        });

        tabquestion.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // Lorsqu'une question est sélectionnée, récupérez son ID
                selectedQuestionId = newSelection.getIdQuestion();
                System.out.println("Question ID: " + selectedQuestionId);
            }
        });
        tabreponse.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // Lorsqu'une réponse est sélectionnée, récupérez ses données à partir du service
                int selectedReponseId = newSelection.getIdReponse();
                try {
                    List<response> responseList = reponseservice.afficher();
                    Optional<response> selectedResponse = responseList.stream()
                            .filter(response -> response.getIdReponse() == selectedReponseId)
                            .findFirst();

                    if (selectedResponse.isPresent()) {
                        response selectedResp = selectedResponse.get();

                        // Affichez les données dans les champs correspondants
                        idtep121.setText(selectedResp.getContenu());

                        // Mettez à jour les cases à cocher en fonction du statut
                        switch (selectedResp.getStatus()) {
                            case ONE:
                                oui1.setSelected(true);
                                non1.setSelected(false);
                                break;
                            case ZERO:
                                oui1.setSelected(false);
                                non1.setSelected(true);
                                break;
                            default:
                                // Gérez le cas où le statut n'est ni ONE ni ZERO
                                System.out.println("Statut invalide dans la base de données");
                                oui1.setSelected(false);
                                non1.setSelected(false);
                                break;
                        }
                    } else {
                        System.out.println("Réponse non trouvée avec l'ID : " + selectedReponseId);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    // Gérez l'exception de manière appropriée dans votre application
                }
            }
        });
        try {
            // Appelez la méthode afficher de votre service pour récupérer les questions
            List<response> reponse = res.afficher();

            // Convertissez la liste en une liste observable pour le TableView
            ObservableList<response> observableList = FXCollections.observableArrayList(reponse);

            // Associez la liste observable au TableView
            tabreponse.setItems(observableList);
        } catch (SQLException e) {
            e.printStackTrace(); // Gérez l'exception de manière appropriée
        }

        try {
            List<question> quest = ques.afficher();
            System.out.println("Nombre de questions récupérées : " + quest.size());

            // Convertissez la liste en une liste observable pour le TableView
            ObservableList<question> observableList = FXCollections.observableArrayList(quest);

            // Associez la liste observable au TableView
            tabquestion.setItems(observableList);
        } catch (SQLException e) {
        e.printStackTrace(); // Gérez l'exception de manière appropriée
        System.err.println("Erreur lors de la récupération des questions : " + e.getMessage());
    }
    }


// Vous devez compléter la méthode btnsup (la fin du nom de la méthode est tronquée dans votre question)
// Ajoutez la suite de votre méthode btnsup ici...

    public void btnsupprimer(ActionEvent actionEvent) {



    }

    public void btnajouter(ActionEvent actionEvent) {
        try {
            // Récupérer les valeurs des champs
            String newContenu = idtep12.getText().trim();
            boolean isSelected = oui.isSelected();
            String statusString = isSelected ? "ONE" : "ZERO";

            // Vérifier que les champs ne sont pas vides
            if (newContenu.isEmpty()) {
                Notification("Champ réponse vide Le champ réponse est vide. Veuillez le remplir.",NotificationType.WARNING);
                return;
            }

            // Vérifier que le champ Réponse respecte le type de données attendu (chaine de caractères)
            if (!newContenu.matches("^[a-zA-Z0-9_ ]+$")) {
                Notification("Format invalide Le champ réponse doit contenir uniquement des caractères alphanumériques.",NotificationType.WARNING);
                return;
            }

            // Vérifier qu'au moins une option CheckBox est sélectionnée
            if (!oui.isSelected() && !non.isSelected()) {
                Notification("Champ statut vide Le champ statut est vide. Veuillez le remplir.",NotificationType.WARNING);
                return;
            }

            response newResponse = new response();
            newResponse.setContenu(newContenu);
            newResponse.setIdQuestion(selectedQuestionId);
            newResponse.setStatus(response.status.valueOf(statusString));

            // Ajouter la réponse dans la base de données
            reponseservice.ajouter(newResponse);
            System.out.println(newResponse);
            Notification ("L'ajout  a été effectuée avec succès.", NotificationType.SUCCESS)  ;
        } catch (SQLException e) {
            Notification("Erreur lors de la modification de la réponse.",NotificationType.WARNING);

            throw new RuntimeException(e);
        }
    }



    public void btnmodifier(ActionEvent actionEvent) {
        try {
            // Récupérer les valeurs des champs de modification
            String newContenu = idtep121.getText().trim();
            boolean isSelected = oui1.isSelected();
            String statusString = isSelected ? "ONE" : "ZERO";
            System.out.println("isSelected: " + isSelected + ", statusString: " + statusString);

            // Vérifier que les champs ne sont pas vides
            if (newContenu.isEmpty()) {
                Notification("Veuillez remplir le champ Réponse.",NotificationType.WARNING);
                return;
            }

            // Vérifier que le champ Réponse respecte le type de données attendu (chaine de caractères)
            if (!newContenu.matches("^[a-zA-Z0-9_ ]+$")) {
                Notification("Le champ Réponse doit contenir uniquement des caractères alphanumériques.",NotificationType.WARNING);
                return;
            }

            // Vérifier qu'au moins une option CheckBox est sélectionnée
            if (!oui1.isSelected() && !non1.isSelected()) {
                Notification("Veuillez sélectionner le statut.",NotificationType.WARNING);
                return;
            }

            response updatedResponse = new response();
            updatedResponse.setContenu(newContenu);
            updatedResponse.setIdReponse(selectedReponseId);
            updatedResponse.setStatus(response.status.valueOf(statusString));

            // Modifier la réponse dans la base de données
            reponseservice.modifier(updatedResponse);
            Notification("La modification a été effectuée avec succès.", NotificationType.SUCCESS);
        } catch (SQLException e) {
            Notification("Erreur lors de la modification de la réponse.",NotificationType.WARNING);
            e.printStackTrace();
        }
    }
    private void showWarning(String message) {
        Notifications.create()
                .title("Avertissement")
                .text(message)
                .showWarning();
    }

    public void afficherrepense(ActionEvent actionEvent) {

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

    public void récupérer(ActionEvent actionEvent) {


        Reponseservice Reponseservice = new Reponseservice();
        try {
            // Get the evaluation ID from the user input or any other source
            int repIdToRetrieve = Integer.parseInt(idtep1.getText());
            response retrievedResponse = Reponseservice.getResponseById(repIdToRetrieve);

            if (retrievedResponse != null) {
                // Display the retrieved question's data in the modification fields
                idtep121.setText(retrievedResponse.getContenu());
                idques1.setText(String.valueOf(retrievedResponse.getIdQuestion()));
                if (retrievedResponse.getStatus() == response.status.ONE) {
                    oui1.setSelected(true);
                    non1.setSelected(false);
                } else if (retrievedResponse.getStatus() == response.status.ZERO) {
                    oui1.setSelected(false);
                    non1.setSelected(true);
                } else {
                    // Handle the case where the status is neither ONE nor ZERO
                    System.out.println("Invalid status in the database");
                }
            } else {
                System.out.println("Response not found with ID: " + repIdToRetrieve);
                // Optionally, clear the modification fields if the response is not found
                idtep121.clear();
                idques1.clear();
                oui1.setSelected(false);
                non1.setSelected(false);
            }




        }catch (NumberFormatException e) {
            e.printStackTrace(); // Handle the exception appropriately in your application
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }}

    public void recherche(ActionEvent actionEvent) {
        try {
            // Obtenez le texte de recherche
            String caractereRecherche = recherchequestion.getText();

            // Vérifiez si le champ de recherche n'est pas vide
            if (!caractereRecherche.isEmpty()) {
                // Effectuez la recherche en utilisant le service quesservice
                List<question> questionsTrouvees = ques.rechercherParCaractere(caractereRecherche);

                // Mettez à jour la liste observable pour le TableView
                ObservableList<question> observableQuestions = FXCollections.observableArrayList(questionsTrouvees);

                // Associez la liste observable au TableView
                tabquestion.setItems(observableQuestions);
            } else {
                // Si le champ de recherche est vide, affichez toutes les questions
                initTable(); // Vous pouvez également appeler votre méthode initTable() pour charger toutes les questions
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Gérez l'exception de manière appropriée dans votre application
        }
    }


    public void recherchereponse(ActionEvent actionEvent) {
        try{
            String caractereRecherche=recherchereponse.getText();
            if (!caractereRecherche.isEmpty()) {
            List<response> responsesTrouves = reponseservice.rechercherParCaractere(caractereRecherche);
            ObservableList<response> observablereponse = FXCollections.observableArrayList(responsesTrouves);
            tabreponse.setItems(observablereponse);
        }else
        {
            initTable();
            }
        }
        catch (SQLException e) {
        e.printStackTrace(); // Gérez l'exception de manière appropriée dans votre application
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
