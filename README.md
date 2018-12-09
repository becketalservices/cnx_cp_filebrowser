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

# 3 Create Docker image via script
I did not include the source files into this repository, you need to download and extract them first.

Please check the license information about WebFileSys on the bottom of the [About WebFileSys](http://www.webfilesys.de/webfilesys-home/webfilesys.html) page.

1. Download webfilesys
Open the [WebFileSys](http://www.webfilesys.de/webfilesys-home/download.html) homepage and download the latest package.
Place the package into the root folder of the git repository.
2. run configuration script 
The configuratio script does all the work for you adding the IBM Connections authentication solution as well. 
In case you want to it manually, check what the script does. 
The script requres curl and xmlstarlet. So make shure the appropriate packages are on your system. The http client jar files are downloaded directly from the internet. Make shure the right environment variables exists, so that curl uses your proxy. 
  
```
  ./configure.sh
```
  
check the output for errors.

# 3 Create basic Docker image manually
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
4. In case you want to have a more IBM Connections like UI color scheme
Copy the cnx style into the webfilesys\_war directory  
`cp -r style/* webfilesys/webfilesys_war/`  
To make it the default ui, you need to modify the users.xml.unix  
`sed -i "s/<css>.*<\/css>/<css>ics<\/css>/" webfilesys/webfilesys_war/WEB-INF/users.xml.unix`
5. run ./build.sh

# 4 Test your image
You can test your new docker image:

1. create directories config and files
2. run ./run.sh
3. Access the container on port :8888. 
The default username is admin / topsecret

# 4 Upload to our Docker registry
You need to tag and then upload the image
  
```
# Path to your registry. e.g.: cpcontainerregistry.azurecr.io
registry=<registry>

# In case you have multiple versions, you can change the tag.
tag=latest 

# Tag the local image
docker tag webfilesys:latest $registry/webfilesys:$tag

# Push to docker registry
docker push $registry/webfilesys:$tag

```

# 5 Use Helm to install the final solution

You need the name of the Storeage Class if you do not want to use the default for creating a new persistant volume. This volume will host the users.xml where your user accounts are stored.

As you uploaded the docker image to your private registry, the registry name is also required.

run on your kubernetes installation host or master:

```
# Path to your registry. e.g.: cpcontainerregistry.azurecr.io
registry=<registry>

# Name of your storage Class
storeClassName=default

# Debug Level (default:0, debug:1)
debugleve=0

# Install
helm install ./helm/webfilesys \
  --name webfilesys \
  --namespace connections \
  --set image.repository=$registry \ 
  --set storageClassName=$storeClassName \
  --set env.debuglevel=$debuglevel

```

You can then simply add the tool by adding the usual Proxy Rules inside the HTTP Server config:

```
ProxyPass "/webfilesys" "http://master_node_host_name:31677/webfilesys" 
ProxyPassReverse "/webfilesys" "http://master_node_host_name:31677/webfilesys"

```

The default login is admin:topsecret  
This might be different in case you used a custom users.xml.unix file.  
I recommend to change all password for all default users. 

There is no integration with the Connections Authentication. It is just a stand alone web-based file manager that acts on the same storage as the IBM Connections Component Pack Customizer.

# 6 Configuration

Currently there are no configurations available beside that you can manage users.  

In case you want to do more with WebFileSys than managing files for IBM Customizer, feel free to modify the _webfilesys.conf_ or any other configuration file. You can either backe you modifications into your docker image or move the files into the /mnt/config directory. Adapt the _setup/startup.sh_ script to initialize your config directory and the Dockerfile to create links from the original position of the files into the config directory.
 
# 7 License

The provided instructions and other assets are licensed under the GPL V3.

Pleae honor the which of the author of WebFileSys and register your copy. For details see the License chapter in [About WebFileSys](http://www.webfilesys.de/webfilesys-home/webfilesys.html).

