package entities;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Random;

public class EmailSender {
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    private static final String EMAIL_USERNAME = "studyland002@gmail.com";
    private static final String EMAIL_PASSWORD = "pmgt trqw atrj xyai";
    private static final byte[] imageBytes;
    private static final byte[] imageBytesCov;
    public  static  String verificationCode;

    static {
        try {
            // Charger l'image en cache une seule fois
            imageBytes = Files.readAllBytes(Paths.get("C:\\pidev\\StudyLand\\src\\main\\resources\\src\\logo.png"));
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la lecture de l'image : " + e.getMessage());
        }
    }
    static {
        try {
            // Charger l'image en cache une seule fois
            imageBytesCov = Files.readAllBytes(Paths.get("C:\\pidev\\StudyLand\\src\\main\\resources\\src\\covEmail.png"));
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la lecture de l'image : " + e.getMessage());
        }
    }

    private static final Session session = createSession();

    private static Session createSession() {
        Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        return Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_USERNAME, EMAIL_PASSWORD);
            }
        });
    }

    public static void sendInfoFormateur(String recipientEmail, Formateur formateur) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_USERNAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("StudyLand Resultat concours");
            Multipart multipart = new MimeMultipart();
            String emailContentWithSignature = "<html>" +
                    "<body>" +
                    "<p>Cher " + formateur.getNom() + ",</p>" +
                    "<p>Nous avons le plaisir de vous informer que vous avez été admis à StudyLand en tant que formateur.</p>" +
                    "<p><strong>Informations d'inscription à renseigner :</strong></p>" +
                    "<p>Votre email : " + formateur.getEmail() + "</p>" +
                    "<p>Votre mot de passe : " + formateur.getPassword() + "</p>" +
                    "<p>Cordialement,<br>StudyLand</p>" +
                    "<img src=\"cid:logo\" width=\"100px\">" +
                    "</body>" +
                    "</html>";
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setContent(emailContentWithSignature, "text/html");
            multipart.addBodyPart(textPart);

            MimeBodyPart imagePart = new MimeBodyPart();
            DataSource imageDataSource = new ByteArrayDataSource(imageBytes, "image/png");
            imagePart.setDataHandler(new DataHandler(imageDataSource));
            imagePart.setHeader("Content-ID", "<logo>");
            imagePart.setDisposition(MimeBodyPart.INLINE);
            multipart.addBodyPart(imagePart);

            message.setContent(multipart);
            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Erreur lors de l'envoi de l'email : " + e.getMessage());
        }
    }
    public static void sendInfoAdmin(String recipientEmail, Admin admin) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_USERNAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("StudyLand Resultat concours");
            Multipart multipart = new MimeMultipart();
            String emailContentWithSignature = "<html>" +
                    "<body>" +
                    "<p>Cher " + admin.getNom() + ",</p>" +
                    "<p>Nous avons le plaisir de vous informer que vous avez été admis à StudyLand en tant que admin.</p>" +
                    "<p><strong>Informations d'inscription à renseigner :</strong></p>" +
                    "<p>Votre email : " + admin.getEmail() + "</p>" +
                    "<p>Votre mot de passe : " + admin.getPassword() + "</p>" +
                    "<p>Cordialement,<br>StudyLand</p>" +
                    "<img src=\"cid:logo\" width=\"100px\">" +
                    "</body>" +
                    "</html>";
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setContent(emailContentWithSignature, "text/html");
            multipart.addBodyPart(textPart);

            MimeBodyPart imagePart = new MimeBodyPart();
            DataSource imageDataSource = new ByteArrayDataSource(imageBytes, "image/png");
            imagePart.setDataHandler(new DataHandler(imageDataSource));
            imagePart.setHeader("Content-ID", "<logo>");
            imagePart.setDisposition(MimeBodyPart.INLINE);
            multipart.addBodyPart(imagePart);

            message.setContent(multipart);
            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Erreur lors de l'envoi de l'email : " + e.getMessage());
        }
    }


    public static void sendWelcomeEmailWithSignature(String recipientEmail, String nom) {
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_USERNAME));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
            message.setSubject("Bienvenue dans notre Platforme StudyLand");
            Multipart multipart = new MimeMultipart();
            String emailContentWithSignature = "<html>" +
                    "<img src=\"cid:cov\" width=\"80%\">" +
                    "<body>" +
                    "<p>Cher " + nom + ",</p>" +
                    "<p><strong>Merci de vous être inscrit à notre plateforme.</strong></p>" +
                    "<p>Cordialement,<br>StudyLand</p>" +
                    "</body>" +
                    "</html>";
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setContent(emailContentWithSignature, "text/html");
            multipart.addBodyPart(textPart);

            MimeBodyPart imagePart1 = new MimeBodyPart();
            DataSource imageDataSource1 = new ByteArrayDataSource(imageBytesCov, "image/png");
            imagePart1.setDataHandler(new DataHandler(imageDataSource1));
            imagePart1.setHeader("Content-ID", "<cov>");
            imagePart1.setDisposition(MimeBodyPart.INLINE);
            multipart.addBodyPart(imagePart1);
            message.setContent(multipart);
            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Erreur lors de l'envoi de l'email : " + e.getMessage());
        }
    }


    //Inscrit
    public static void sendVerficationEmail(String recipientEmail, String nom) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_USERNAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Vérification d'authentification StudyLand");
            Multipart multipart = new MimeMultipart();
            String emailContentWithSignature = "<html>" +
                    "<body>" +
                    "<p>Cher " + nom + ",</p>" +
                    "<p>Nous avons le plaisir de vous informer que vous avez été Inscrit  à StudyLand en tant que Apprenant .</p>" +
                   "<p> Veuillez répondre à cet e-mail en confirmant votre inscription en incluant le mot-clé 'CONFIRMATION' dans votre réponse.</p>"+
                    "<p>Cordialement,<br>StudyLand</p>" +
                    "<img src=\"cid:logo\" width=\"100px\">" +
                    "</body>" +
                    "</html>";
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setContent(emailContentWithSignature, "text/html");
            multipart.addBodyPart(textPart);

            MimeBodyPart imagePart = new MimeBodyPart();
            DataSource imageDataSource = new ByteArrayDataSource(imageBytes, "image/png");
            imagePart.setDataHandler(new DataHandler(imageDataSource));
            imagePart.setHeader("Content-ID", "<logo>");
            imagePart.setDisposition(MimeBodyPart.INLINE);
            multipart.addBodyPart(imagePart);

            message.setContent(multipart);
            Transport.send(message);
        } catch (MessagingException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Erreur lors de l'envoi de l'email : " + e.getMessage());
        }
    }


    public static void sendVerificationCode(String recipientEmail) {
        String  code = generateVerificationCode();
        verificationCode=code;
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_USERNAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Code de vérification");
            message.setText("Votre code de vérification est : " + code);
            Transport.send(message);
            System.out.println("Code de vérification envoyé avec succès à : " + recipientEmail);
        } catch (MessagingException e) {
            throw new RuntimeException("Erreur lors de l'envoi de l'email : " + e.getMessage());
        }
    }

    public static String generateVerificationCode() {
        Random random = new Random();
        int code = 1000 + random.nextInt(9000);
        return String.valueOf(code);
    }
    //
    public static String generateRandomPassword() {
        Random random = new Random();
        StringBuilder password = new StringBuilder();
        password.append("STUDY");
        for (int i = 0; i < 4; i++) {
            int digit = random.nextInt(10);
            password.append(digit);
        }
        return password.toString();
    }
      static  String passwordGenerate="";
    public  static  String getPasswordGenerte(){
        return  passwordGenerate=generateRandomPassword();
    }



}
