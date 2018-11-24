#!/bin/bash
echo "Startup $PWD"

if [ ! -e /mnt/config/users.xml ]; then
  cp $CATALINA_HOME/webapps/webfilesys/WEB-INF/users.xml.unix /mnt/config/users.xml
fi

. ./bin/catalina.sh $1 $2 $3

