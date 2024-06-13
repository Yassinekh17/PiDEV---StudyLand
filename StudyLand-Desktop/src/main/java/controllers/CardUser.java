package controllers;

import entities.CodeQRUser;
import entities.User;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class CardUser {
    @FXML
    private Label id_label_timer;
    @FXML
    private Label id_label_msg;
    @FXML
    private Label email;
    @FXML
    private Button btn_codeQ;
    @FXML
    private VBox cardsContainer;

    @FXML
    private Label nom_user;
    @FXML
    private ImageView id_image_User;

    @FXML
    private Label role;
    @FXML
    private Label prenom_user;

    public void initData(User user) {
        nom_user.setText(user.getNom());
        prenom_user.setText(user.getPrenom());
        email.setText(user.getEmail());
        role.setText(user.getRole());
        Image image = new Image("file:" + user.getImage());
        if (image != null) {
            id_image_User.setImage(image);
        } else {
            Image defaultImage = new Image("C:\\pidev\\StudyLand\\src\\main\\resources\\src\\77.png");
            id_image_User.setImage(defaultImage);
        }
    }

    public Label getEmail() {
        return email;
    }

    public Label getNom_user() {
        return nom_user;
    }

    public Label getPrenom_user() {
        return prenom_user;
    }

    @FXML
    private ImageView image_code;
    public Label getRole() {
        return role;
    }

    public void setRole(Label role) {
        this.role = role;
    }

    @FXML
    private Button id_telecharger;

    public void setEmail(Label email) {
        this.email = email;
    }

    public void setNom_user(Label nom_user) {
        this.nom_user = nom_user;
    }

    public void setPrenom_user(Label prenom_user) {
        this.prenom_user = prenom_user;
    }

    private final String templatePath = "template.pdf"; // Chemin vers votre modèle PDF

    @FXML
    void telecharger(ActionEvent event) {
        try {
            // Créer un document PDF
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);

            // Début de l'écriture dans le document
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
            float margin = 50;
            float yPosition = page.getMediaBox().getHeight() - margin;

            // Écrire les informations dans le document
            contentStream.beginText();
            contentStream.newLineAtOffset(margin, yPosition);
            contentStream.showText("Nom: " + nom_user.getText());
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Prénom: " + prenom_user.getText());
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Email: " + email.getText());
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Rôle: " + role.getText());
            contentStream.endText();

            contentStream.close();

            // Utilisation de FileChooser pour sélectionner l'emplacement de sauvegarde
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialFileName("user_card.pdf");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            File file = fileChooser.showSaveDialog(stage);

            // Enregistrer le document PDF
            if (file != null) {
                document.save(file);
                document.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private Timeline qrCodeTimer;

    @FXML
    void codeQ(ActionEvent event) {
        try {
            User user = new User();
            user.setNom(nom_user.getText());
            user.setPrenom(prenom_user.getText());
            user.setEmail(email.getText());
            user.setRole(role.getText());
            byte[] qrCodeData = CodeQRUser.generateQRCode(user);
            Image qrCodeImage = new Image(new ByteArrayInputStream(qrCodeData));
            image_code.setImage(qrCodeImage);

            // Affichage du code QR
            startQRCodeTimer();
            id_label_msg.setText("Vous pouvez scanner ce code dans          secondes.");
            // Affichage du compte à rebours dans l'étiquette
            AtomicInteger countdownSeconds = new AtomicInteger(6);
            id_label_timer.setText(Integer.toString(countdownSeconds.get()));
            Timeline countdownTimer = new Timeline(new KeyFrame(Duration.seconds(1), timerEvent -> { // Renommer la variable event
                countdownSeconds.decrementAndGet();
                id_label_timer.setText(Integer.toString(countdownSeconds.get()));
                if (countdownSeconds.get() == 0) {
                    stopQRCodeTimer();
                    id_label_msg.setText(null);
                    id_label_timer.setText(null);
                }  }));
            countdownTimer.setCycleCount(6); // Exécuter la timeline jusqu'à ce que le compte à rebours soit terminé
            countdownTimer.play();

        } catch (Exception exception) { // Renommer la variable event en exception
            exception.printStackTrace();
        }
    }


    private void startQRCodeTimer() {
        stopQRCodeTimer();
        qrCodeTimer = new Timeline(new KeyFrame(Duration.seconds(6), event -> {
            image_code.setImage(null);
        }));
        qrCodeTimer.setCycleCount(1);
        qrCodeTimer.play();
    }

    private void stopQRCodeTimer() {
        if (qrCodeTimer != null) {
            qrCodeTimer.stop();
        }
    }
}


