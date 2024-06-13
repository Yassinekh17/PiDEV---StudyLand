package controllers;
import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.AuthorizationCodeResponseUrl;
import com.google.api.client.auth.oauth2.AuthorizationCodeTokenRequest;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;
import com.google.api.services.calendar.model.Events;
import com.google.api.client.util.store.FileDataStoreFactory;

import com.google.api.services.calendar.model.Event.Reminders;
import com.google.api.services.calendar.CalendarScopes;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class CalendarController {
    private static final String CLIENT_ID = "1096571340757-egfj21fbev6tro0et2f16ob5aag267ob.apps.googleusercontent.com";
    private static final String CLIENT_SECRET = "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";
    private static final String REDIRECT_URI = "http://localhost";



    private static AuthorizationCodeFlow getFlow() throws IOException {
        try {
            return new GoogleAuthorizationCodeFlow.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    JacksonFactory.getDefaultInstance(),
                    CLIENT_ID, CLIENT_SECRET,
                    Collections.singletonList(CalendarScopes.CALENDAR))
                    .setAccessType("offline")
                    .setApprovalPrompt("force")
                    .build();
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    private static Credential exchangeCodeForCredentials(AuthorizationCodeFlow flow, String authorizationCode)
            throws IOException {
        AuthorizationCodeTokenRequest tokenRequest = flow.newTokenRequest(authorizationCode)
                .setRedirectUri(REDIRECT_URI)
                .set("access_type", "offline"); // Set access type to offline
        return flow.createAndStoreCredential(tokenRequest.execute(), "user");
    }
    public static void addEventToCalendar(int projectId, Date startDate, Date endDate, String projectName)
            throws IOException {
        // Create a new Calendar service
        AuthorizationCodeFlow flow = getFlow();
        String authorizationUrl = flow.newAuthorizationUrl().setRedirectUri(REDIRECT_URI).build();
        System.out.println("Please open the following URL in your browser then type the authorization code:");
        System.out.println(authorizationUrl);

// Assume you have received the authorization code as user input
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the authorization code: ");
        String authorizationCode = scanner.nextLine();

// Exchange authorization code for credentials
        Credential credential = exchangeCodeForCredentials(flow, authorizationCode);

        Calendar service = null;
        try {
            service = new Calendar.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    JacksonFactory.getDefaultInstance(),
                    credential)
                    .setApplicationName("Client de bureau 1")
                    .build();
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }

        // Prepare the event data
        Event event = new Event()
                .setSummary("Project: " + projectName)
                .setDescription("Project start and end dates");

        EventDateTime startDateTime = new EventDateTime()
                .setDateTime(new com.google.api.client.util.DateTime(startDate))
                .setTimeZone("UTC");
        event.setStart(startDateTime);

        EventDateTime endDateTime = new EventDateTime()
                .setDateTime(new com.google.api.client.util.DateTime(endDate))
                .setTimeZone("UTC");
        event.setEnd(endDateTime);

        // Add a reminder for the event (optional)
        Event.Reminders reminders = new Reminders()
                .setUseDefault(false)
                .setOverrides(Collections.singletonList(
                        new EventReminder().setMethod("popup").setMinutes(10)));
        event.setReminders(reminders);

        // Add the event to the primary calendar
        service.events().insert("primary", event).execute();
    }
}
