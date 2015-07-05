package dk.statsbiblioteket.medieplatform.newspaper.metadataexporter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.statsbiblioteket.doms.central.connectors.EnhancedFedoraImpl;
import dk.statsbiblioteket.doms.central.connectors.fedora.pidGenerator.PIDGeneratorException;
import dk.statsbiblioteket.sbutil.webservices.authentication.Credentials;
import dk.statsbiblioteket.medieplatform.autonomous.AutonomousComponentUtils;
import dk.statsbiblioteket.medieplatform.autonomous.AutonomousWorker;
import dk.statsbiblioteket.medieplatform.autonomous.Batch;
import dk.statsbiblioteket.medieplatform.autonomous.BatchItemFactory;
import dk.statsbiblioteket.medieplatform.autonomous.CallResult;
import dk.statsbiblioteket.medieplatform.autonomous.ConfigConstants;
import dk.statsbiblioteket.medieplatform.autonomous.DomsEventStorageFactory;
import dk.statsbiblioteket.medieplatform.autonomous.NewspaperBatchAutonomousComponentUtils;
import dk.statsbiblioteket.medieplatform.autonomous.NewspaperDomsEventStorage;
import dk.statsbiblioteket.medieplatform.autonomous.NewspaperDomsEventStorageFactory;
import dk.statsbiblioteket.medieplatform.autonomous.ResultCollector;
import dk.statsbiblioteket.medieplatform.autonomous.RunnableComponent;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.Properties;

public class MetadataExporterStandalone {
    private static Logger log = LoggerFactory.getLogger(MetadataExporterStandalone.class);

    /**
     * The class must have a main method, so it can be started as a command line tool
     *
     * @param args the arguments.
     *
     * @throws Exception
     * @see AutonomousComponentUtils#parseArgs(String[])
     */
    public static void main(String[] args) throws Exception {
        System.exit(doMain(args));
    }

    public static int doMain(String[] args) throws Exception {
        log.info("Starting with args {}", args);

        //Parse the args to a properties construct
        Properties properties = NewspaperBatchAutonomousComponentUtils.parseArgs(args);

        //Get batchid and roundtrip number from command line
        String batchid = parseBatchId(args);
        Integer roundtripno = parseRoundtripNumber(args);
        boolean preservable = parsePreservable(args);

        String maxResultsProperty = properties.getProperty(ConfigConstants.MAX_RESULTS_COLLECTED);
        Integer maxResults = null;
        if (maxResultsProperty != null) {
            maxResults = Integer.parseInt(maxResultsProperty);
        }

        //get a DOMS storage from properties
        NewspaperDomsEventStorageFactory domsEventStorageFactory = new NewspaperDomsEventStorageFactory();
        domsEventStorageFactory.setFedoraLocation(properties.getProperty(ConfigConstants.DOMS_URL));
        domsEventStorageFactory.setUsername(properties.getProperty(ConfigConstants.DOMS_USERNAME));
        domsEventStorageFactory.setPassword(properties.getProperty(ConfigConstants.DOMS_PASSWORD));
        domsEventStorageFactory.setItemFactory(new BatchItemFactory());
        NewspaperDomsEventStorage domsEventStorage = domsEventStorageFactory.createDomsEventStorage();

        //make a new runnable component from the properties
        MetadataExporterRunnableComponent exporter = new MetadataExporterRunnableComponent(properties);
        ResultCollector result = new ResultCollector(exporter.getComponentName(),
                                                              exporter.getComponentVersion(), maxResults);
        result.setPreservable(preservable);
        AutonomousWorker<Batch> worker = new AutonomousWorker<>(
                exporter, result,
                new Batch(batchid, roundtripno), domsEventStorage);

        //Run the component
        worker.run();

        //Return result
        log.info(result.toReport());
        if (!result.isSuccess()) {
            return 1;
        }
        return 0;
    }

    private static boolean parsePreservable(String[] args) {
        for (int i = 0; i < args.length - 1; i++) {
            if (args[i].equals("-p")) {
                return Boolean.parseBoolean(args[i+1]);
            }
        }
        return false;
    }

    private static String parseBatchId(String[] args) {
        for (int i = 0; i < args.length - 1; i++) {
            if (args[i].equals("-b")) {
                return args[i+1];
            }
        }
        usage();
        throw new IllegalArgumentException("No parameter given for batch");
    }

    private static Integer parseRoundtripNumber(String[] args) {
        for (int i = 0; i < args.length - 1; i++) {
            if (args[i].equals("-n")) {
                return Integer.parseInt(args[i+1]);
            }
        }
        usage();
        throw new IllegalArgumentException("No parameter given for roundtrip");
    }

    private static void usage() {
        System.err.println("Usage: java " + MetadataExporterStandalone.class.getName()
                                   + " -b <batchid> -n <roundtripno> [-p <preserveresult>] [-c <configfile>] ");
    }
}
