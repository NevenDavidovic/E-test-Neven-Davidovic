import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HTMLReportGenerator {
    private static String getTimestampedFilePath() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String timestamp = LocalDateTime.now().format(formatter);
        return "report_" + timestamp + ".html";
    }

    private static String getFilePath() {
        return getTimestampedFilePath();
    }

    public static void appendToHTML(String content) {
        String filePath = getFilePath();
        try (FileWriter fw = new FileWriter(filePath, true);
             PrintWriter pw = new PrintWriter(fw)) {
            pw.println(content);
        } catch (IOException e) {
            System.err.println("Error writing to HTML file: " + e.getMessage());
        }
    }

    public static void createHTMLHeader() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
        String formattedDate = LocalDateTime.now().format(formatter);

        String header = "<html><head><title>Database Report</title>" +
                        "<style>" +
                        "body { font-family: Arial, sans-serif; margin: 20px; }" +
                        "img { display: block; margin: 0 auto; }" +
                        "</style>" +
                        "</head><body>" +
                        "<img src='https://www.evolva.hr/modules/m_evolva_web/images/og/evolva_logo_hr_big.png' alt='Evolva Logo'>" +
                        "<h1>Database Report</h1>" +
                        "<p>Date: " + formattedDate + "</p>";
        appendToHTML(header);
    }

    public static void createHTMLFooter() {
        String footer = "</body></html>";
        appendToHTML(footer);
    }
}
