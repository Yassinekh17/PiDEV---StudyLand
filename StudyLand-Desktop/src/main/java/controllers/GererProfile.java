package controllers;

import entities.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import security.Session;
import security.UserInfo;
import services.ServiceAdmin;
import services.ServiceApprenant;
import services.ServiceFormateur;
import services.ServiceUser;

import java.io.*;
import java.nio.channels.FileChannel;
import java.sql.SQLException;

public class GererProfile {
    @FXML
    private ImageView id_image;
    @FXML
    private Button btn_changer_image;
    @FXML
    private Label erroMessage;
    @FXML
    private TextField id_email;
    @FXML
    private TextField id_nom;
    @FXML
    private TextField id_prenom;
    public void setId_email(TextField id_email) {
        this.id_email = id_email;
    }
    public void setId_nom(TextField id_nom) {
        this.id_nom = id_nom;
    }

    public TextField getId_email() {
        return id_email;
    }

    public TextField getId_nom() {
        return id_nom;
    }

    public TextField getId_prenom() {
        return id_prenom;
    }

    String filepath = null, filename = null, fn = null; FileChooser fc = new FileChooser(); String uploads = "C:/pidev/StudyLand/src/main/resources/src";
    UserInfo userInfo = Session.getInstance().userInfo;
    @FXML
    public void initialize() {
        id_email.setDisable(true);
        id_email.setOpacity(0.5);
        ServiceUser serviceUser =new ServiceUser();
        try {
            User user = serviceUser.rechercheUserParEmail(userInfo.email);
            Image image = new Image("file:" + user.getImage());
            id_image.setImage(image);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    @FXML
    void ModifierUser(ActionEvent event) {
        String nouveauNom = id_nom.getText();
        String nouveauPrenom = id_prenom.getText();
        String nouvelEmail = id_email.getText();

        if (nouveauNom.isEmpty() || nouveauPrenom.isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs.");
            return;
        }

        if (nouveauNom.equals(userInfo.nom) && nouveauPrenom.equals(userInfo.prenom)) {
            showAlert2("Information", "Aucune modification n'a été apportée.");
            return;
        }

        try {
            User user1 = new User(userInfo.nom, userInfo.prenom, userInfo.email, userInfo.role, userInfo.id);
            if (!nouveauNom.equals(userInfo.nom)) {
                user1.setNom(nouveauNom);
            }
            if (!nouveauPrenom.equals(userInfo.prenom)) {
                user1.setPrenom(nouveauPrenom);
            }

            if (user1.getRole().equals("Apprenant")) {
                Apprenant apprenant = new Apprenant(userInfo.nom, userInfo.prenom, userInfo.email, userInfo.role, userInfo.id);
                if (!nouveauNom.equals(userInfo.nom)) {
                    apprenant.setNom(user1.getNom());
                }
                if (!nouveauPrenom.equals(userInfo.prenom)) {
                    apprenant.setPrenom(user1.getPrenom());
                }
                ServiceApprenant serviceApprenant = new ServiceApprenant();
                serviceApprenant.modifier(apprenant);
             if (user1.getRole().equals("Formateur")) {
                    Formateur formateur = new Formateur(userInfo.nom, userInfo.prenom, userInfo.email, userInfo.role, userInfo.id);
                    if (!nouveauNom.equals(userInfo.nom)) {
                        formateur.setNom(user1.getNom());
                    }
                    if (!nouveauPrenom.equals(userInfo.prenom)) {
                        formateur.setPrenom(user1.getPrenom());
                    }
                    ServiceFormateur serviceFormateur = new ServiceFormateur();
                    serviceFormateur.modifier(formateur);

                } else {
                    Admin admin = new Admin(userInfo.nom, userInfo.prenom, userInfo.email, userInfo.role, userInfo.id);
                    if (!nouveauNom.equals(userInfo.nom)) {
                        admin.setNom(user1.getNom());
                    }
                    if (!nouveauPrenom.equals(userInfo.prenom)) {
                        admin.setPrenom(user1.getPrenom());
                    }
                    ServiceAdmin serviceAdmin = new ServiceAdmin();
                    serviceAdmin.modifier(admin);
                }
                showAlert2("Succès", "Les informations de l'utilisateur ont été modifiées avec succès.");
            }} catch (SQLException e) {
        }
    }

    // Méthode pour afficher une alerte
    private void showAlert(String titre, String contenu) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(contenu);
        alert.showAndWait();
    }
    private void showAlert2(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    void modifierImage(ActionEvent event)  throws SQLException, FileNotFoundException, IOException {
        File file = fc.showOpenDialog(null);
        if (file != null) {
            id_image.setImage(new Image(file.toURI().toString()));
            filename = file.getName();
            filepath = file.getAbsolutePath();
            fn = filename;
            FileChannel source = new FileInputStream(filepath).getChannel();
            FileChannel dest = new FileOutputStream(uploads + filename).getChannel();
            dest.transferFrom(source, 0, source.size());
            ServiceUser serviceUser = new ServiceUser();
            serviceUser.modifierImage(userInfo.id, filepath);
            String newImageURL = uploads + filename;
            serviceUser.modifierImage(userInfo.id, filepath);
        }
    }
}
