package dk.statsbiblioteket.medieplatform.newspaper.metadataexporter;

import dk.statsbiblioteket.medieplatform.autonomous.Batch;
import dk.statsbiblioteket.medieplatform.autonomous.ResultCollector;
import dk.statsbiblioteket.medieplatform.autonomous.TreeProcessorAbstractRunnableComponent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.eventhandlers.EventRunner;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.eventhandlers.TreeEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class MetadataExporterRunnableComponent extends TreeProcessorAbstractRunnableComponent {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final MetadataExporter metadataExporter;
    private String eventID;

    protected MetadataExporterRunnableComponent(Properties properties) {
        super(properties);
        if (Boolean.parseBoolean(properties.getProperty("metadataexporter.transform", "false"))) {
            metadataExporter = new TransformingMetadataExporter(properties);
        } else {
            metadataExporter = new MetadataExporter(properties);
        }
        eventID = properties.getProperty("metadataexporter.eventID", "Metadata_Exported");
    }

    @Override
    public String getEventID() {
        return eventID;
    }

    @Override
    public void doWorkOnItem(Batch batch, ResultCollector resultCollector) throws Exception {
        log.info("Starting metadata export for '{}'", batch.getFullID());
        List<TreeEventHandler> metadataExporter = Arrays.asList(new TreeEventHandler[]
                {this.metadataExporter});
        EventRunner eventRunner = new EventRunner(createIterator(batch), metadataExporter, resultCollector);
        eventRunner.run();
        log.info("Done exporting metadata for '{}', success: {}", batch.getFullID(), resultCollector.isSuccess());
    }
}
