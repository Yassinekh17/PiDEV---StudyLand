package controllers;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.*;
import java.io.FileOutputStream;
import java.io.IOException;
public class WordExporter {

    public static void exportToWord(String title, String content, String filePath) throws IOException {
        try (XWPFDocument document = new XWPFDocument()) {
            // Add title
            XWPFParagraph titleParagraph = document.createParagraph();
            XWPFRun titleRun = titleParagraph.createRun();
            titleRun.setText(title);
            titleRun.setFontSize(16);
            titleRun.setColor("FF0000"); // Red color

            // Add content
            XWPFParagraph contentParagraph = document.createParagraph();
            XWPFRun contentRun = contentParagraph.createRun();
            contentRun.setText(content);

            try (FileOutputStream out = new FileOutputStream(filePath)) {
                document.write(out);
            }
        }
    }
}
