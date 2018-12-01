Authentication modules for IBM Connections based Authentication
===============================================================

This branch contains the java sources for the Authentication filter and the User Manager that support the authentication against IBM Connections for WebFileSys.

# Introduction

## DefaultValueUserManager

The DefaultValueUserManager extends the XMLUserManager from WebFileSys. 
The main purpose is to present user objects to WebFileSys that do not exist in the users.xml file as users authenticated via IBM Connections Authentication do not need to have an own user profile within WebFileSys but they can.
When the administrator creates a user profile, this is used. Make sure the userid of the WebFileSys user matches the userid from IBM Connections. You see the userid that WebFileSys recognized in the menu bar of WebFileSys. The userid is case sensitive.

Users which do not have a native WebFileSys user profile, can change their user profile settings but the settings are never saved and used. 


## LtpaTokenAuthFilter

The LtpaTokenAuthFilter intercepts the HTTP traffic between the user and WebFileSys. It checks if a valid session with userid exists and forwards this HTTP traffic withou further modification to WebFileSys.
In case no valid session or userid exists, the filter checks if LtpaToken or LtpaToken2 cookies exist in the request. If not, the user gets redirected to the IBM Connections homepage login page for authentication. When cookies exist after they authenticated, this cookies are used to authenticate the user against the IBM Connections backend by issuing some API calls. 
To be allowed to use WebFileSys the user must have the admin role in the Connecions Application. This behavior is taken from the appregistry application.

To bypass the IBM Connections authentication to login as native WebFileSys user (e.g. admin), you need to open the WebFileSys login form directly. Use _<server>webfilesys/servlet?command=loginForm_. Only users present in the users.xml can login. The password is taken out of the users.xml file.

Make sure your native WebFileSys users change their password regularly or at least enter a complex password.


# Configuration

## DefaultValueUserManager configuration

You can configure the DefaultValueUserManager via System properties.
Please check the WebFileSys documentation for allowed values.

1. de.beas.DefaultValueUserManager.css  
Specify the CSS sytle to use. Defaults to fmweb. Known values are: bluemoon, dark, fmweb, sunset
2. de.beas.DefaultValueUserManager.language  
Specify the Language. Defaults to English. Known values are: English, German, Spanish
3. de.beas.DefaultValueUserManager.role  
Specify the Role. Defaults to webspace. Known values are: admin, user, webspace.
4. de.beas.DefaultValueUserManager.docroot  
Specify the Document Root. Defaults to *: for Windows and / for Unix. 

Example:

```
-Dde.beas.DefaultValueUserManager.language=German
-Dde.beas.DefaultValueUserManager.docroot=C:/DATA
```


## LtpaTokenAuthFilter configuration

You must set these system properties to initialize the LtpaTokenAuthFilter  
The property values can be read from the connections-env configmap when deploying with IBM Component Pack

1. de.beas.LtpaTokenAuthFilter.homepage  
This property should map to the homepage URL of the front door.  
Map to ic-homepage-url from the connections-env configmap.
2. de.beas.LtpaTokenAuthFilter.connections  
This property should map to the connections URL of the backend door.  
Map to ic-connections-url from the connections-env configmap.
3. de.beas.LtpaTokenAuthFilter.profiles  
This property should map to the profiles URL of the backend door.  
Map to ic-profiles-url from the connections-env configmap.
4. de.beas.LtpaTokenAuthFilter.host  
This property should map to the front door host.  
Map to ic-host from the connections-env configmap.

Example:

```
-Dde.beas.LtpaTokenAuthFilter.homepage=https://cnx.beaslabs.com/homepage
-Dde.beas.LtpaTokenAuthFilter.connections=https://cnx-backend.beaslabs.com/connections
-Dde.beas.LtpaTokenAuthFilter.profiles=https://cnx-backend.beaslabs.com/profiles
-Dde.beas.LtpaTokenAuthFilter.host=cnx.beaslabs.com
```

The authentication cookie names are currently hard coded to _LtpaToken_ and _LtpaToken2_. 

## Other useful configurations

When your back end is not using TLSv1.2 or does not have a globally trusted certificate, you might need to configure the HTTP client used by the filter.

Common configuration values are:

```
-Dhttps.protocols=TLSv1,TLSv1.1,TLSv1.2
-Djavax.net.ssl.trustStore=/mnt/config/truststore.jks
-Djavax.net.ssl.trustStorePassword=changeme
```

To generate the trust certificate sore, you can use the java keytool.
Store the certificate you want to trust in Base64 format as trust_root.cer

```
keytool -import -file /mnt/config/trust_root.cer -trustcacerts -keystore /mnt/config/truststore.jks -storepass changeme

```
