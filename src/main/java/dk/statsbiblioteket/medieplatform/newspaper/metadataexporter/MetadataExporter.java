package dk.statsbiblioteket.medieplatform.newspaper.metadataexporter;

import dk.statsbiblioteket.medieplatform.autonomous.Batch;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.AttributeParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.NodeBeginsParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.NodeEndParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.eventhandlers.DefaultTreeEventHandler;
import dk.statsbiblioteket.util.Files;

import org.apache.commons.fileupload.util.Streams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 * Processes tree saving all metadata files.
 */
public class MetadataExporter extends DefaultTreeEventHandler {
    private Logger log = LoggerFactory.getLogger(getClass());

    public static String METADATAEXPORTER_LOCATION_PROPERTY = "metadataexporter.outputdir";

    private final String metadataexportdir;

    /**
     * @param properties Defines specifics for metadata export.
     */
    public MetadataExporter(Properties properties) {
        super();
        metadataexportdir = properties.getProperty(METADATAEXPORTER_LOCATION_PROPERTY, "target/metadataexport/");
    }

    @Override
    public void handleNodeBegin(NodeBeginsParsingEvent event) {
    }

    @Override
    public void handleNodeEnd(NodeEndParsingEvent event) {
    }

    @Override
    public void handleAttribute(AttributeParsingEvent event) {
        String location = event.getLocation();
        if (location != null) {
            File file = new File(metadataexportdir, location);
            File md5file = new File(metadataexportdir, location + ".md5");
            file.getParentFile().mkdirs();
            try {
                Files.saveString(Streams.asString(event.getData()), file);
                Files.saveString(event.getChecksum(), md5file);
            } catch (IOException e) {
                throw new RuntimeException("Unable to write file '" + file + "'", e);
            }
        }

    }

    @Override
    public void handleFinish() {
        super.handleFinish();
    }
}
