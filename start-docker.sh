#!/bin/sh

# Add the server certificate used to authenticate the other party.
if [ -n "${HMRC_TRUSTSTORE}" ] ; then
  echo $HMRC_TRUSTSTORE | base64 --decode > /app/hmrc_truststore.jks
fi

SCRIPT=$(find . -type f -name emcs-tfe-reference-data)
exec $SCRIPT $HMRC_CONFIG -Dconfig.file=conf/emcs-tfe-reference-data.conf