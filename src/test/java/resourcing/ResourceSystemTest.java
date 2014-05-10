package resourcing;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertTrue;

public class ResourceSystemTest {
    @Test
    public void testGetResource() throws Exception {
        ResourceSystem rs = ResourceSystem.getInstance();
        assertTrue(rs.getResource("Locations") != null);
        assertTrue(rs.getResource("Templates") != null);
    }

    @Test
    public void testGetConfig() throws Exception {
        ResourceSystem rs = ResourceSystem.getInstance();
        Map<String, String> serverConfig = rs.getConfig("server");
        assertTrue(Integer.parseInt(serverConfig.get("port")) > 0);
    }
}