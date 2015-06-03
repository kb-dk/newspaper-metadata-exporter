package dk.statsbiblioteket.medieplatform.newspaper.metadataexporter;

import org.testng.FileAssert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.NodeBeginsParsingEvent;

import java.io.File;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.util.Properties;

import static org.testng.AssertJUnit.*;

/**
 * Tests for the transforming metadata exporter.
 */
public class TransformingMetadataExporterTest {

    private TransformingMetadataExporter transformingMetadataExporter;
    private File temporaryDirectory;

    @BeforeMethod
    public void setProperties(Method method) throws Exception {
        temporaryDirectory = new File("target", method.getName());
        Properties properties = new Properties();
        properties.setProperty("metadataexporter.cutoffdate", "1875-01-01");
        properties.setProperty("metadataexporter.presentationcopydir", "/avis-show");
        properties.setProperty("metadataexporter.outputdir", temporaryDirectory.getAbsolutePath());
        transformingMetadataExporter = new TransformingMetadataExporter(properties);
    }

    @Test
    public void testIncludedInExportPageBefore() throws Exception {
        assertTrue(transformingMetadataExporter.includedInExport(
                "B400022028241-RT1/400022028241-1/1795-06-15-01/adresseavisen1759-1795-06-15-01-0003A.mods.xml"));
    }

    @Test
    public void testIncludedInExportPageAfter() throws Exception {
        assertFalse(transformingMetadataExporter.includedInExport(
                "B400022028241-RT1/400022028241-1/1995-06-15-01/adresseavisen1759-1995-06-15-01-0003A.mods.xml"));
    }

    @Test
    public void testIncludedInExportBrikBefore() throws Exception {
        assertTrue(transformingMetadataExporter.includedInExport(
                "B400022028241-RT1/400022028241-1/1795-06-15-01/adresseavisen1759-1795-06-15-01-0003-brik.mix.xml"));

    }

    @Test
    public void testIncludedInExportBrikAfter() throws Exception {
        assertFalse(transformingMetadataExporter.includedInExport(
                "B400022028241-RT1/400022028241-1/1995-06-15-01/adresseavisen1759-1995-06-15-01-0003-brik.mix.xml"));

    }

    @Test
    public void testIncludedInExportUnmatched() throws Exception {
        assertFalse(transformingMetadataExporter.includedInExport(
                "B400022028241-RT1/400022028241-1/UNMATCHED/adresseavisen1759-400022028241-1-0008.mix.xml"));

    }

    @Test
    public void testIncludedInExportFilm() throws Exception {
        assertTrue(transformingMetadataExporter.includedInExport(
                "B400022028241-RT1/400022028241-1/adresseavisen1759-400022028241-1.film.xml"));

    }

    @Test
    public void testIncludedInExportIsoTarget() throws Exception {
        assertTrue(transformingMetadataExporter.includedInExport(
                "B400022028241-RT1/400022028241-1/FILM-ISO-target/adresseavisen1759-400022028241-1-ISO-0001.mix.xml"));

    }

    @Test
    public void testIncludedInExportWorkshiftTarget() throws Exception {
        assertTrue(transformingMetadataExporter
                           .includedInExport("B400022028241-RT1/WORKSHIFT-ISO-TARGET/Target-000001-0001.mix.xml"));

    }

    @Test
    public void testTransformNamePage() throws Exception {
        assertEquals("adresseavisen1759/1795-06-15/01/adresseavisen1759-1795-06-15-01-0003A.mods.xml",
                     transformingMetadataExporter.transformName(
                             "B400022028241-RT1/400022028241-1/1795-06-15-01/adresseavisen1759-1795-06-15-01-0003A.mods.xml"));
    }

    @Test
    public void testTransformNameBrik() throws Exception {
        assertEquals("adresseavisen1759/1795-06-15/01/adresseavisen1759-1795-06-15-01-0003-brik.mix.xml",
                     transformingMetadataExporter.transformName(
                             "B400022028241-RT1/400022028241-1/1795-06-15-01/adresseavisen1759-1795-06-15-01-0003-brik.mix.xml"));
    }

    @Test
    public void testTransformNameUnmatched() throws Exception {
        assertEquals("adresseavisen1759/Unmatched/adresseavisen1759-400022028241-1-0008.mix.xml",
                     transformingMetadataExporter.transformName(
                             "B400022028241-RT1/400022028241-1/UNMATCHED/adresseavisen1759-400022028241-1-0008.mix.xml"));
    }

    @Test
    public void testTransformNameFilm() throws Exception {
        assertEquals("adresseavisen1759/Film/adresseavisen1759-400022028241-1.film.xml",
                     transformingMetadataExporter.transformName(
                             "B400022028241-RT1/400022028241-1/adresseavisen1759-400022028241-1.film.xml"));
    }

    @Test
    public void testTransformNameIsoTarget() throws Exception {
        assertEquals("adresseavisen1759/Film/adresseavisen1759-400022028241-1-ISO-0001.mix.xml",
                     transformingMetadataExporter.transformName(
                             "B400022028241-RT1/400022028241-1/FILM-ISO-target/adresseavisen1759-400022028241-1-ISO-0001.mix.xml"));
    }

    @Test
    public void testTransformNameWorkshiftTarget() throws Exception {
        assertEquals("Target/Target-000001-0001.mix.xml",
                     transformingMetadataExporter.transformName(
                             "B400022028241-RT1/WORKSHIFT-ISO-TARGET/Target-000001-0001.mix.xml"));
    }

    @Test
    public void testNodeBegin() throws Exception {
        String name = "B400022028241-RT1/400022028241-1/1795-06-15-01/adresseavisen1759-1795-06-15-01-0003A.jp2";
        NodeBeginsParsingEvent event = new NodeBeginsParsingEvent(name, null);
        transformingMetadataExporter.handleNodeBegin(event);
        assertTrue(Files.exists(Paths.get(temporaryDirectory.getPath(),
                               "adresseavisen1759/1795-06-15/01/adresseavisen1759-1795-06-15-01-0003A.jp2"),
                     LinkOption.NOFOLLOW_LINKS));
    }
}