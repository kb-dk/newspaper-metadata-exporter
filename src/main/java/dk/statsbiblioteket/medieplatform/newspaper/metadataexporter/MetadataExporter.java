package dk.statsbiblioteket.medieplatform.newspaper.metadataexporter;

import dk.statsbiblioteket.medieplatform.autonomous.Batch;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.AttributeParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.NodeBeginsParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.NodeEndParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.eventhandlers.DefaultTreeEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * Processes tree saving all metadata files.
 */
public class MetadataExporter extends DefaultTreeEventHandler {
    private Logger log = LoggerFactory.getLogger(getClass());

    public static String METADATAEXPORTER_LOCATION_PROPERTY = "metadataexporter.outputdir";

    private final String metadataexportdir;

    /**
     * @param batch Used for naming the output file.
     * @param properties Defines specifics for metadata export.
     */
    public MetadataExporter(Batch batch, Properties properties) {
        super();
        metadataexportdir = properties.getProperty(METADATAEXPORTER_LOCATION_PROPERTY, "target/metadataexport/")
                + batch.getFullID() + "/";
    }

    @Override
    public void handleNodeBegin(NodeBeginsParsingEvent event) {
    }

    @Override
    public void handleNodeEnd(NodeEndParsingEvent event) {
    }

    @Override
    public void handleAttribute(AttributeParsingEvent event) {
        //TODO: Write metadata
    }

    @Override
    public void handleFinish() {
        super.handleFinish();
    }
}
