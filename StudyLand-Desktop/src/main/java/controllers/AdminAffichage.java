package controllers;

import entities.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import services.ServiceAdmin;
import services.ServiceApprenant;
import services.ServiceUser;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AdminAffichage {

    @FXML
    private AnchorPane id_detailUser;

    @FXML
    private TableColumn<Admin, String> email_user;
    @FXML
    private TableColumn<Admin,Integer> id_user;
    @FXML
    private TableColumn<Admin, String> mdp_user;
    @FXML
    private TableColumn<Admin, String> nom_user;
    @FXML
    private TableColumn<Admin, String> pre_user;
    @FXML
    private TableColumn<Admin, Void> supprimer_user;
    @FXML
    private TableView<Admin> tab_Admin;

    @FXML
    private Label errorEmailLabel;
    @FXML
    private Label errorEmailLabel1;
    @FXML
    private TextField id_email;
    @FXML
    private TextField id_mdp;
    @FXML
    private TextField id_nom;
    @FXML
    private TextField id_prenom;
    private ServiceAdmin serviceAdmin;
    public AdminAffichage() {
        serviceAdmin = new ServiceAdmin();
    }
    @FXML
    private Button btn_ajouter;


    @FXML
    public void initialize() {
        nom_user.setCellValueFactory(new PropertyValueFactory<>("nom"));
        pre_user.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        email_user.setCellValueFactory(new PropertyValueFactory<>("email"));
        addActionColumn();
        id_mdp.setDisable(true);
        id_mdp.setOpacity(0.5);
        tab_Admin.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                User selectedUser = tab_Admin.getSelectionModel().getSelectedItem();
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
        initTable();
    }



    private void addActionColumn() {
        supprimer_user.setCellFactory(param -> new TableCell<>() {
            private final Button button = new Button("Supprimer");

            {
                button.setOnAction(event -> {
                    Admin admin = getTableView().getItems().get(getIndex());
                    try {
                        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                        confirmationAlert.setTitle("Confirmation de suppression");
                        confirmationAlert.setHeaderText("Supprimer l'admin ?");
                        confirmationAlert.setContentText("Êtes-vous sûr de vouloir supprimer l'admin " + admin.getNom() + " ?");

                        confirmationAlert.showAndWait().ifPresent(response -> {
                            if (response == ButtonType.OK) {
                                try {
                                    serviceAdmin.supprimer(admin);
                                    System.out.println("Supprimer l'admin : " + admin.getNom());
                                    tab_Admin.getItems().remove(admin);
                                    showAlert("Suppression réussie", "L'admin a été supprimé avec succès.");
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
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void initTable() {

        try {
            List<Admin> admins =serviceAdmin .afficher();
            ObservableList<Admin> observableList = FXCollections.observableArrayList(admins);
            tab_Admin.setItems(observableList);
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void Ajouter(ActionEvent event) {
        id_mdp.setText(EmailSender.getPasswordGenerte());
        try {
            Admin admin = new Admin(id_nom.getText(), id_prenom.getText(), id_email.getText(), id_mdp.getText());
            User user = new User();
            ServiceUser serviceUser = new ServiceUser();
            try {
                user = serviceUser.rechercheUserParEmail(id_email.getText());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            if (user != null) {
                showAlert("Erreur", "Un utilisateur avec cet email existe déjà. Impossible d'ajouter l'admin.");
            } else {
                if (admin == null) {
                    errorEmailLabel.setText("Veuillez remplir tous les champs correctement..");
                } else if (id_nom.getText().isEmpty() || id_prenom.getText().isEmpty() || id_email.getText().isEmpty() || id_mdp.getText().isEmpty() || !ValidationFormuaire.isEmail(id_email.getText())) {
                    showAlert2("Erreur", "Veuillez remplir tous les champs correctement.");
                } else {
                    serviceAdmin.ajouter(admin);
                    EmailSender.sendInfoAdmin(id_email.getText(), admin);
                    id_nom.clear();
                    id_prenom.clear();
                    id_email.clear();
                    showAlert("Succès", "L'admin a été ajouté avec succès.");
                }
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
            showAlert("Erreur", "Une erreur s'est produite lors de l'ajout d'un Admin. Veuillez réessayer.");
        } catch(Exception e){
            System.out.println(e.getMessage());
            showAlert("Erreur", "Une erreur s'est produite lors de l'ajout d'un Admin. Veuillez réessayer.");
        }
    }


    // Méthode pour afficher une alerte
    private void showAlert2(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}