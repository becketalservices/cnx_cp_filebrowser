File Manager based on WebFileSys
================================

The solution using File Browser is good but it lacks the possibility to unzip a uploaded file. So I searched for an alternative solution.
I found [WebFileSys the unviversal web-based filemanager](http://www.webfilesys.de/webfilesys-home/index.html).

This solution is build on tomcat and has no Docker image available. So we need to create our own.

# 1 Clone or download git repo

```
git clone https://github.com/becketalservices/cnx_cp_filebrowser.git \
  --branch webfilesys --single-branch webfilesys

```

# 2 Prepare Docker image

## 2.1 Create directory via script
I did not include the source files into this repository, you need to download and extract them first.

Please check the license information about WebFileSys on the bottom of the [About WebFileSys](http://www.webfilesys.de/webfilesys-home/webfilesys.html) page.

1. Download webfilesys  
Open the [WebFileSys](http://www.webfilesys.de/webfilesys-home/download.html) homepage and download the latest package.
Place the package into the root folder of the git repository.
2. run configuration script  
The configuration script does all the work for you, adding the HCL Connections authentication solution as well. 
In case you want to do it manually, check what the script does. 
The script requres unzip, curl and xmlstarlet. So make shure the appropriate packages are on your system. The http client jar files are downloaded directly from the internet. Make shure the right environment variables exists, so that curl uses your proxy. 
  
```
./configure.sh <default password>
```
  
check the output for errors.

# 2.2 Create directory manually
I did not include the source files into this repository, you need to download and extract them first.

Please check the license information about WebFileSys on the bottom of the [About WebFileSys](http://www.webfilesys.de/webfilesys-home/webfilesys.html) page.

1. Download webfilesys  
Open the [WebFileSys](http://www.webfilesys.de/webfilesys-home/download.html) homepage and download the latest package.  
Place the package into the root folder of the git repository.
2. Extract the contents: 
  
```
mkdir webfilesys
unzip webfilesys-*.zip -d webfilesys
mkdir webfilesys/webfilesys_war
unzip webfilesys/webfilesys.war -d webfilesys/webfilesys_war

``` 

3. In case you want to modify the default user / passwords / documentRoot
modify the file webfilesys/webfilesys\_war/WEB-INF/users.xml.unix<br>
an example is in the root directory where a 2nd user exists and the documentRoot is /mnt/files<br>
`cp users.xml.unix webfilesys/webfilesys_war/WEB-INF/users.xml.unix`
4. In case you want to have a more HCL Connections like UI color scheme
Copy the cnx style into the webfilesys\_war directory  
`cp -r style/* webfilesys/webfilesys_war/`  
To make it the default ui, you need to modify the users.xml.unix  
`sed -i "s/<css>.*<\/css>/<css>ics<\/css>/" webfilesys/webfilesys_war/WEB-INF/users.xml.unix`

# 3 Build Docker image
_In case your connections instance is running a self signed certificate or a company issued certificate, you need to trust it first_

extract the cacerts file from the Docker image.

```
imgid=$(docker create tomcat:8.5-jre8-alpine)
docker cp $imgid:/etc/ssl/certs/java/cacerts .
docker rm $imgid

```

retrieve the self signed certificate from your connections.

```
URL=<connections hostname:port>
openssl s_client -showcerts -connect $URL < /dev/null | openssl x509 -outform DER > cacert.der

```

merge self signed certifcate into the keystore. 
the default password is `chagneit` 
make sure you answer the question if you trust the certificae with yes.

```
keytool -import -trustcacerts -file cacert.der -alias selfsignedcert -keystore cacerts

```

Add a line in the Dockerfile to copy the cecerts file into the Docker image:

```
COPY cacerts /etc/ssl/certs/java/cacerts

```

To build the final image run:

run ./build.sh

# 4 Test your image
You can test your new docker image:

1. create directories config and files
2. run ./run.sh
3. Access the container on port :8888. 
The default username is admin / topsecret

# 5 Upload to your Docker registry
You need to tag and then upload the image
  
```
# Path to your registry. e.g.: cpcontainerregistry.azurecr.io
registry=<registry>

# In case you have multiple versions, you can change the tag.
tag=$(date "+%Y%m%d%H%M") 

# Tag the local image
docker tag webfilesys:latest $registry/webfilesys:$tag

# Push to docker registry
docker push $registry/webfilesys:$tag

```

# 6 Use Helm to install the final solution

You need the name of the Storeage Class if you do not want to use the default for creating a new persistant volume. This volume will host the users.xml where your user accounts are stored.

As you uploaded the docker image to your private registry, the registry name is also required.

Run on your kubernetes installation host or master:

```
# Path to your registry. e.g.: cpcontainerregistry.azurecr.io
registry=<registry>

# Image Tag used during creation 
tag=$tag

# Name of your storage Class
storeClassName=default

# Debug Level (default:0, debug:1)
debuglevel=0

# Install
helm install ./helm/webfilesys \
  --name webfilesys \
  --namespace connections \
  --set image.repository=$registry \
  --set-string image.tag=$tag \
  --set storageClassName=$storeClassName \
  --set env.debuglevel=$debuglevel

```

You can then simply add the tool by adding the usual Proxy Rules inside the HTTP Server config:

```
ProxyPass "/webfilesys" "http://master_node_host_name:31677/webfilesys" 
ProxyPassReverse "/webfilesys" "http://master_node_host_name:31677/webfilesys"

```

The default login is admin:<password set in 2.1 - or topsecret>  
This might be different in case you used a custom users.xml.unix file.  
I recommend to change all password for all default users. 

Depending on the methond you choose to create your docker image, there is an integration with HCL Connections. When the integration is active, you get redirected to your HCL Connections Homepage for authentication equal to the method, the Component Pack Customizer uses. If you need more inforamation about the HCL Connections Authentication check the documentation for [Authentication modules for HCL Connections based Authentication](https://github.com/becketalservices/cnx_cp_filebrowser/tree/wfs_cnx_auth). To bypass the HCL Connections Authentication, use /webfilesys/servlet?command=loginForm as URL. This will force the webfilesys authentication based on the users.xml file.

# 7 Configuration

Currently there are no configurations available beside that you can manage users.  

In case you want to do more with WebFileSys than managing files for HCL Customizer, feel free to modify the _webfilesys.conf_ or any other configuration file. You can either backe you modifications into your docker image or move the files into the /mnt/config directory. Adapt the _setup/startup.sh_ script to initialize your config directory and the Dockerfile to create links from the original position of the files into the config directory.
 
# 8 License

The provided instructions and other assets are licensed under the GPL V3.

Please note the request of the author of WebFileSys and register your copy. For details see the license chapter in [About WebFileSys](http://www.webfilesys.de/webfilesys-home/webfilesys.html).

