package resourcing;

import resourcing.resources.Resource;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

public class XmlParser {
	public static Resource parse(String xmlFile) throws ParserException {
	    try {	 
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
		 
			SaxHandler handler = new SaxHandler();

            InputStream is = new ByteArrayInputStream(xmlFile.getBytes("UTF-8"));
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
			saxParser.parse(is, handler);
	        br.close();

	        return handler.getResource();
	    }
        catch (Exception e) {
            throw new ParserException(e);
        }
    }
}
