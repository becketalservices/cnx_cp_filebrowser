File Manager based on WebFileSys
================================

The solution using File Browser is good but it lacks the possibility to unzip a uploaded file. So I searched for an alternative solution.
I found [WebFileSys the unviversal web-based filemanager](http://www.webfilesys.de/webfilesys-home/index.html).

This solution is build on tomcat and has no Docker image available. So we need to create our own.

#1 Clone or download git repo


#2 Create Docker image
I did not include the source files into this repository, you you need to download and extract them first.

1. Download webfilesys 
2. Extract the contents:
```
mkdir webfilesys
unzip webfilesys-*.zip -d webfilesys
mkdir webfilesys/webfilesys_war
unzip webfilesys/webfilesys.war -d webfilesys/webfilesys_war

```
3. In case you want to modify the default user / passwords / documentRoot
modify the file webfilesys/webfilesys\_war/wEB-INF/users.xml.unix
an example is in the root directory where a 2nd user exists and the documentRoot is /mnt/files
4. run ./build.sh

#3 Test your image
You can test your new docker image:

1. create directories config and files
2. run ./run.sh
3. Access the container on port :8888. 
The default username is admin / topsecret

#4 Upload to our Docker registry
You need to tag and then upload the image
```
# Path to your registry. e.g.: cpcontainerregistry.azurecr.io
registry=<registry>

# In case you have multiple versiony, you can change this.
tag=latest 

# Tag the local image
docker tag webfilesys:$tag $registry/webfilesys:$tag

# Push to docker registry
docker push $registry/webfilesys:$tag

```

#5 Use Helm to install the final solution

You need the name of the Storeage Class if you do not want to use the default for creating a new persistant volume. This volume will host the  users.xml where your user accounts are stored.

As you uploaded the the docker image to your private registry, the registry name is also necessary.

run on your kubernetes installation host or master:

```
helm install ./helm/webfilesys \
  --name webfilesys \
  --namespace connections \
  --set image.repository=<container registry> \ 
  --set storageClassName=<storeage class>

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

#6 Configuration

Currently there are no configurations available beside that you can manage users.


