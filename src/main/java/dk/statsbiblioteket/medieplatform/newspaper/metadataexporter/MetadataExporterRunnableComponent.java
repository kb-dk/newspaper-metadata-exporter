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

    private Logger log = LoggerFactory.getLogger(getClass());
    private Properties properties;

    protected MetadataExporterRunnableComponent(Properties properties) {
        super(properties);
        this.properties = properties;
    }

    @Override
    public String getEventID() {
        return "Metadata_Exported";
    }

    @Override
    public void doWorkOnItem(Batch batch, ResultCollector resultCollector) throws Exception {
        log.info("Starting metadata export for '{}'", batch.getFullID());
        List<TreeEventHandler> metadataExporter = Arrays.asList(new TreeEventHandler[]
                { new MetadataExporter(batch, properties) });
        EventRunner eventRunner = new EventRunner(createIterator(batch), metadataExporter, resultCollector);
        eventRunner.run();
        log.info("Done exporting metadata for '{}', success: {}", batch.getFullID(), resultCollector.isSuccess());
    }
}
