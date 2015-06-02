package dk.statsbiblioteket.medieplatform.newspaper.metadataexporter;

import java.util.Properties;

/**
 * A metadata exporter, that transforms the directory layout, and only includes data with older dates.
 */
public class TransformingMetadataExporter extends MetadataExporter {

    private String cutoffDate;

    /**
     * @param properties Defines specifics for metadata export.
     */
    public TransformingMetadataExporter(Properties properties) {
        super(properties);
        cutoffDate = properties.getProperty("metadataexporter.cutoffdate");
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
