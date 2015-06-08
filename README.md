Newspaper-metadata-exporter
==========================

This module exports newspaper metadata from one tree to a new one.

It will include MODS, MIX and FILM metadata files and OCR, but not actual jp2-files. The output will mimic the metadata 
exchange format we originally received the files in. 

Optionally it will instead transform and filter the directory layout to one directory to avoid film directories, 
and filter by a given date. The transforming exporter will also make symlinks to the presentation copies.

## Installation

This comes as an autonomous component .tar.gz file. Unpack it, and set configuration, and it will be ready to be run from cron.
It also includes a standalone application, that will run on one specific batch.

## Configuration

In addition to the standard configuration parameters for autonomous components, this component requires the following
properties (values given below are dummy-examples):

 * metadataexporter.outputdir: Where to place the generated metadata.
 * metadataexporter.transform: Whether to transform and filter result files (default is false)
 * metadataexporter.eventID: Sets the eventID - this allows running multiple exporters with different eventIDs as
                             autonomous components
 * metadataexporter.cutoffdate: Used by the filtering, only dates before this are included. Format is yyyy-MM-dd. If unset, output is not filtered. Note that if set, unmatched pages are not included, since we don't know the date.
 * metadataexporter.presentationcopydir. Used by the transforming exporter to make symlinks to presentation copies.

## Design

This component builds on the tree-iterator framework, and simply serializes and saves the file and a checksum for it in the given directory.
Transforming and filtering is done in a subclass, that could be replaced/amended for further transformation/filtering.
