package com.shunyi.laboratory.xml.sax;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.sax.SAXSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author esehhuc
 * @create 2021-03-16 14:47
 */
public class JavaXmlSchemaValidationEx {

    public static void main(String[] args) {
        File xsdFile = new File("src/main/resources/users.xsd");
        try {
            Path xmlPath = Paths.get("src/main/resources/users.xml");
            Reader reader = Files.newBufferedReader(xmlPath);

            String schemaLang = XMLConstants.W3C_XML_SCHEMA_NS_URI;
            SchemaFactory factory = SchemaFactory.newInstance(schemaLang);
            Schema schema = factory.newSchema(xsdFile);

            Validator validator = schema.newValidator();

            SAXSource source = new SAXSource(new InputSource(reader));
            validator.validate(source);

            System.out.println("The document was validated OK");

        } catch (SAXException ex) {

            Logger lgr = Logger.getLogger(JavaXmlSchemaValidationEx.class.getName());
            lgr.log(Level.SEVERE, "The document failed to validate");
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        } catch (IOException ex) {

            Logger lgr = Logger.getLogger(JavaXmlSchemaValidationEx.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
}