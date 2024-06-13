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
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import services.Reponseservice;
import entities.response.status;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class Reponce {
    @FXML
    private StackPane centerPane;
    Reponseservice reponseservice =new Reponseservice();
    @FXML
    private TableColumn<response,Void> supprimer;
    @FXML
    private TextField idques;


    public StackPane getCenterPane() {
        return centerPane;
    }

    public void setCenterPane(StackPane centerPane) {
        this.centerPane = centerPane;
    }

    @FXML
    private TableColumn<response, Integer> idquestion;

    @FXML
    private TableColumn<response, Integer> idreponse;

    @FXML
    private TextField idtep;

    @FXML
    private CheckBox non;

    @FXML
    private CheckBox oui;

    @FXML
    private TextArea rep;

    @FXML
    private TableColumn<response, String> reponse;

    @FXML
    private TableColumn<response, String> status;


    @FXML
    private TableView<response> tabreponse;









    @FXML
    public void initialize() {
        reponse.setCellValueFactory(new PropertyValueFactory<>("contenu"));
        status.setCellValueFactory(new PropertyValueFactory<>("status"));


        // Appelez la méthode pour charger les données dans le tableau
        initTable();
        addActionColumn();
    }

    private void initTable() {
        try {
            // Appelez la méthode afficher de votre service pour récupérer les questions
            List<response> responses = reponseservice.afficher();

            // Convertissez la liste en une liste observable pour le TableView
            ObservableList<response> observableList = FXCollections.observableArrayList(responses);

            // Associez la liste observable au TableView
            tabreponse.setItems(observableList);
        } catch (SQLException e) {
            e.printStackTrace(); // Gérez l'exception de manière appropriée
        }
    }



    public void gererepense(ActionEvent actionEvent) {
        centerPane.getChildren().clear();
        FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/gerereponse.fxml"));
        try {
            Parent root = loader1.load();
            centerPane.getChildren().clear();
            centerPane.getChildren().add(root);
            Gerereponse controller = loader1.getController();
        } catch (IOException e) {
            System.out.println(e.getMessage());
    }}

    public void btnafficher(ActionEvent actionEvent) {

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



    private void addActionColumn() {
        supprimer.setCellFactory(param -> new TableCell<>() {
            private final Button button = new Button("Supprimer");

            {
                button.setOnAction(event -> {
                    response res = getTableView().getItems().get(getIndex());
                    try {
                        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                        confirmationAlert.setTitle("Confirmation de suppression");
                        confirmationAlert.setHeaderText("Supprimer response ?");
                        confirmationAlert.setContentText("Êtes-vous sûr de vouloir supprimer response " + res.getContenu() + " ?");

                        confirmationAlert.showAndWait().ifPresent(response -> {
                            if (response == ButtonType.OK) {
                                try {
                                    reponseservice.supprimer(res);
                                    tabreponse.getItems().remove(res);
                                    showAlert("Suppression réussie", " response a été supprimé avec succès.");
                                } catch (SQLException e) {
                                    System.out.println(e.getMessage());
                                }
                            }
                        });
                    } finally {

                    }
                });
            }

            private void showAlert(String title, String message) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(title);
                alert.setHeaderText(null);
                alert.setContentText(message);
                alert.showAndWait();
            }


            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(button);
                }
            }
        });
    }

}
