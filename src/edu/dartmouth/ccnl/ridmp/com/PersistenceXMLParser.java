package edu.dartmouth.ccnl.ridmp.com;

import org.apache.commons.digester.Digester;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.Properties;

/**
 * Date: May 14, 2005
 *
 * @author Amir  Sharifzadeh
 */
public class PersistenceXMLParser {
    private static final Log log = LogFactory.getLog(PersistenceXMLParser.class);

    private Collection configFiles = new HashSet();
    private Properties properties = new Properties();

    public Collection getConfigFiles() {
        return configFiles;
    }

    public Properties getProperties() {
        return properties;
    }

    private static final Class[] ADD_CONFIG_FILE_ARGUMENTS = {String.class};

    public void addConfigFile(String item) {
        log.debug("adding config file to persitence config files: " + item);
        configFiles.add(item);
    }

    private static final Class[] SET_SYSTEM_PROPERTY_ARGUMENTS = {String.class, String.class};

    public void addSystemProperty(String key, String value) {
        log.debug("setting system property (" + key + ", " + value + ")");
        properties.setProperty(key, value);
    }

    /**
     * Parses an {@link java.io.InputStream}s containing configuration XML, and fills {@link #properties} and {@link #configFiles}.
     */
    public void parse(InputStream xmlStream) {
        Digester digester;
        try {
            digester = prepareDigester();
            digester.push(this);
            digester.parse(xmlStream);
            log.debug("persitence xml parsed");
        } catch (IOException e) {
            log.error(e, e);
            throw new RuntimeException(e);
        } catch (SAXException e) {
            log.error(e, e);
            throw new RuntimeException(e);
        }
    }


    private Digester prepareDigester() {
        Digester digester = new Digester();
        digester.addCallMethod("session-factory/configuration-property", "addSystemProperty", 2, SET_SYSTEM_PROPERTY_ARGUMENTS);
        digester.addCallParam("session-factory/configuration-property/property-name", 0);
        digester.addCallParam("session-factory/configuration-property/property-value", 1);
        digester.addCallMethod("session-factory/configuration-file", "addConfigFile", 0, ADD_CONFIG_FILE_ARGUMENTS);
        return digester;
    }

    public static void main(String[] args) throws Exception {
        PersistenceXMLParser parser = new PersistenceXMLParser();

        parser.parse(PersistenceXMLParser.class.getResourceAsStream("/ridmp/com/sessionFactory.cfg.xml"));
    }
}