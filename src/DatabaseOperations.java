import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DatabaseOperations {
    private String databasePath;

    public DatabaseOperations(String databasePath) {
        this.setDatabasePath(databasePath);
    }

    public void testConnection() {
        try {
            SQLiteConnector connector = new SQLiteConnector(getDatabasePath());
            Connection connection = connector.getConnection();

            if (connection != null && !connection.isClosed()) {
                System.out.println("Connected to the database successfully.");
            } else {
                System.out.println("Failed to connect to the database.");
            }

            connector.closeConnection();
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
   
    
    
    
    
    // Method to check if a country already exists
    private boolean countryExists(Connection connection, String countryName) throws SQLException {
        String query = "SELECT COUNT(*) FROM country WHERE country_name = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, countryName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
    
    // Method to insert a new country
    private void insertCountry(Connection connection, String countryName) throws SQLException {
        String query = "INSERT INTO country (country_name) VALUES (?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, countryName);
            pstmt.executeUpdate();
        }
    }
    
    // Method to check and insert a country
    public void addCountry(String countryName) {
        SQLiteConnector connector = null;
        Connection connection = null;
        try {
            connector = new SQLiteConnector(getDatabasePath());
            connection = connector.getConnection();

            if (countryExists(connection, countryName)) {
                System.out.println("Country \"" + countryName + "\" already exists.");
            } else {
                insertCountry(connection, countryName);
                System.out.println("Country \"" + countryName + "\" added successfully.");
            }

        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.err.println("Failed to close connection: " + e.getMessage());
                }
            }
            if (connector != null) {
                try {
                    connector.closeConnection();
                } catch (SQLException e) {
                    System.err.println("Failed to close connector: " + e.getMessage());
                }
            }
        }
    }
    
 // Method to check if a city already exists
    private boolean cityExists(Connection connection, String cityName) throws SQLException {
        String query = "SELECT COUNT(*) FROM city WHERE city_name = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, cityName);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    // Method to insert a new city
    private void insertCity(Connection connection, String cityName, int countryId) throws SQLException {
        String query = "INSERT INTO city (city_name, country_id) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, cityName);
            pstmt.setInt(2, countryId);
            pstmt.executeUpdate();
        }
    }

    // Method to get the ID of a country by its name
    private int getCountryId(Connection connection, String countryName) throws SQLException {
        String query = "SELECT id FROM country WHERE country_name = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, countryName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                } else {
                    throw new SQLException("Country not found: " + countryName);
                }
            }
        }
    }

    // Helper method to get or insert a country and return its ID
    private int getOrInsertCountryId(Connection connection, String countryName) throws SQLException {
        if (countryExists(connection, countryName)) {
            return getCountryId(connection, countryName);
        } else {
            insertCountry(connection, countryName); // Ensure this method exists as part of the class
            return getCountryId(connection, countryName);
        }
    }

    // Method to add a city
    public void addCity(String cityName, String countryName) {
        SQLiteConnector connector = null;
        Connection connection = null;
        try {
            // Initialize SQLiteConnector and get Connection
            connector = new SQLiteConnector(getDatabasePath());
            connection = connector.getConnection();

            // Get or insert the country and retrieve its ID
            int countryId = getOrInsertCountryId(connection, countryName);

            // Check if the city exists, and insert it if it does not
            if (cityExists(connection, cityName)) {
                System.out.println("City \"" + cityName + "\" already exists.");
            } else {
                insertCity(connection, cityName, countryId);
                System.out.println("City \"" + cityName + "\" added successfully.");
            }

        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Close the connection and the connector
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.err.println("Failed to close connection: " + e.getMessage());
                }
            }
            if (connector != null) {
                try {
                    connector.closeConnection();
                } catch (SQLException e) {
                    System.err.println("Failed to close connector: " + e.getMessage());
                }
            }
        }
    }

 // Method to check if a currency already exists
    private boolean currencyExists(Connection connection, String currencyName) throws SQLException {
        String query = "SELECT COUNT(*) FROM currency WHERE currency_name = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, currencyName);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

 // Method to insert a new currency
    private void insertCurrency(Connection connection, String currencyName) throws SQLException {
        String query = "INSERT INTO currency (currency_name) VALUES (?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, currencyName);
            pstmt.executeUpdate();
        }
    }
    
 // Method to check and insert a currency
    public void addCurrency(String currencyName) {
        SQLiteConnector connector = null;
        Connection connection = null;
        try {
            connector = new SQLiteConnector(getDatabasePath());
            connection = connector.getConnection();

            if (currencyExists(connection, currencyName)) {
                System.out.println("Currency \"" + currencyName + "\" already exists.");
            } else {
                insertCurrency(connection, currencyName);
                System.out.println("Currency \"" + currencyName + "\" added successfully.");
            }

        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.err.println("Failed to close connection: " + e.getMessage());
                }
            }
            if (connector != null) {
                try {
                    connector.closeConnection();
                } catch (SQLException e) {
                    System.err.println("Failed to close connector: " + e.getMessage());
                }
            }
        }
    }
 // Method to insert a new transaction
    private void insertTransaction(Connection connection, int cityId, int currencyId, double amount) throws SQLException {
        String query = "INSERT INTO  \"transaction\" (city_id, currency_id, amount) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, cityId);
            pstmt.setInt(2, currencyId);
            pstmt.setDouble(3, amount);
            pstmt.executeUpdate();
        }
    }

 // Method to add a transaction
    public void addTransaction(String cityName, String countryName, String currencyName, double amount) {
        SQLiteConnector connector = null;
        Connection connection = null;
        try {
            connector = new SQLiteConnector(getDatabasePath());
            connection = connector.getConnection();

            // Get or insert the country and retrieve its ID
            int countryId = getOrInsertCountryId(connection, countryName);

            // Get or insert the city and retrieve its ID
            if (!cityExists(connection, cityName)) {
                insertCity(connection, cityName, countryId);
            }
            int cityId = getCityId(connection, cityName);

            // Get or insert the currency and retrieve its ID
            if (!currencyExists(connection, currencyName)) {
                insertCurrency(connection, currencyName);
            }
            int currencyId = getCurrencyId(connection, currencyName);

            // Insert the transaction
            insertTransaction(connection, cityId, currencyId, amount);
            System.out.println("Transaction added successfully: " + cityName + ", " + currencyName + ", " + amount);

        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.err.println("Failed to close connection: " + e.getMessage());
                }
            }
            if (connector != null) {
                try {
                    connector.closeConnection();
                } catch (SQLException e) {
                    System.err.println("Failed to close connector: " + e.getMessage());
                }
            }
        }
    }

    // Method to get the ID of a city by its name
    private int getCityId(Connection connection, String cityName) throws SQLException {
        String query = "SELECT id FROM city WHERE city_name = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, cityName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                } else {
                    throw new SQLException("City not found: " + cityName);
                }
            }
        }
    }

    // Method to get the ID of a currency by its name
    private int getCurrencyId(Connection connection, String currencyName) throws SQLException {
        String query = "SELECT id FROM currency WHERE currency_name = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, currencyName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                } else {
                    throw new SQLException("Currency not found: " + currencyName);
                }
            }
        }
    }


    // Method to get total amount per city
    public void getTotalAmountPerCity() {
        String query = "SELECT c.city_name, SUM(t.amount) AS total_amount " +
                       "FROM \"transaction\" t " +
                       "JOIN city c ON t.city_id = c.id " +
                       "GROUP BY c.city_name";
        
        StringBuilder htmlContent = new StringBuilder("<h2>Total Amount Per City</h2><table border='1'><tr><th>City</th><th>Total Amount</th></tr>");
        
        try (Connection connection = new SQLiteConnector(getDatabasePath()).getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                String cityName = rs.getString("city_name");
                double totalAmount = rs.getDouble("total_amount");
                htmlContent.append("<tr><td>").append(cityName).append("</td><td>").append(totalAmount).append("</td></tr>");
            }

        } catch (SQLException e) {
            System.err.println("SQL Exception in getTotalAmountPerCity: " + e.getMessage());
            e.printStackTrace();
        }
        
        htmlContent.append("</table>");
        HTMLReportGenerator.appendToHTML(htmlContent.toString());
    }


    // Method to get total amount per currency
    public void getTotalAmountPerCurrency() {
        String query = "SELECT cur.currency_name, SUM(t.amount) AS total_amount " +
                       "FROM \"transaction\" t " +
                       "JOIN currency cur ON t.currency_id = cur.id " +
                       "GROUP BY cur.currency_name";
        
        StringBuilder htmlContent = new StringBuilder("<h2>Total Amount Per Currency</h2><table border='1'><tr><th>Currency</th><th>Total Amount</th></tr>");
        
        try (Connection connection = new SQLiteConnector(getDatabasePath()).getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                String currencyName = rs.getString("currency_name");
                double totalAmount = rs.getDouble("total_amount");
                htmlContent.append("<tr><td>").append(currencyName).append("</td><td>").append(totalAmount).append("</td></tr>");
            }

        } catch (SQLException e) {
            System.err.println("SQL Exception in getTotalAmountPerCurrency: " + e.getMessage());
            e.printStackTrace();
        }
        
        htmlContent.append("</table>");
        HTMLReportGenerator.appendToHTML(htmlContent.toString());
    }


    // Method to get the number of cities per country
    public void getNumberOfCitiesPerCountry() {
        String query = "SELECT co.country_name, COUNT(ci.id) AS city_count " +
                       "FROM city ci " +
                       "JOIN country co ON ci.country_id = co.id " +
                       "GROUP BY co.country_name";
        
        StringBuilder htmlContent = new StringBuilder("<h2>Number of Cities Per Country</h2><table border='1'><tr><th>Country</th><th>Number of Cities</th></tr>");
        
        try (Connection connection = new SQLiteConnector(getDatabasePath()).getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                String countryName = rs.getString("country_name");
                int cityCount = rs.getInt("city_count");
                htmlContent.append("<tr><td>").append(countryName).append("</td><td>").append(cityCount).append("</td></tr>");
            }

        } catch (SQLException e) {
            System.err.println("SQL Exception in getNumberOfCitiesPerCountry: " + e.getMessage());
            e.printStackTrace();
        }
        
        htmlContent.append("</table>");
        HTMLReportGenerator.appendToHTML(htmlContent.toString());
    }

  

    // Method to get the average transaction amount per currency
    public void getAverageTransactionAmountPerCurrency() {
        String query = "SELECT cur.currency_name, AVG(t.amount) AS average_amount " +
                       "FROM \"transaction\" t " +
                       "JOIN currency cur ON t.currency_id = cur.id " +
                       "GROUP BY cur.currency_name";
        
        StringBuilder htmlContent = new StringBuilder("<h2>Average Transaction Amount Per Currency</h2><table border='1'><tr><th>Currency</th><th>Average Amount</th></tr>");
        
        try (Connection connection = new SQLiteConnector(getDatabasePath()).getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                String currencyName = rs.getString("currency_name");
                double averageAmount = rs.getDouble("average_amount");
                htmlContent.append("<tr><td>").append(currencyName).append("</td><td>").append(averageAmount).append("</td></tr>");
            }

        } catch (SQLException e) {
            System.err.println("SQL Exception in getAverageTransactionAmountPerCurrency: " + e.getMessage());
            e.printStackTrace();
        }
        
        htmlContent.append("</table>");
        HTMLReportGenerator.appendToHTML(htmlContent.toString());
    }
    
    public void generateTransactionReport() {
        String query = "SELECT t.id AS transaction_id, t.amount AS transaction_amount, " +
                       "c.city_name, cur.currency_name, co.country_name " +
                       "FROM \"transaction\" t " +
                       "JOIN city c ON t.city_id = c.id " +
                       "JOIN currency cur ON t.currency_id = cur.id " +
                       "JOIN country co ON c.country_id = co.id " +
                       "ORDER BY t.id DESC";
        
        // Get the current date
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
        String formattedDate = today.format(formatter);
        
        // Start building the HTML content
        StringBuilder htmlContent = new StringBuilder("<html><head><title>Transaction Report</title>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; margin: 20px; }" +
                "table { width: 100%; border-collapse: collapse; }" +
                "th, td { padding: 10px; text-align: left; border: 1px solid #ddd; }" +
                "th { background-color: #f2f2f2; }" +
                "tr:nth-child(even) { background-color: #f9f9f9; }" +
                "h2 { text-align: center; }" +
                "img { display: block; margin: 0 auto; }" +
                "</style>" +
                "</head><body>"  +
                "<h2>Transaction Report</h2>" +
                         "<table>" +
                "<tr><th>Transaction ID</th><th>Amount</th><th>City</th><th>Currency</th><th>Country</th></tr>");
        
        try (Connection connection = new SQLiteConnector(getDatabasePath()).getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                int transactionId = rs.getInt("transaction_id");
                double transactionAmount = rs.getDouble("transaction_amount");
                String cityName = rs.getString("city_name");
                String currencyName = rs.getString("currency_name");
                String countryName = rs.getString("country_name");
                
                htmlContent.append("<tr>")
                           .append("<td>").append(transactionId).append("</td>")
                           .append("<td>").append(transactionAmount).append("</td>")
                           .append("<td>").append(cityName).append("</td>")
                           .append("<td>").append(currencyName).append("</td>")
                           .append("<td>").append(countryName).append("</td>")
                           .append("</tr>");
            }

        } catch (SQLException e) {
            System.err.println("SQL Exception in generateTransactionReport: " + e.getMessage());
            e.printStackTrace();
        }
        
        htmlContent.append("</table></body></html>");
        HTMLReportGenerator.appendToHTML(htmlContent.toString());
    }



    public String getDatabasePath() {
        return databasePath;
    }

    public void setDatabasePath(String databasePath) {
        this.databasePath = databasePath;
    }
    
    


}
