Newspaper-metadata-exporter
==========================

This module exports newspaper metadata from one tree to a new one.

It will include MODS, MIX and FILM metadata files and OCR, but not actual files.

The output will mimic the metadata exchange format we originally received the files in.

## Installation

This comes as an autonomous component .tar.gz file. Unpack it, and set configuration, and it will be ready to be run from cron.

## Configuration

In addition to the standard configuration parameters for autonomous components, this component requires the following
properties (values given below are dummy-examples):
 * metadataexporter.outputdir: Where to place the generated metadata.

## Design

This component builds on the tree-iterator framework, and simple serializes and saves the file and a checksum for it in the given directory.
