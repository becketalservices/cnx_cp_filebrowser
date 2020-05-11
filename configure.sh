#!/bin/bash
set -o pipefail
version=202005111717

echo "Assemble webfilesys directory for docker container"

DEFAULTPWD="$1"

BASEDIR=$(dirname $0)
filterName=LtpaTokenAuthFilter
filterClass=de.beas.LtpaTokenAuthFilter
usermgrClass=de.beas.DefaultValueUserManager
urlPattern="/servlet/*"
error_pages[403]=/html/NotAuthorized.html
xmlfile=$BASEDIR/webfilesys/webfilesys_war/WEB-INF/web.xml
tmpext=$(date +%Y%m%d%H%M%S)
# using maven repository to download additional jars
declare -A jars
jars[httpclient-4.5.12.jar]="https://repo1.maven.org/maven2/org/apache/httpcomponents/httpclient/4.5.12/httpclient-4.5.12.jar"
jars[httpcore-4.4.13.jar]="https://repo1.maven.org/maven2/org/apache/httpcomponents/httpcore/4.4.13/httpcore-4.4.13.jar"
jars[commons-logging-1.2.jar]="https://repo1.maven.org/maven2/commons-logging/commons-logging/1.2/commons-logging-1.2.jar"
jars[commons-codec-1.14.jar]="https://repo1.maven.org/maven2/commons-codec/commons-codec/1.14/commons-codec-1.14.jar"

function extract_wefilessys {
  if [ ! -e "$BASEDIR/webfilesys" ]; then
    mkdir -p "$BASEDIR/webfilesys"
  fi
  if [ -d "$BASEDIR/webfilesys" ]; then
    if [ ! -e "$BASEDIR/webfilesys/webfilesys_war" ]; then
      mkdir -p "$BASEDIR/webfilesys/webfilesys_war"
    fi
    if [ -d "$BASEDIR/webfilesys/webfilesys_war" ]; then
      if [ ! -e "$BASEDIR/webfilesys/webfilesys.war" ];then
        latestzip=$(ls $BASEDIR/webfilesys-*.zip | tail -1)
        if [ $? -eq 0 ]; then
          echo "  Extracting $latestzip"
          unzip -q -d "$BASEDIR/webfilesys" $latestzip
          if [ $? -gt 0 ]; then
            echo
            echo "  ERROR: unzip $latestzip -d '$BASEDIR/webfilesys' failed."
            return 1
          fi
        else
          echo
          echo "  ERROR: No webfilesys sofware package found. Please download first."
          return 1
        fi
      fi
      if [ -e "$BASEDIR/webfilesys/webfilesys.war" ];then
        echo "  Extracting $BASEDIR/webfilesys/webfilesys.war"
        unzip -q -d "$BASEDIR/webfilesys/webfilesys_war" "$BASEDIR/webfilesys/webfilesys.war"
        if [ $? -gt 0 ]; then
          echo
          echo "  ERROR: unzip '$BASEDIR/webfilesys/webfilesys.war' -d '$BASEDIR/webfilesys/webfilesys_war' failed."
          return 1
        fi
      else
        echo
        echo "  ERROR: $BASEDIR/webfilesys/webfilesys.war does not exist after unzip. Can not continue."
        return 1
      fi
    else
      echo
      echo "  ERROR: $BASEDIR/webfilesys/webfilesys_war is not a directory. Can not extract."
      return 1
    fi
  else 
    echo
    echo "  ERROR: $BASEDIR/webfilesys is not a directory. Can not extract."
    return 1
  fi
  if [ -e "$BASEDIR/webfilesys/webfilesys_war/WEB-INF/webfilesys.conf" ]; then  
    echo "  Extraction successfully done."
  else
    echo
    echo "  ERROR: After extration, '$BASEDIR/webfilesys/webfilesys_war/WEB-INF/webfilesys.conf' still does not exists."
    return 1 
  fi
}

function addFilterConfig {
  addFilter
  if [ $? -gt 0 ]; then
    return 1
  fi
  addFilterMapping
  if [ $? -gt 0 ]; then
    return 1
  fi
}

function addFilter {
  xmlstarlet ed -a '/web-app/filter[last()]' -t elem -n filter -v "" $xmlfile > /tmp/file1.xml
  if [ $? -eq 0 ]; then
    xmlstarlet ed -s '/web-app/filter[last()]' -t elem -n filter-name -v "$filterName" /tmp/file1.xml > /tmp/file2.xml
    if [ $? -eq 0 ]; then
      xmlstarlet ed -s "/web-app/filter[filter-name='$filterName']" -t elem -n filter-class -v "$filterClass" /tmp/file2.xml > /tmp/file3.xml
      if [ $? -eq 0 ]; then
        cp /tmp/file3.xml $xmlfile
        rm /tmp/file1.xml
        rm /tmp/file2.xml
        rm /tmp/file3.xml
      fi
    fi
  fi
}

