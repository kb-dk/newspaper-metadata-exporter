#!/bin/sh

# Usage: metadataexporter.sh -b <batchid> -n <roundtripnumber>

SCRIPT_DIR=$(dirname $(readlink -f $0))

java -classpath "$SCRIPT_DIR/../conf:$SCRIPT_DIR/../lib/*" \
   dk.statsbiblioteket.medieplatform.newspaper.metadataexporter.MetadataExporterStandalone -c $SCRIPT_DIR/../conf/config.properties "$@"