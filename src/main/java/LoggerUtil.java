import java.io.IOException;
import java.util.logging.*;

public class LoggerUtil {
    private static Logger instance = null;
    private static String fileLogName = "log.txt";
    private LoggerUtil (){
    }

    static private class MyFormatter extends Formatter{
        @Override
        public String format(LogRecord record) {
            StringBuilder sb = new StringBuilder();
            sb.append(record.getMessage()).append('\n');
            return sb.toString();
        }
    }

    public static Logger getInstance() {
        if(instance == null) {
            instance = Logger.getLogger(LoggerUtil.class.getName());
            Handler handlerConsole = new ConsoleHandler();
            FileHandler handlerFile = null;
            try {
                handlerFile = new FileHandler(fileLogName, false);
            } catch (IOException e) {
                e.printStackTrace();
            }
            handlerFile.setLevel(Level.ALL);
            handlerFile.setFormatter(new MyFormatter());
            instance.addHandler(handlerFile);
        }
        return instance;
    }
}
