package resourcing;

import frontend.Locations;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class SaxParserTest {
    @Test
    public void testParse() throws Exception {
        String xml =
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<class name=\"frontend.Locations\">\n" +
                "    <INDEX>/index</INDEX>\n" +
                "    <LOGIN>/login</LOGIN>\n" +
                "    <REGISTER>/register</REGISTER>\n" +
                "    <POLL>/poll</POLL>\n" +
                "    <TIMER>/timer</TIMER>\n" +
                "    <REGISTRATION>/registration</REGISTRATION>\n" +
                "</class>";

        Locations locations = (Locations) SaxParser.parse(xml);
        assertTrue(Locations.INDEX.equals("/index"));
    }
}