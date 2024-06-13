package controllers;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.Random;

public class Captcha {
    @FXML
    private VBox pnlCaptcha;

    @FXML
    private Label captchaLabel;

    @FXML
    private TextField captchaField;

    // Existing code...

    @FXML
    void initialize() {
        generateCaptcha();
    }

    private void generateCaptcha() {
        String captchaText = generateRandomString(6);
        captchaLabel.setText(captchaText);
    }

    private String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder captcha = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            captcha.append(characters.charAt(random.nextInt(characters.length())));
        }

        return captcha.toString();
    }

    @FXML
    void handleCaptchaVerification() {
        String userEnteredCaptcha = captchaField.getText();
        String actualCaptcha = captchaLabel.getText();

        if (userEnteredCaptcha.equalsIgnoreCase(actualCaptcha)) {
            System.out.println("CAPTCHA verification successful!");
            generateCaptcha();
        } else {
            System.out.println("CAPTCHA verification failed. Please try again.");
        }
    }
}

