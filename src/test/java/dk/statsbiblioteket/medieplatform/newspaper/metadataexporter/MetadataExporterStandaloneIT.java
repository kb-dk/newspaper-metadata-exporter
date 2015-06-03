package dk.statsbiblioteket.medieplatform.newspaper.metadataexporter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class MetadataExporterStandaloneIT {
    private final static String TEST_BATCH_ID = "400022028241";
    private File genericPropertyFile;
    private Properties properties;
    private Logger log = LoggerFactory.getLogger(getClass());
    @BeforeMethod(groups = "testDataTest")
    public void loadGeneralConfiguration() throws Exception {
        String pathToProperties = System.getProperty("integration.test.newspaper.properties");
        properties = new Properties();

        log.info("Loading general config from: " + pathToProperties);
        genericPropertyFile = new File(pathToProperties);
        properties.load(new FileInputStream(genericPropertyFile));
        String path = genericPropertyFile.getParentFile() + "/newspaper-metadata-exporter-config/config.properties";
        log.info("Loading specific config from: " + path);
        File specificProperties = new File(path);
        properties.load(new FileInputStream(specificProperties));
        properties.setProperty(MetadataExporter.METADATAEXPORTER_LOCATION_PROPERTY,
                               "target/metadataexporter/Integration");
        properties.setProperty("metadataexporter.transform",
                               "true");
        new File("target/metadataexporter").mkdirs();
        properties.store(new FileOutputStream("target/metadataexporter/it.properties"), null);
    }

    /**
     * Test that a reasonable batch can be run against the flagger component without generating any
     * errors or flags when the batch and configuration agree on the setup..
     * @throws Exception
     */
    @Test(groups = "testDataTest")
    public void testSmallBatch() throws Exception {
        processBatch();
    }

    /**
     * Test that a the default batch with a configuration inconsistent with the metadata in the batch. This should
     * generate a lot of flags.
     * @throws Exception
     */
    @Test(groups = "testDataTest")
    public void testBadBatch() throws Exception {
        processBatch();
    }

    private void processBatch()  throws Exception  {
        MetadataExporterStandalone.main(
                new String[]{"-c", "target/metadataexporter/it.properties", "-b", TEST_BATCH_ID, "-n", "1", "-p", "false"});
    }
}
