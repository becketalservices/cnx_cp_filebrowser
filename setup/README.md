Startup Script for the Docker containter
========================================

# Environment Variables

* DEBUG\_LEVEL  
Set to 1 to use debug logging for webfilesys and the LTPA login manager
* CATALINA\_OPTS  
This option will be enhanced and then forwarded to the tomcat server
* CONFIG\_DIR  
Configuration directory. Defaults to /mnt/config  
Directory must be persistant. ReadWriteOnly storage class is sufficient.
* BASE\_DIR  
File directory. Defaults to /mnt/files  
Directory shown as root directory. Probably used by multiple instances, so use ReadWriteMany storage class.
* DEFAULT\_CSS  
Default css style used by the DefaultValueUserManager.
Defaults to ics.
* DEFAULT\_LANG  
Default language used by the DefaultValueUserManager.  
Defaults to English.
* DEFAULT\_ROLE  
Default role used by the DefaultValueUserManager.  
Defaults to webspace.
* IC\_HOMEPAGE  
Set to front door homepage url.
* IC\_CONNECTIONS  
Set to back door connections url.
* IC\_PROFILES  
Set to back door profiles url.
* IC\_HOST  
Set to back door host. Must match the host part of IC\_CONNECTIONS and IC\_PROFILES.

