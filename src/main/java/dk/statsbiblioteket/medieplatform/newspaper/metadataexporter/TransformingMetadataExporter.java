package dk.statsbiblioteket.medieplatform.newspaper.metadataexporter;

import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.NodeBeginsParsingEvent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * A metadata exporter, that transforms the directory layout, and only includes data with older dates.
 */
public class TransformingMetadataExporter extends MetadataExporter {

    private final String presentationcopydir;
    private final String cutoffDate;

    /**
     * @param properties Defines specifics for metadata export.
     */
    public TransformingMetadataExporter(Properties properties) {
        super(properties);
        cutoffDate = properties.getProperty("metadataexporter.cutoffdate");
        presentationcopydir = properties.getProperty("metadataexporter.presentationcopydir");
    }

    protected boolean includedInExport(String name) {
        if (cutoffDate == null) {
            return true;
        }
        String date = getDate(name);
        return (date != null && date.compareTo(cutoffDate) < 0) || date == null || date == "Film";
    }

    protected String transformName(String name) {
        String newspaperid = getNewspaperId(name);
        String date = getDate(name);
        String edition = getEdition(name);
        String filename = getFileName(name);
        StringBuilder result = new StringBuilder(newspaperid);
        if (date != null) {
            result.append("/").append(date);
        }
        if (edition != null) {
            result.append("/").append(edition);
        }
        result.append("/").append(filename);
        return result.toString();
    }

    @Override
    public void handleNodeBegin(NodeBeginsParsingEvent event) {
        super.handleNodeBegin(event);
        String name = event.getName();
        if (name != null && includedInExport(name) && name.endsWith(".jp2")) {
            Path presentationFile = Paths.get(presentationcopydir, name.replaceAll("\\.jp2$", "-presentation.jp2"));
            Path link = Paths.get(metadataexportdir, transformName(name));
            link.getParent().toFile().mkdirs();
            link.toFile().delete();
            try {
                Files.createSymbolicLink(link, presentationFile);
            } catch (IOException e) {
                throw new RuntimeException("Unable to create symlink for " + link);
            }
        }
    }

    private String getNewspaperId(String name) {
        String filename = getFileName(name);
        return filename.substring(0, filename.indexOf("-"));
    }

    private String getFileName(String name) {
        return name.substring(name.lastIndexOf("/") + 1);
    }

    private String getDate(String name) {
        if (name.contains("/WORKSHIFT-ISO-TARGET/")) {
            return null;
        } else if (name.contains("/UNMATCHED/")) {
            return "Unmatched";
        } else if (name.contains("/FILM-ISO-target/")
                || name.endsWith("film.xml")) {
            return "Film";
        } else {
            String filename = getFileName(name);
            filename = filename.replaceAll("-brik", "");
            String dateAndEdition;
            if (name.endsWith("edition.xml")) {
                dateAndEdition = filename.substring(filename.indexOf("-") + 1, filename.indexOf("."));
            } else {
                dateAndEdition = filename.substring(filename.indexOf("-") + 1, filename.lastIndexOf("-"));
            }
            return dateAndEdition.substring(0, dateAndEdition.lastIndexOf("-"));
        }

    }

    private String getEdition(String name) {
        if (name.contains("/FILM-ISO-target/")
                || name.contains("/WORKSHIFT-ISO-TARGET/")
                || name.contains("/UNMATCHED/")
                || name.endsWith("film.xml")) {
            return null;
        } else {
            String filename = getFileName(name);
            filename = filename.replaceAll("-brik", "");
            String dateAndEdition;
            if (name.endsWith("edition.xml")) {
                dateAndEdition = filename.substring(filename.indexOf("-") + 1, filename.indexOf("."));
            } else {
                dateAndEdition = filename.substring(filename.indexOf("-") + 1, filename.lastIndexOf("-"));
            }
            return dateAndEdition.substring(dateAndEdition.lastIndexOf("-") + 1);
        }
    }
}
