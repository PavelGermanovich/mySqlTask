import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class testMysqlDb {
    private static Logger logger = LoggerUtil.getInstance();
    private static String requestInfoPattern = "Request %d: %s";
    private static String browserFirefox ="firefox";
    private static String browserChrome ="chrome";
    private static String date="2015-11-07";
    private static Connection connectionToMysql= null;

    @Before
    public void setupConnection() {
        connectionToMysql = DbUtils.setupConnection();
    }

    @Test
    public void logRequestResults() {
        String requestInfoFirst = String.format(requestInfoPattern, 1,
                "for each test min time should be displayed , results are sorted by project and tests");
        DbUtils.executeAndLogQueryResults(Queries.testsWithMinTime.getQueryString(), connectionToMysql, requestInfoFirst);

        String requestInfoSecond = String.format(requestInfoPattern, 2,
                "all projects with unique numbers of tests in them should be displayed");
        DbUtils.executeAndLogQueryResults(Queries.testsNumbersForEachProejct.getQueryString(), connectionToMysql, requestInfoSecond);

        String requestInfoThird = String.format(requestInfoPattern, 3,
                "Display the tests for each projects which were performed later than 5 November 2015");
        String queryForDateLateThan5November = String.format(Queries.testsPeformedLaterThan5November.getQueryString(), date);
        DbUtils.executeAndLogQueryResults(queryForDateLateThan5November, connectionToMysql, requestInfoThird);

        String requestInfoFourth = String.format(requestInfoPattern, 4, "Display numbers of tests that were performed on Chrome and Firefox");
        String queryTestsPeformedOnChromeAndFirefox = String.format(Queries.testsPerformedOnChromeAndFirefox.getQueryString(),
                browserChrome, browserFirefox);
        DbUtils.executeAndLogQueryResults(queryTestsPeformedOnChromeAndFirefox, connectionToMysql, requestInfoFourth);
    }

    @After
    public void closeConnection() {
        try {
            connectionToMysql.close();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }
}
