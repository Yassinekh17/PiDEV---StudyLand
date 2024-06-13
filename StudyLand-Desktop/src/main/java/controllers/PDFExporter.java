package controllers;

import entities.Project;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class PDFExporter {

    public static void exportToPDF(String title, List<Project> projects, String filePath) throws IOException {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                // Load Helvetica font
                try (InputStream fontStream = new FileInputStream("C:/Users/moham/OneDrive/Desktop/study/study_land/src/main/java/controllers/Helvetica.ttf")) {
                    PDType0Font pdfFont = PDType0Font.load(document, fontStream);

                    // Set the font and font size
                    contentStream.setFont(pdfFont, 12);

                    // Add title
                    contentStream.beginText();
                    contentStream.newLineAtOffset(20, 700);
                    contentStream.showText(title);
                    contentStream.endText();

                    // Add project details
                    int yOffset = 650;
                    int yOffsetIncrement = 150;
                    for (Project project : projects) {
                        StringBuilder projectDetails = new StringBuilder();
                        projectDetails.append("Nom du Projet: ").append(project.getNomProjet()).append(" | ");
                        projectDetails.append("Description: ").append(project.getDescProjet()).append(" | ");
                        projectDetails.append("Date Début: ").append(project.getDate_D()).append(" | ");
                        projectDetails.append("Date Fin: ").append(project.getDate_F()).append(" | ");
                        projectDetails.append("Nombre de tâches: ").append(project.getNb_taches());

                        contentStream.beginText();
                        contentStream.newLineAtOffset(20, yOffset);
                        contentStream.showText(projectDetails.toString());
                        contentStream.newLine();
                        contentStream.endText();

                        yOffset -= yOffsetIncrement; // Adjust this value based on your layout
                    }
                }
            }

            // Save the document to the specified file path
            document.save(filePath);
        }
    }
}
