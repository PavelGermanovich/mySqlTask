import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DbUtils {
    private static Logger logger = LoggerUtil.getInstance();
    private static String formatLongFields = "%-140.130s";
    private static String formatShortFields = "%-20.15s";
    private static String propertiesDbPath = "src/main/resources/db.properties";

    private static Properties dbProperties;

    static {
        try {
            dbProperties = new Properties();
            dbProperties.load(new FileInputStream(propertiesDbPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String userName = dbProperties.getProperty("db.login");
    private static String password = dbProperties.getProperty("db.password");
    private static String dbConnection = dbProperties.getProperty("db.connection");
    private static String dbDriverName = dbProperties.getProperty("db.driverName");

    public static ResultSet executeQuery(String queries, Connection connection) {
        Statement statement = null;
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        ResultSet rs = null;
        try {
            rs = statement.executeQuery(queries);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }

        return rs;
    }

    public static void logQueryResults(ResultSet rs, String requestInfo) {
        ResultSetMetaData resultSetMetaData = null;
        int columnCount = 0;
        try {
            resultSetMetaData = rs.getMetaData();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        try {
            columnCount = resultSetMetaData.getColumnCount();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }

        StringBuilder columnNames = new StringBuilder();
        for (int i=1; i <= columnCount; i++) {
            try {
                if(columnCount == 3 && i == 2){
                    columnNames.append(String.format(formatLongFields, resultSetMetaData.getColumnLabel(i)) + "|");
                }
                else
                columnNames.append(String.format(formatShortFields, resultSetMetaData.getColumnLabel(i)) + "|");
            } catch (SQLException e) {
                logger.log(Level.SEVERE, e.getMessage());
            }
        }

        StringBuilder row = new StringBuilder();
        try {
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    try {
                        if(columnCount ==3 && i==2) {
                            row.append(String.format(formatLongFields, rs.getString(i)) + "|");
                        }
                        else
                        row.append(String.format(formatShortFields, rs.getString(i)) + "|");
                    } catch (SQLException e) {
                        logger.log(Level.SEVERE, e.getMessage());
                    }
                }
                row.append("\n");
            }
            logger.log(Level.INFO, requestInfo + "\n" +
                    columnNames.toString() + "\n" + row.toString());
        }
        catch (Exception e){
            logger.log(Level.SEVERE, e.getMessage());
        }
    }

    public static Connection setupConnection() {
        try {
            Class.forName(dbDriverName);
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(dbConnection, userName, password);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        return connection;
    }

    public static void executeAndLogQueryResults(String query, Connection con, String requestInfo) {
        ResultSet rs = executeQuery(query, con);
        logQueryResults(rs, requestInfo);
    }
}
