package controllers;
import entities.Apprenant;
import entities.EmailSender;
import entities.User;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import entities.Formateur;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import services.ServiceApprenant;
import services.ServiceFormateur;
import services.ServiceUser;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public class ForamteurAffichage {

    @FXML
    private AnchorPane id_detailUser;
    @FXML
    private Button btn_ajouter;
    @FXML
    private Label errorEmailLabel;
    @FXML
    private TextField id_email;
    @FXML
    private TextField id_mdp;
    @FXML
    private TextField id_nom;
    @FXML
    private TextField id_prenom;
    @FXML
    private TableColumn<Formateur, String> email_user;
    @FXML
    private TableColumn<Formateur, String> id_user;

    @FXML
    private Label errorMessage1;
    @FXML
    private TableColumn<Formateur, String> mdp_user;
    @FXML
    private TableColumn<Formateur, String> nom_user;
    @FXML
    private TableColumn<Formateur, String> pre_user;
    @FXML
    private TableColumn<Formateur, Void> supprimer_user;
    @FXML
    private TableView<Formateur> tab_formateur;
    private final ServiceFormateur serviceFormateur = new ServiceFormateur();
    @FXML
    public void initialize() {
        nom_user.setCellValueFactory(new PropertyValueFactory<>("nom"));
        pre_user.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        email_user.setCellValueFactory(new PropertyValueFactory<>("email"));
        addActionColumn();
        id_mdp.setDisable(true);
        id_mdp.setOpacity(0.5);
        tab_formateur.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                User selectedUser = tab_formateur.getSelectionModel().getSelectedItem();
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
                    Formateur formateur = getTableView().getItems().get(getIndex());
                    Alert confirmationAlert = new Alert(AlertType.CONFIRMATION);
                    confirmationAlert.setTitle("Confirmation de suppression");
                    confirmationAlert.setHeaderText("Supprimer formateur ?");
                    confirmationAlert.setContentText("Êtes-vous sûr de vouloir supprimer le formateur " + formateur.getNom() + " ?");
                    confirmationAlert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            try {
                                serviceFormateur.supprimer(formateur);
                                System.out.println("Supprimer le formateur : " + formateur.getNom());
                                tab_formateur.getItems().remove(formateur);
                                showAlert("Suppression réussie", "Le formateur a été supprimé avec succès.");
                            } catch (SQLException e) {
                                System.out.println(e.getMessage());
                            }
                        }
                    });
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
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void initTable() {
        try {
            List<Formateur> formateurs = serviceFormateur.afficher();
            ObservableList<Formateur> observableList = FXCollections.observableArrayList(formateurs);
            tab_formateur.setItems(observableList);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    @FXML
    void Ajouter(ActionEvent event) {
        ServiceUser serviceUser =new ServiceUser();
        ServiceApprenant serviceApprenant =new ServiceApprenant();
        id_mdp.setText(EmailSender.getPasswordGenerte());
        try {
            String nom = id_nom.getText();
            String prenom = id_prenom.getText();
            String email = id_email.getText();

            // Vérification si les champs sont vides
            if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty()) {
                showAlert("Erreur", "Veuillez remplir tous les champs.");
                return;
            }
            Formateur formateur = new Formateur(nom, prenom, email, id_mdp.getText());
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<Formateur>> violations = validator.validate(formateur);
            if (!violations.isEmpty()) {
                String errorMessageText = "";
                for (ConstraintViolation<Formateur> violation : violations) {
                    errorMessageText += violation.getMessage() + "\n";
                }
                showAlert("Erreur de Validation", errorMessageText);
            } else {
                User user = serviceUser.rechercheUserParEmail(email);
                if (user != null) {
                    // Vérification du rôle de l'utilisateur
                    if (user.getRole().equals("Apprenant")) {
                        Alert confirmationAlert = new Alert(AlertType.CONFIRMATION);
                        confirmationAlert.setTitle("Confirmation de Upgrate profile");
                        confirmationAlert.setHeaderText("Ce email est déjà utilisé pour notre Apprenant " + user.getNom() + "," + user.getPrenom());
                        confirmationAlert.setContentText("Êtes-vous sûr de Upgrate ce compte à un formateur ?");
                        confirmationAlert.showAndWait().ifPresent(response -> {
                            if (response == ButtonType.OK) {
                                try {
                                    Apprenant app =new Apprenant(user.getNom(), user.getPrenom(), user.getEmail(),user.getPassword(),user.getPassword());
                                    serviceApprenant.Upgrate(user.getId(), user.getEmail());
                                    Formateur FormateurNew =new Formateur(user.getId(), user.getNom(), user.getPrenom(), user.getEmail(), user.getPassword());
                                    EmailSender.sendInfoFormateur(user.getEmail(),FormateurNew);
                                    id_nom.clear();
                                    id_prenom.clear();
                                    id_email.clear();
                                    id_mdp.clear();
                                    showAlert("Succès", "Le compte a été mis à niveau avec succès.");
                                } catch (SQLException e) {
                                    System.out.println(e.getMessage());
                                    showAlert("Erreur", "Une erreur s'est produite lors de la mise à niveau du compte. Veuillez réessayer.");
                                } catch (Exception e) {
                                    System.out.println(e.getMessage());
                                    showAlert("Erreur", "Une erreur s'est produite lors de la mise à niveau du compte. Veuillez réessayer.");
                                }
                            }
                        });
                    } else {
                        // Si l'utilisateur existe mais n'est pas un apprenant
                        showAlert("Erreur", "Un utilisateur avec cet email existe déjà mais n'est pas un apprenant.");
                    }
                } else {
                    // Si l'utilisateur n'existe pas, ajouter le formateur directement
                    serviceFormateur.ajouter(formateur);
                    EmailSender.sendInfoFormateur(formateur.getEmail(), formateur);
                    id_nom.clear();
                    id_prenom.clear();
                    id_email.clear();
                    id_mdp.clear();
                    showAlert("Succès", "Le formateur a été ajouté avec succès.");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            showAlert("Erreur", "Une erreur s'est produite lors de l'ajout du formateur. Veuillez réessayer.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            showAlert("Erreur", "Une erreur s'est produite lors de l'ajout du formateur. Veuillez réessayer.");
        }
    }

    private void showAlert2(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}