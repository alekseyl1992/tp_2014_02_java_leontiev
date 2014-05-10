package resourcing;

import org.junit.Test;
import java.util.Map;
import static org.junit.Assert.assertTrue;

public class DOMParserTest {
    @Test
    public void testParse() throws Exception {
        String xml =
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<config name=\"server\">\n" +
                "    <property name=\"port\">8081</property>\n" +
                "</config>";

        Map<String, String> map = DOMParser.parse(xml);
        assertTrue(map.get("port").equals("8081"));
    }
}