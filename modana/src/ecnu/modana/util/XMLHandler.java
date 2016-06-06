package ecnu.modana.util;

import java.io.File;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.dom4j.io.SAXValidator;
import org.dom4j.util.XMLErrorHandler;
import org.xml.sax.SAXException;

/**
 * Access XML element using dom4j (validating by xsd)
 * @author cb
 */
public class XMLHandler {
	
	Logger logger = Logger.getRootLogger();
	
	/**
	 * xml file to be handled
	 */
	private File xmlFile;
	
	/**
	 * constructor
	 * @param xmlPath xml file path string
	 */
	public XMLHandler(String xmlPath) {
		super();
		this.xmlFile = new File(xmlPath);
	}

	/**
	 * validate xml by specified xsd file
	 * @param xsdFilePath
	 * @return true if the validation succeeds, otherwise false
	 */
	public boolean validateByXSD(String xsdFilePath) {
            //create xml default error handler
            XMLErrorHandler errorHandler = new XMLErrorHandler(); 
            //get SAX parser factory
            SAXParserFactory factory = SAXParserFactory.newInstance(); 
            factory.setValidating(true); 
            factory.setNamespaceAware(true);
			try {
				//get a SAX parser with above setting
				SAXParser parser = factory.newSAXParser();
	            //create a SAX reader
	            SAXReader xmlReader = new SAXReader(); 
	            //get xml document
	            Document xmlDocument = (Document) xmlReader.read(xmlFile); 
	            //set xml reader
	            parser.setProperty(
	                    "http://java.sun.com/xml/jaxp/properties/schemaLanguage", 
	                    "http://www.w3.org/2001/XMLSchema"); 
	            parser.setProperty( 
	                    "http://java.sun.com/xml/jaxp/properties/schemaSource", 
	                    "file:" + xsdFilePath); 
	            //create and set a SAX validator
	            SAXValidator validator = new SAXValidator(parser.getXMLReader()); 
	            //error handler is used for us to get error information
	            validator.setErrorHandler(errorHandler); 
	            validator.validate(xmlDocument); //validate
	            if (errorHandler.getErrors().hasContent()) { //error information is not empty
	                logger.error("Xml file (" + xmlFile + ") validation failed! (xsd file: " + 
	                		xsdFilePath + "), Error info: " + errorHandler.getErrors().getStringValue());
	            } else { //no error
	                return true; // return directly
	            }
			} catch (ParserConfigurationException e) {
				logger.error("SAX parser config error!", e);
			} catch (SAXException e) {
				logger.error("Get xml reader error!", e);
			} catch (DocumentException e) {
				logger.error("Read document error!", e);
			} catch (Exception e) {
				logger.error("unknown error!", e);
			}
			return false;
	}
	
}
