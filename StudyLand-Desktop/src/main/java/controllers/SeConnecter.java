package controllers;

import com.github.scribejava.apis.GoogleApi20;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.oauth.OAuth20Service;
import entities.ValidationFormuaire;
import javafx.application.HostServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import security.Session;
import security.UserInfo;

import javax.sound.midi.Soundbank;
import java.io.IOException;

public class SeConnecter {
    @FXML
    private Label errorMdpLabel;

    @FXML
    private Button btn_connecter;

    @FXML
    private Button btn_connecter1;
    @FXML
    private TextField id_email;

    @FXML
    private TextField id_mdp;

    @FXML
    public TextField getId_email() {
        return id_email;
    }

    public TextField getId_mdp() {
        return id_mdp;
    }

    public void setId_email(TextField id_email) {
        this.id_email = id_email;
    }

    public void setId_mdp(TextField id_mdp) {
        this.id_mdp = id_mdp;
    }

    @FXML
    private Hyperlink id_mdp_oublier;

    @FXML
    void mdpOublier(ActionEvent event) {
        FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/mdpOublie.fxml"));
        try {
            Parent root = loader1.load();
            MdpOublie controller = loader1.getController();
            id_email.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }


    }

    @FXML
    void seConnecter(ActionEvent event) {
        String email = id_email.getText();
        String mdp = id_mdp.getText();
        Session session = Session.getInstance();
        session.login(id_email.getText(), id_mdp.getText());
        UserInfo userinfo = Session.getInstance().userInfo;

        if (email.isEmpty() || mdp.isEmpty()) {
            errorMdpLabel.setText("Veuillez remplir tous les champs.");
        } else if (!ValidationFormuaire.isEmail(email) && !mdp.isEmpty()) {
            errorMdpLabel.setText("Email invalide !");
        } else if (userinfo == null) {
            errorMdpLabel.setText("Veuillez vérifier vos informations.");

        } else if (userinfo.role.equals("Apprenant")) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ProfileApprenant.fxml"));
            try {
                Parent root = loader.load();
                ProfileApprenant controller = loader.getController();
                controller.getId_nom1().setText( userinfo.nom);
                id_email.getScene().setRoot(root);
            } catch (IOException e) {
                System.out.println(e.getMessage());

                System.out.println("exception from se connecter ");
            }

        } else if (userinfo.role.equals("Admin")) {
            FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/DashboardAdmin.fxml"));
            try {
                Parent root = loader1.load();
                DashboardAdmin controller = loader1.getController();
                controller.getId_nom1().setText(userinfo.nom);
                id_email.getScene().setRoot(root);
            } catch (IOException e) {
                System.out.println(e.getMessage());
                System.out.println("exeption from se connecter  ya syrine");
            }
        } else if (userinfo.role.equals("Formateur")) {
            FXMLLoader loader2 = new FXMLLoader(getClass().getResource("/DashboardFormateur.fxml"));
            try {
                Parent root = loader2.load();
                DashboardFormateur controller = loader2.getController();
                controller.getId_nom1().setText(userinfo.nom);
                id_email.getScene().setRoot(root);
            } catch (IOException e) {
                System.out.println(e.getMessage());
                System.out.println("exception from se connecter ");
            }
        }
    }

    @FXML
    void pageInscription(ActionEvent event) {
        FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/InscriptionApprenant.fxml"));
        try {
            Parent root = loader1.load();
            InscriptionApprenant controller = loader1.getController();
            id_email.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void seConnecterAvecGoogle(ActionEvent event) {
    }
}