function addFilterMapping {
  xmlstarlet ed -a '/web-app/filter-mapping[last()]' -t elem -n filter-mapping -v "" $xmlfile > /tmp/file1.xml
  if [ $? -eq 0 ]; then
    xmlstarlet ed -s '/web-app/filter-mapping[last()]' -t elem -n filter-name -v "$filterName" /tmp/file1.xml > /tmp/file2.xml
    if [ $? -eq 0 ]; then
      xmlstarlet ed -s "/web-app/filter-mapping[filter-name='$filterName']" -t elem -n url-pattern -v "$urlPattern" /tmp/file2.xml > /tmp/file3.xml
      if [ $? -eq 0 ]; then
        cp /tmp/file3.xml $xmlfile
        rm /tmp/file1.xml
        rm /tmp/file2.xml
        rm /tmp/file3.xml
      fi
    fi
  fi
}

function errorPage {
  number=$1
  location=$2
  echo "  check Error $number, $location"
  erg=$(xmlstarlet sel -t -c "count(/web-app/error-page)" $xmlfile)
  if [ "$erg" == "0" ]; then
    echo "    add first error page"
    xmlstarlet ed -s '/web-app' -t elem -n error-page -v "" $xmlfile > /tmp/file1.xml
    err=$?
  else
    echo "    check for exisisting error page"
    erg=$(xmlstarlet sel -t -c "count(/web-app/error-page[error-code='$number'])" $xmlfile)
    if [ "$erg" == "0" ]; then
      echo "    error page $number does not exist"
      xmlstarlet ed -a '/web-app/error-page[last()]' -t elem -n error-page -v "" $xmlfile > /tmp/file1.xml
      err=$?
    else
      echo "    error page $number already exists"
      return 0
    fi
  fi
  if [ $err -eq 0 ]; then
    xmlstarlet ed -s '/web-app/error-page[last()]' -t elem -n error-code -v "$number" /tmp/file1.xml > /tmp/file2.xml
    if [ $? -eq 0 ]; then
      xmlstarlet ed -s "/web-app/error-page[error-code='$number']" -t elem -n location -v "$location" /tmp/file2.xml > /tmp/file3.xml
      if [ $? -eq 0 ]; then
        cp /tmp/file3.xml $xmlfile
        rm /tmp/file1.xml
        rm /tmp/file2.xml
        rm /tmp/file3.xml
      fi
    fi
  fi
}

function addJar {
  jar=$1
  sourceURL=$2
  echo "  Add $jar from $sourceURL"
  if [ ! -e "$BASEDIR/webfilesys/webfilesys_war/WEB-INF/lib/$1" ]; then
    echo "  curl -sS -o '$BASEDIR/webfilesys/webfilesys_war/WEB-INF/lib/$1' '$sourceURL'"
    curl -sS -o "$BASEDIR/webfilesys/webfilesys_war/WEB-INF/lib/$1" "$sourceURL"
    if [ $? -eq 0 ]; then
      echo "  Download $jar successfully done."
    else
      echo "  ERROR: Download $jar failed."
      return 1
    fi
  else
    echo "  $jar alredy exist. No download necessary."
  fi
}

if [ "$DEFAULTPWD" == "" ]; then
  echo "ERROR: no default password given."
  echo "Run: $0 <default password>"
  exit 1
fi

echo "Check required commands"
command -v xmlstarlet > /dev/null
if [ $? -gt 0 ]; then
  echo "ERROR: command xmlstarlet missing."
  exit 1
fi

echo "Check webfilesys_war directory"

if [ ! -e "$BASEDIR/webfilesys/webfilesys_war/WEB-INF/webfilesys.conf" ]; then
  echo "  webfilesys_war not yet extracted."
  extract_wefilessys
  if [ $? -gt 0 ]; then
    echo
    echo "------------------------------"
    echo "ERROR etraction failed. Abort"
    echo
    exit 1
  fi
else
  echo "  webfilesys_war already extracted. Nothign to do"
fi

echo "Adding custom log configurations"
cp -r $BASEDIR/plugin/log4j* $BASEDIR/webfilesys/webfilesys_war/WEB-INF/classes/
if [ $? -gt 0 ]; then
  echo "-------------------------------------"
  echo "ERROR add custom log failed. Abort"
  echo
  exit 1
else
  echo "  Add custom log successfully done."
