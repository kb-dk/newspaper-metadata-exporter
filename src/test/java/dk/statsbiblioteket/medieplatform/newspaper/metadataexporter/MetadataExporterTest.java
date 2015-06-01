package dk.statsbiblioteket.medieplatform.newspaper.metadataexporter;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.AttributeParsingEvent;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

import static org.testng.FileAssert.*;

public class MetadataExporterTest {

    private File temporaryDirectory;

    @BeforeMethod
    public void setTemporaryDirectory(Method method) {
        temporaryDirectory = new File("target", method.getName());
        System.getProperties().setProperty("metadataexporter.outputdir", temporaryDirectory.getAbsolutePath());
    }

    /**
     * Test that an attribute event causes the metadata file and the md5 sum to be written.
     *
     * @throws Exception
     */
    @Test
    public void testFilesWritten() throws Exception {
        //Setup fixture
        MetadataExporter metadataExporter = new MetadataExporter(System.getProperties());
        String name = "B4000123456-RT1/4000123456-01/1970-01-01-01/minavis-1970-01-01.mods.xml";
        AttributeParsingEvent event = new AttributeParsingEvent(name, null) {
            @Override
            public InputStream getData() throws IOException {
                return new ByteArrayInputStream("<placeHolder/>".getBytes());
            }

            @Override
            public String getChecksum() throws IOException {
                return "305bb2d3b2607bcd34e7d8c6d154e7e9";
            }
        };

        //Call method
        metadataExporter.handleAttribute(event);

        //Check result
        File expectedFile = new File(temporaryDirectory, name);
        assertReadable(expectedFile);
        assertLength(expectedFile, 14);
        File expectedMd5File = new File(temporaryDirectory, name + ".md5");
        assertReadable(expectedMd5File);
        assertLength(expectedMd5File, 32);
    }


    /**
     * Test that an attribute event with no location does not fail.
     *
     * @throws Exception
     */
    @Test
    public void testNoLocation() throws Exception {
        //Setup fixture
        MetadataExporter metadataExporter = new MetadataExporter(System.getProperties());
        String location = null;
        AttributeParsingEvent event = new AttributeParsingEvent("MODS", location) {
            @Override
            public InputStream getData() throws IOException {
                return new ByteArrayInputStream("<placeHolder/>".getBytes());
            }

            @Override
            public String getChecksum() throws IOException {
                return "305bb2d3b2607bcd34e7d8c6d154e7e9";
            }
        };

        //Call method
        metadataExporter.handleAttribute(event);

        //Check result
        //No result to check
    }

}
