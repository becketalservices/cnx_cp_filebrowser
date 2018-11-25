#!/bin/bash
echo "Startup $PWD"

if [ ! -e /mnt/config/users.xml ]; then
  cp $CATALINA_HOME/webapps/webfilesys/WEB-INF/users.xml.unix /mnt/config/users.xml
fi

if [ ! -e /mnt/config/decorations.xml ]; then
cat << EOF > /mnt/config/decorations.xml
<?xml version="1.0" encoding="UTF-8"?>
<decorations>
</decorations>
EOF
fi

. ./bin/catalina.sh $1 $2 $3

