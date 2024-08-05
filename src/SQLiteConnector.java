import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteConnector {
    private Connection connection;

    public SQLiteConnector(String databasePath) throws SQLException {
        String url = "jdbc:sqlite:" + databasePath;
        connection = DriverManager.getConnection(url);
        initializeDatabase();
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    // Method to initialize the database schema
    private void initializeDatabase() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            // Create country table
            String createCountryTable = "CREATE TABLE IF NOT EXISTS country ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "country_name TEXT UNIQUE NOT NULL)";
            stmt.execute(createCountryTable);

            // Create city table
            String createCityTable = "CREATE TABLE IF NOT EXISTS city ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "city_name TEXT UNIQUE NOT NULL, "
                    + "country_id INTEGER, "
                    + "FOREIGN KEY (country_id) REFERENCES country(id))";
            stmt.execute(createCityTable);

            // Create currency table
            String createCurrencyTable = "CREATE TABLE IF NOT EXISTS currency ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "currency_name TEXT UNIQUE NOT NULL)";
            stmt.execute(createCurrencyTable);

         // Create transaction table with quotes to escape the reserved keyword
            String createTransactionTable = "CREATE TABLE IF NOT EXISTS \"transaction\" ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "city_id INTEGER, "
                    + "currency_id INTEGER, "
                    + "amount REAL, "
                    + "FOREIGN KEY (city_id) REFERENCES city(id), "
                    + "FOREIGN KEY (currency_id) REFERENCES currency(id))";
            stmt.execute(createTransactionTable);

        }
    }
}
