import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class MoneyCounter {
    private String folderPath;

    // Default constructor
    public MoneyCounter() {
        this.folderPath = "C:\\data";
    }

    // Constructor with custom path
    public MoneyCounter(String folderPath) {
        this.folderPath = folderPath;
    }

   

    public void displayPath() {
        System.out.println("Trenutni path: " + this.folderPath);
    }

    public void setPath(String newPath) {
        this.folderPath = newPath;
    }

    public void setDefaultPath() {
        this.folderPath = "C:\\data";
    }
    
    public void addToDatabase() {
        // Initialize DatabaseOperations instance
        DatabaseOperations dbOps = new DatabaseOperations("evolva_db.db");
        
        // Access the directory and list CSV files
        File folder = new File(this.folderPath);
        File[] listOfFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".csv"));
        
        // Check if there are any CSV files
        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                processCsvFile(file, dbOps);
            }
        } else {
            System.out.println("No CSV files found in the specified folder.");
        }
    }

    // Method to process a single CSV file
    private void processCsvFile(File file, DatabaseOperations dbOps) {
        // Get the filename without the extension
        String countryName = getFilenameWithoutExtension(file.getName());

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String cityName = parts[0].trim();
                    String currencyName = parts[1].trim();
                    double amount = Double.parseDouble(parts[2].trim());

                    // Add the transaction to the database
                    dbOps.addTransaction(cityName, countryName, currencyName, amount);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        }
    }

    // Method to get the filename without the extension
    private String getFilenameWithoutExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0) {
            return fileName.substring(0, dotIndex);
        }
        return fileName; // No extension found
    }


    public void count() {
        System.out.println("Searching for CSV files in \"" + this.folderPath + "\"");

        File folder = new File(this.folderPath);
        if (!folder.exists()) {
            System.out.println("The directory " + this.folderPath + " does not exist.");
            return;
        }

        if (!folder.isDirectory()) {
            System.out.println("The path " + this.folderPath + " is not a directory.");
            return;
        }

        // System.out.println("The directory " + this.folderPath + " exists.");
        File[] listOfFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".csv"));

        if (listOfFiles == null || listOfFiles.length == 0) {
            System.out.println("No CSV files found in the directory.");
            return;
        }

        // From here we sum the values
        Map<String, Double> totalSums = new HashMap<>();

        // for loop
        for (File file : listOfFiles) {
            System.out.println("Processing file: " + file.getName());
            Map<String, Double> fileSums = new HashMap<>();

            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    // Skip empty lines
                    if (line.trim().isEmpty()) {
                        continue;
                    }

                    String[] values = line.split(",");
                    // Ensure the line has exactly three parts
                    if (values.length == 3) {
                        String currency = values[1].trim();
                        double amount = Double.parseDouble(values[2].trim());
                        fileSums.put(currency, fileSums.getOrDefault(currency, 0.0) + amount);
                    } else {
                        System.out.println("Skipping malformed line in file " + file.getName() + ": " + line);
                    }
                }
            } catch (IOException e) {
                System.out.println("Error reading file: " + file.getName());
                e.printStackTrace();
            }

            System.out.println("\"" + file.getName() + "\" found: ");
            printSums(fileSums);

            // Add the file sums to the total sums
            for (Map.Entry<String, Double> entry : fileSums.entrySet()) {
                totalSums.put(entry.getKey(), totalSums.getOrDefault(entry.getKey(), 0.0) + entry.getValue());
            }
        }

        System.out.println("Total sums for all files:");
        printSums(totalSums);
    }

    private void printSums(Map<String, Double> sums) {
        String[] order = {"EUR", "HRK", "USD"};
        for (String currency : order) {
            if (sums.containsKey(currency)) {
                System.out.println(currency + ": " + sums.get(currency));
            }
        }
    }
}
