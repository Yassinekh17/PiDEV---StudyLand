package controllers;
import entities.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import entities.Apprenant;
import javafx.fxml.FXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import services.ServiceApprenant;
import services.ServiceUser;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AffichageApprenant {
    @FXML
    private AnchorPane id_detailUser;

    @FXML
    private TableColumn<Apprenant, Void> modifierButton;
    @FXML
    private TableColumn<Apprenant, String> email_user;

    @FXML
    private TableColumn<Apprenant, Integer> id_user;
    @FXML
    private TableColumn<Apprenant, String> mdp_user;

    @FXML
    private TableColumn<Apprenant, String> nom_user;

    @FXML
    private TableColumn<Apprenant, String> pre_user;

    @FXML
    private TableColumn<Apprenant,Void> supprimer_user;

    @FXML
    private TableView<Apprenant> tab_Apprenant;
    private ServiceApprenant serviceApprenant;

    public AffichageApprenant() {
        serviceApprenant = new ServiceApprenant();
    }

    @FXML
    public void initialize() {
        nom_user.setCellValueFactory(new PropertyValueFactory<>("nom"));
        pre_user.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        email_user.setCellValueFactory(new PropertyValueFactory<>("email"));
        tab_Apprenant.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                User selectedUser = tab_Apprenant.getSelectionModel().getSelectedItem();
                if (selectedUser != null) {
                    ServiceUser serviceUser = new ServiceUser();
                    try {
                        User selectUs = serviceUser.rechercheUserParEmail(selectedUser.getEmail());
                        int userId = selectUs.getId();

                        System.out.println(selectUs);
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/CardUser.fxml"));
                        AnchorPane card = loader.load();
                        CardUser controller = loader.getController();
                        controller.initData(selectUs);
                        id_detailUser.getChildren().clear();
                        id_detailUser.getChildren().add(card);
                    } catch (IOException | SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        addActionColumn();
        initTable();
    }

    private void addActionColumn() {
        supprimer_user.setCellFactory(param -> new TableCell<>() {
            private final Button button = new Button("Supprimer");

            {
                button.setOnAction(event -> {
                    Apprenant apprenant = getTableView().getItems().get(getIndex());
                    try {
                        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                        confirmationAlert.setTitle("Confirmation de suppression");
                        confirmationAlert.setHeaderText("Supprimer l'apprenant ?");
                        confirmationAlert.setContentText("Êtes-vous sûr de vouloir supprimer l'apprenant " + apprenant.getNom() + " ?");

                        confirmationAlert.showAndWait().ifPresent(response -> {
                            if (response == ButtonType.OK) {
                                try {
                                    serviceApprenant.supprimer(apprenant);
                                    System.out.println("Supprimer l'apprenant : " + apprenant.getNom());
                                    tab_Apprenant.getItems().remove(apprenant);
                                    showAlert("Suppression réussie", "L'apprenant a été supprimé avec succès.");
                                } catch (SQLException e) {
                                    System.out.println(e.getMessage());
                                }
                            }
                        });
                    } finally {

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

    // Méthode pour afficher une alerte
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void initTable() {

        try {
            List<Apprenant> apprenants =serviceApprenant .afficher();
            ObservableList<Apprenant> observableList = FXCollections.observableArrayList(apprenants);
            tab_Apprenant.setItems(observableList);
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }

}}
