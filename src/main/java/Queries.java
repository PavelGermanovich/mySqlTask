import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public enum Queries {

    testsNumbersForEachProejct("src/main/resources/sqlRequests/testsCountOnEachProjects"),
    testsPerformedOnChromeAndFirefox("src/main/resources/sqlRequests/testsNumbersPerformedOnBrowsers"),
    testsPeformedLaterThan5November("src/main/resources/sqlRequests/testsThatWerePerformedLaterThan%date%"),
    testsWithMinTime("src/main/resources/sqlRequests/testsWithMinTime");

    private String query;

    Queries(String filePath) {
        try {
            query = new Scanner(new File(filePath)).useDelimiter("\\Z").next();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String getQueryString(){
        return query;
    }
}
