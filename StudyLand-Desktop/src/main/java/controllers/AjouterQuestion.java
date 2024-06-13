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
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import services.quesservice;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class AjouterQuestion {

    public StackPane getCenterPane() {
        return centerPane;
    }

    public void setCenterPane(StackPane centerPane) {
        this.centerPane = centerPane;
    }

    @FXML
    private StackPane centerPane;
    @FXML
    private TableColumn<question, Void> supprimer;
    @FXML
    quesservice sq =new quesservice();
    @FXML
    private TableView<question> tab;


    @FXML
    private TableColumn<question, String> columndom;

    @FXML
    private TableColumn<question,String> columnenonce;

    public void btnafficher(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/reponce.fxml"));
            Parent root = loader.load();

            // Obtenez le contrôleur après avoir chargé le fichier FXML
            Reponce affichepre = loader.getController();

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

    @FXML
    public void initialize() {

        columndom.setCellValueFactory(new PropertyValueFactory<>("domaine"));
        columnenonce.setCellValueFactory(new PropertyValueFactory<>("enonce"));
        addActionColumn();
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

    public void gererquestion(ActionEvent actionEvent) {
        centerPane.getChildren().clear();
        FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/gerequestion.fxml"));
        try {
            Parent root = loader1.load();
            centerPane.getChildren().clear();
            centerPane.getChildren().add(root);
            Gerequestion controller = loader1.getController();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }



    public void evaluation(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Lesevaluation.fxml"));
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
    private void addActionColumn() {
        supprimer.setCellFactory(param -> new TableCell<>() {
            private final Button button = new Button("Supprimer");

            {
                button.setOnAction(event -> {
                    question q = getTableView().getItems().get(getIndex());

                    // Ajoutez une boîte de dialogue de confirmation
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation");
                    alert.setHeaderText("Confirmation de la suppression");
                    alert.setContentText("Voulez-vous vraiment supprimer cette question ?");

                    Optional<ButtonType> result = alert.showAndWait();

                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        try {
                            // Appel à la méthode de service pour supprimer la question
                            sq.supprimer(q);
                            // Mise à jour du TableView
                            tab.getItems().remove(q);
                        } catch (SQLException e) {
                            e.printStackTrace();  // Gérez l'exception de manière appropriée
                        }
                    }
                });
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

