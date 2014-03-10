import datasets.UserDataSet;
import org.junit.Before;
import org.junit.Test;
import org.junit.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.*;

public class PageGeneratorTest {
    @Test
    public void testGetPage() throws Exception {
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("refreshPeriod", "1000");
        pageVariables.put("serverTime", "15.57.11");
        pageVariables.put("login", "test");

        String page = PageGenerator.getPage(FrontendServlet.Templates.TIMER, pageVariables);

        for (Object o : pageVariables.entrySet()) {
            Map.Entry pair = (Map.Entry) o;
            String value = (String) pair.getValue();

            assertTrue(page.contains(value));
        }
    }
}
