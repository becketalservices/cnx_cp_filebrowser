FROM tomcat:8.5-jre8-alpine

COPY webfilesys/webfilesys_war /usr/local/tomcat/webapps/webfilesys
COPY setup $CATALINA_HOME/bin

ENV CATALINA_OPTS -Dfile.encoding=UTF-8

RUN chmod u+x $CATALINA_HOME/bin/startup.sh; \
  rm -rf $CATALINA_HOME/webapps/ROOT; \
  rm -rf $CATALINA_HOME/webapps/docs; \
  rm -rf $CATALINA_HOME/webapps/examples; \
  rm -rf $CATALINA_HOME/webapps/host-manager; \
  rm -rf $CATALINA_HOME/webapps/manager; \
  mkdir $CATALINA_HOME/webapps/ROOT; \
  mkdir -p /mnt/config; mkdir -p /mnt/files; \ 
  sed -i "s/Connector port/Connector URIEncoding=\"UTF-8\" port/" $CATALINA_HOME/conf/server.xml; \
  ln -s /mnt/config/users.xml $CATALINA_HOME/webapps/webfilesys/WEB-INF/users.xml ; \
  ln -s /mnt/config/decorations.xml $CATALINA_HOME/webapps/webfilesys/WEB-INF/decorations.xml ; \
  cp $CATALINA_HOME/webapps/webfilesys/index.html $CATALINA_HOME/webapps/ROOT/index.html

CMD ["startup.sh", "run"]

