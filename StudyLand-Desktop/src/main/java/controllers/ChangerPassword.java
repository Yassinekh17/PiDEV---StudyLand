package controllers;

import entities.User;
import entities.ValidationFormuaire;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.input.MouseEvent;
import security.Session;
import security.UserInfo;
import services.ServiceUser;

import java.io.IOException;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
public class ChangerPassword {
    @FXML
    private Label erroMessage;
    @FXML
    private PasswordField confirmer_new_password;

    @FXML
    private PasswordField new_password;

    static  public User user;

    @FXML
    void modifier_mdp(ActionEvent event) {
        String newPassword = new_password.getText();
        String confirmedPassword = confirmer_new_password.getText();
        if (!newPassword.isEmpty() && newPassword.equals(confirmedPassword)) {
            if (!ValidationFormuaire.isValidPassword(newPassword)) {
                erroMessage.setText("Le mot de passe est faible (moins de 5 caractères).");
                return;
            }
            ServiceUser serviceUser = new ServiceUser();
            try {
                user = serviceUser.UpdateMdp(user, newPassword);
                // Affichage de l'alerte de validation
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Modification de mot de passe réussie");
                alert.setHeaderText(null);
                alert.setContentText("Votre mot de passe a été modifié avec succès.");
                alert.showAndWait();

                // Migration vers la page de connexion
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Seconnecter.fxml"));
                try {
                    Parent root = loader.load();
                    SeConnecter controller = loader.getController();
                    controller.getId_email().setText(user.getEmail());
                    controller.getId_mdp().setText(user.getPassword());
                    new_password.getScene().setRoot(root);
                } catch (IOException e) {
                    // Gérer l'exception d'E/S si nécessaire
                    e.printStackTrace();
                }
            } catch (Exception e) {
                // Gérer les exceptions si nécessaire
                e.printStackTrace();
            }
        } else {
            erroMessage.setText("Les champs de mot de passe ne correspondent pas ou sont vides.");
        }
    }
    @FXML
    void Mdp1(MouseEvent event) {
        FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/mdpOublie.fxml"));
        try {
            Parent root = loader1.load();
            SeConnecter controller = loader1.getController();
            new_password.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


}

