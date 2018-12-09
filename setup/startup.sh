#!/bin/bash
echo "Startup $PWD"

if [ -z "$CONFIG_DIR" ]; then
  CONFIG_DIR=/mnt/config
fi

echo
echo "Configuration parameter:"
echo "CATALINA_OPTS=$CATALINA_OPTS"
echo "CONFIG_DIR=$CONFIG_DIR"
echo "BASE_DIR=$BASE_DIR"
echo "DEFAULT_CSS=$DEFAULT_CSS"
echo "DEFAULT_LANG=$DEFAULT_LANG"
echo "DEFAULT_ROLE=$DEFAULT_ROLE"
echo "IC_HOMEPAGE=$IC_HOMEPAGE"
echo "IC_CONNECTIONS=$IC_CONNECTIONS"
echo "IC_PROFILES=$IC_PROFILES"
echo "IC_HOST=$IC_HOST"
echo "DEBUG_LEVEL=$DEBUG_LEVEL"
echo

if [ "$BASE_DIR" ]; then
  CATALINA_OPTS="$CATALINA_OPTS -Dde.beas.DefaultValueUserManager.docroot=$BASE_DIR"
fi
if [ "$DEFAULT_CSS" ]; then
  CATALINA_OPTS="$CATALINA_OPTS -Dde.beas.DefaultValueUserManager.css=$DEFAULT_CSS" 
fi
if [ "$DEFAULT_LANG" ]; then
  CATALINA_OPTS="$CATALINA_OPTS -Dde.beas.DefaultValueUserManager.language=$DEFAULT_LANG"
fi
if [ "$DEFAULT_ROLE" ]; then
  CATALINA_OPTS="$CATALINA_OPTS -Dde.beas.DefaultValueUserManager.role=$DEFAULT_ROLE"
fi

if [ "$IC_HOMEPAGE" ]; then
  CATALINA_OPTS="$CATALINA_OPTS -Dde.beas.LtpaTokenAuthFilter.homepage=$IC_HOMEPAGE"
fi
if [ "$IC_CONNECTIONS" ]; then
  CATALINA_OPTS="$CATALINA_OPTS -Dde.beas.LtpaTokenAuthFilter.connections=$IC_CONNECTIONS"
fi
if [ "$IC_PROFILES" ]; then
  CATALINA_OPTS="$CATALINA_OPTS -Dde.beas.LtpaTokenAuthFilter.profiles=$IC_PROFILES"
fi
if [ "$IC_HOST" ]; then
  CATALINA_OPTS="$CATALINA_OPTS -Dde.beas.LtpaTokenAuthFilter.host=$IC_HOST"
fi

#if [ -z "$LOG4J_CONFIGURATION_FILE" ]; then
#  if [ "$DEBUG_LEVEL" ]; then
#    LOG4J_CONFIGURATION_FILE=log4j${DEBUG_LEVEL}.xml
#  else
#    LOG4J_CONFIGURATION_FILE=log4j0.xml
#  fi
#fi
#if [ ! -f "$LOG4J_CONFIGURATION_FILE" -a ! -f "$CATALINA_HOME/webapps/webfilesys/WEB-INF/classes/$LOG4J_CONFIGURATION_FILE" ]; then
#  echo WARNING: log4j configuration file not found.
#  echo looked at $LOG4J_CONFIGURATION_FILE and $CATALINA_HOME/webapps/webfilesys/WEB-INF/classes/$LOG4J_CONFIGURATION_FILE
#fi

if [ "$DEBUG_LEVEL" == "1" ]; then
  cp "$CATALINA_HOME/webapps/webfilesys/WEB-INF/classes/log4j1.xml" "$CATALINA_HOME/webapps/webfilesys/WEB-INF/classes/log4j.xml"
else
  cp "$CATALINA_HOME/webapps/webfilesys/WEB-INF/classes/log4j0.xml" "$CATALINA_HOME/webapps/webfilesys/WEB-INF/classes/log4j.xml"
fi

if [ ! -e $CONFIG_DIR/users.xml ]; then
  cp $CATALINA_HOME/webapps/webfilesys/WEB-INF/users.xml.unix $CONFIG_DIR/users.xml
fi

#if [ ! -e $CONFIG_DIR/webfilesys.conf ]; then
#  cp $CATALINA_HOME/webapps/webfilesys/WEB-INF/webfilesys.conf $CONFIG_DIR/webfilesys.conf
#fi
#rm $CATALINA_HOME/webapps/webfilesys/WEB-INF/webfilesys.conf
#ln -s $CONFIG_DIR/webfilesys.conf $CATALINA_HOME/webapps/webfilesys/WEB-INF/webfilesys.conf


if [ ! -e $CONFIG_DIR/decorations.xml ]; then
cat << EOF > /mnt/config/decorations.xml
<?xml version="1.0" encoding="UTF-8"?>
<decorations>
</decorations>
EOF
fi

echo "Final LOG4J_CONFIGURATION_FILE=$LOG4J_CONFIGURATION_FILE"
echo "Final CATALINA_OPTS=$CATALINA_OPTS"

. ./bin/catalina.sh $1 $2 $3

