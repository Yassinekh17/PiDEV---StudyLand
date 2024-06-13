package entities;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import entities.User;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CodeQRUser {
    public static byte[] generateQRCode(User user) throws WriterException, IOException {
        String userData = user.getNom() + "," + user.getPrenom() + "," + user.getEmail() + "," + user.getRole();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

        BitMatrix bitMatrix = new QRCodeWriter().encode(userData, BarcodeFormat.QR_CODE, 200, 200, hints);
        BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);

        ImageIO.write(image, "png", outputStream);
        return outputStream.toByteArray();
    }

    public static String decodeQRCode(byte[] qrCodeImage) throws IOException, NotFoundException {
        BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(qrCodeImage));
        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Result result = new MultiFormatReader().decode(bitmap);
        return result.getText();
    }

    public static void generateAndDisplayQRCode(User user, ImageView qrCodeImageView) {
        try {
            // Génération du code QR
            byte[] qrCodeData = generateQRCode(user);
            Image qrCodeImage = new Image(new ByteArrayInputStream(qrCodeData));

            // Affichage du code QR dans l'ImageView
            qrCodeImageView.setImage(qrCodeImage);

            // Démarrage du temporisateur pour masquer le code QR après 6 secondes
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(6), event -> {
                qrCodeImageView.setImage(null); // Effacer l'image du code QR
            }));
            timeline.play();
        } catch (Exception e) {
            e.printStackTrace();
            // Gérer les erreurs
        }
    }
}
