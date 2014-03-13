package server;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertTrue;

public class PageGeneratorTest {
    @Test
    public void testGetPageOk() throws Exception {
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

    @Test
    public void testGetPageFailed() {
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("refreshPeriod", "1000");
        pageVariables.put("serverTime", "15.57.11");
        pageVariables.put("login", "test");

        String page = PageGenerator.getPage("testGetPageFailed", pageVariables);

        assertTrue(page.isEmpty());
    }
}
