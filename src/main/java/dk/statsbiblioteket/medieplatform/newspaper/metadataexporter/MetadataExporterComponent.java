package dk.statsbiblioteket.medieplatform.newspaper.metadataexporter;

import java.util.Properties;

import dk.statsbiblioteket.medieplatform.autonomous.CallResult;
import dk.statsbiblioteket.medieplatform.autonomous.RunnableComponent;
import dk.statsbiblioteket.medieplatform.autonomous.NewspaperBatchAutonomousComponentUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MetadataExporterComponent {

    private static Logger log = LoggerFactory.getLogger(MetadataExporterComponent.class);

    /**
     * The class must have a main method, so it can be started as a command line tool
     *
     * @param args the arguments.
     *
     * @throws Exception
     * @see dk.statsbiblioteket.medieplatform.autonomous.AutonomousComponentUtils#parseArgs(String[])
     */
    public static void main(String[] args) throws Exception {
        log.info("Starting with args {}", args);

        //Parse the args to a properties construct
        Properties properties = NewspaperBatchAutonomousComponentUtils.parseArgs(args);

        //make a new runnable component from the properties
        RunnableComponent component = new MetadataExporterRunnableComponent(properties);

        CallResult result = NewspaperBatchAutonomousComponentUtils.startAutonomousComponent(properties, component);
        log.info(result.toString());
        System.exit(result.containsFailures());
    }
}