fi

echo "Adding users.xml and set passwords"
#cp $BASEDIR/users.xml.unix $BASEDIR/webfilesys/webfilesys_war/WEB-INF/
xmlstarlet ed -u "users/user/password" -v "$DEFAULTPWD" $BASEDIR/users.xml.unix > $BASEDIR/webfilesys/webfilesys_war/WEB-INF/users.xml.unix
if [ $? -gt 0 ]; then
  echo "------------------------------"
  echo "ERROR copy failed. Abort"
  echo
  exit 1
else
  echo "  Copy users.xml successfully done."
fi

echo "Add style"
if [ ! -e "$BASEDIR/webfilesys/webfilesys_war/styles/skins/ics.css" ]; then
  echo "  Style missing"
  cp -r $BASEDIR/style/* "$BASEDIR/webfilesys/webfilesys_war/"
  if [ $? -eq 0 ]; then
    echo "  Style copy operation successfully done."
  else
    echo "  ERROR: Style copy operation failed. Abort"
    exit 1
  fi
else
  echo "  Style already exists. Nothing to do"
fi

echo "Add plugin jar"
if [ ! -e "$BASEDIR/webfilesys/webfilesys_war/WEB-INF/lib/de.beas.addon.jar" ]; then
  cp $BASEDIR/plugin/de.beas.addon.jar $BASEDIR/webfilesys/webfilesys_war/WEB-INF/lib
  if [ $? -gt 0 ]; then
   echo "------------------------------"
    echo "ERROR copy failed. Abort"
    echo
    exit 1
  else
    echo "  Copy plugin jar successfully done."
  fi
else
  echo "  Plugin already exist. Nothing copied."
fi

echo "Add additional jar: ${!jars[@]}"
for i in "${!jars[@]}"; do
  addJar $i ${jars[$i]}
  if [ $? -gt 0 ]; then
    echo
    echo "--------------------------------"
    echo "ERROR: Add jar $i failed."
    echo
    exit 1
  else
    echo "  jar $i successfully processed"
  fi
done

echo "Adding html pages"
cp $BASEDIR/html/* $BASEDIR/webfilesys/webfilesys_war/html/
if [ $? -gt 0 ]; then
  echo "------------------------------"
  echo "ERROR copy failed. Abort"
  echo
  exit 1
else
  echo "  Copy files successfully done."
fi

echo "Load custom user manager"
if [ -e "$BASEDIR/webfilesys/webfilesys_war/WEB-INF/webfilesys.conf" ]; then
  erg=$(grep "^UserManagerClass" "$BASEDIR/webfilesys/webfilesys_war/WEB-INF/webfilesys.conf")
  if [ $? -gt 0 ]; then
    echo "  webfilesys.conf has no entry for UserManagerClass yet. Add"
    echo "UserManagerClass=$usermgrClass">>"$BASEDIR/webfilesys/webfilesys_war/WEB-INF/webfilesys.conf"
  else
    echo "  webfilesys.conf has alrday an entry for UserManagerClass. No change done."
  fi
else
    echo
    echo "--------------------------------"
    echo "ERROR webfilesys.conf not found."
    echo
    exit 1
fi

echo "backup $xmlfile"
cp $xmlfile ${xmlfile}.${tmpext}
if [ $? -eq 0 ]; then
  echo "  backup to ${xmlfile}.${tmpext} done"
else
  echo "-------------------------------------"
  echo "ERROR backup failed. Abort"
  echo
  exit 1
fi
  
echo "check filter configuration"
erg=$(xmlstarlet sel -t -c "count(/web-app/filter[filter-name='$filterName'])" $xmlfile)
if [ "$erg" == "0" ]; then
  echo "  Filter does not yet exists. Adding..."
  addFilterConfig
  if [ $? -gt 0 ]; then
    echo
    echo "-------------------------"
    echo "ERROR. Add filter failed."
    echo
    exit 1
  else
    echo "  Add filter successfully done."
  fi
else
  if [ "$erg" == "1" ]; then
    echo "  Filter already exists."
  else
    echo
    echo "-------------------------------------------------"
    echo "ERROR: check command returned unknown response: $erg"
    echo
    exit 1
  fi
fi

echo "process error pages"
for i in "${!error_pages[@]}"; do
  errorPage $i ${error_pages[$i]}
  if [ $? -gt 0 ]; then
    echo
    echo "--------------------------------"
    echo "ERROR: Add error page $i failed."
    echo
    exit 1
  else
    echo "  error page successfully processed"
  fi
done

echo
echo "Customization successfully finished."
echo "run ./build.sh to create the docker container"
echo

