    package controllers;

    import entities.evaluation;
    import entities.response;
    import javafx.event.ActionEvent;
    import javafx.fxml.FXML;
    import javafx.fxml.FXMLLoader;
    import javafx.scene.Node;
    import javafx.scene.Parent;
    import javafx.scene.Scene;
    import javafx.scene.control.*;
    import javafx.scene.control.cell.PropertyValueFactory;
    import javafx.scene.layout.AnchorPane;
    import javafx.scene.layout.StackPane;
    import javafx.stage.Stage;
    import services.EvalService;

    import java.io.IOException;
    import java.sql.SQLException;
    import java.util.List;

    public class Lesevaluation {
        @FXML
        private StackPane centerPane;

        public StackPane getCenterPane() {
            return centerPane;
        }

        public void setCenterPane(StackPane centerPane) {
            this.centerPane = centerPane;
        }

        EvalService evalService = new EvalService();
        @FXML
        private TableView<evaluation> tableevaluation;

        @FXML
        private TableColumn<evaluation, String> titre;
        @FXML
        private ListView<String> evaluationListView;
        @FXML
        private TableColumn<evaluation,Void> supprimer;
        @FXML
        private ScrollPane Scroll;
        @FXML
        private TextField idsup;
        @FXML
        public void initialize() {
            titre.setCellValueFactory(new PropertyValueFactory<>("titre_evaluation"));

            // Configure the "Supprimer" column to use a button cell


            try {
                List<evaluation> evaluations = evalService.afficher();
                displayEvaluationsInTable(evaluations);
            } catch (SQLException e) {
                e.printStackTrace(); // Handle the exception appropriately
            }
            addActionColumn();
        }
        private void displayEvaluationsInTable(List<evaluation> evaluations) {
            // Clear existing items
            tableevaluation.getItems().clear();

            // Add evaluations to the TableView
            tableevaluation.getItems().addAll(evaluations);
        }


        private void addActionColumn() {
            supprimer.setCellFactory(param -> new TableCell<>() {
                private final Button button = new Button("Supprimer");

                {
                    button.setOnAction(event -> {
                         evaluation evaluation = getTableView().getItems().get(getIndex());
                        try {
                            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                            confirmationAlert.setTitle("Confirmation de suppression");
                            confirmationAlert.setHeaderText("Supprimer Évaluations ?");
                            confirmationAlert.setContentText("Êtes-vous sûr de vouloir supprimer Évaluations " + evaluation.getTitre_evaluation() + " ?");

                            confirmationAlert.showAndWait().ifPresent(response -> {
                                if (response == ButtonType.OK) {
                                    try {
                                        evalService.supprimer(evaluation);
                                        tableevaluation.getItems().remove(evaluation);
                                        showAlert("Suppression réussie", " Évaluations a été supprimé avec succès.");
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




        public void ajouterevaluation(ActionEvent actionEvent) {
            centerPane.getChildren().clear();
            FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/ajoutevaluation.fxml"));
            try {
                Parent root = loader1.load();
                centerPane.getChildren().clear();
                centerPane.getChildren().add(root);
                Ajoutevaluation controller = loader1.getController();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

        public void modifierevaluation(ActionEvent actionEvent) {
            centerPane.getChildren().clear();
            FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/modifierevaluation.fxml"));
            try {
                Parent root = loader1.load();
                centerPane.getChildren().clear();
                centerPane.getChildren().add(root);
                Modifierevaluation controller = loader1.getController();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }



        public void supprimer(ActionEvent actionEvent) {

        }


    }
