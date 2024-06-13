package entities;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import java.util.Random;

public class SmsSender {
    public static final String ACCOUNT_SID = "ACbc9f0ffd773dda3cb0fa2b79c7c8dc10";
    public static final String AUTH_TOKEN = "756a88c7b88115e34bbcc4cbfaa0b0df";
    public static String verificationCode;

    public static void sendVerificationCode(String toPhoneNumber) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        String twilioPhoneNumber = "+12678592125"; // Remplacez par votre numéro de téléphone Twilio réel
        // Générer un code aléatoire
        Random random = new Random();
        verificationCode = String.format("%04d", random.nextInt(10000));
        String messageBody = "Votre code de vérification pour StudyLand : " + verificationCode;
        String senderName = "StudyLand";
        Message message = Message.creator(
                        new PhoneNumber(toPhoneNumber),
                        new PhoneNumber(twilioPhoneNumber),
                        messageBody)
                .setFrom(new PhoneNumber(twilioPhoneNumber)) // Utilisez votre numéro de téléphone Twilio réel ici
                .create();
        System.out.println("Message SID: " + message.getSid());
    }





}