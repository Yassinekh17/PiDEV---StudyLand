package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class Home {
    @FXML
    private Button id_popUp_seconnecter;

    @FXML
    private Button id_popUp_sinscrire;


    @FXML
    void onRecherche(ActionEvent event) {
        // Logique de recherche
    }

    @FXML
    void popUpSinscrire(ActionEvent event) {
        openDialog("/InscriptionApprenant.fxml", "Inscription Apprenant");
    }

    @FXML
    void popUpSeConnecter(ActionEvent event) {
        openDialog("/SeConnecter.fxml", "Se Connecter");
    }

    private void openDialog(String fxmlFile, String title) {
        try {
            // Chargement du fichier FXML de la page
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait(); // Attend que la boîte de dialogue soit fermée

            // Aucune action nécessaire après la fermeture de la pop-up, car showAndWait bloque l'exécution ici
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
