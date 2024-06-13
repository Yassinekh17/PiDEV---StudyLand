package controllers;

import entities.SmsSender;
import entities.User;
import entities.ValidationFormuaire;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import org.controlsfx.control.Notifications;
import security.Session;
import security.UserInfo;
import services.ServiceUser;

import java.sql.SQLException;

public class ModifierEmail {
    @FXML
    private Button btn_modifierEmail;

    @FXML
    private Label erroMessage;
    @FXML
    private TextField id_code;

    @FXML
    private Button btn_code;

    @FXML
    private TextField id_email;

    public TextField getId_email() {
        return id_email;
    }

    public void setId_email(String email) {
        id_email.setText(email);
    }

    @FXML
    private TextField id_email_new;



    @FXML
    private TextField id_num;

    @FXML
    private void initialize() {
        id_email.setDisable(true);

    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("[0-9]{8}");
    }

    private void showToastMessage(String message) {
        ImageView imageView = new ImageView("src/attendre.png");
        imageView.setFitHeight(20);
        imageView.setFitWidth(20);
        Notifications.create()
                .title("changer Email")
                .text(message)
                .graphic(imageView)
                .position(Pos.CENTER)
                .show();
    }

    @FXML
    void Envoyer_code(ActionEvent event) {
        String email = id_email.getText();
        String newEmail = id_email_new.getText();
        String phoneNumberInput = id_num.getText();

        if (email.isEmpty() || newEmail.isEmpty() || !ValidationFormuaire.isEmail(email) || !ValidationFormuaire.isEmail(newEmail)) {
            showToastMessage("Veuillez remplir tous les champs et vérifier les adresses email.");
        }
       else if (!isValidPhoneNumber(phoneNumberInput)) {
            showToastMessage("Numéro de téléphone incorrect. Veuillez saisir un numéro valide.");
        }
       else {
        String phoneNumberCleaned = id_num.getText();
        String toPhoneNumber = "+216" + phoneNumberInput;
        System.out.println(toPhoneNumber);
        SmsSender.sendVerificationCode(toPhoneNumber);
    }}

    @FXML
    void modifier_email(ActionEvent event) {
        if (!verifierCode()) {
            showToastMessage("le code n'est pas valide");
            return;
        }
        ServiceUser serviceUser = new ServiceUser();
        try {
            User user1 = serviceUser.rechercheUserParEmail(id_email.getText());
            if (user1 == null) {
                showToastMessage("Utilisateur introuvable avec cet email.");
                return;
            }
            serviceUser.modifierEmail(user1, id_email_new.getText());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            showToastMessage("Une erreur s'est produite lors de la modification de l'email.");
            return;
        }

        showToastMessage("Email modifié avec succès.");
    }

    Boolean verifierCode() {
        String enteredCode = id_code.getText();
        if (SmsSender.verificationCode.equals(enteredCode)) {
            return true;
        } else {
            showToastMessage("Code de vérification incorrect.");
            return false;
        }
    }



}