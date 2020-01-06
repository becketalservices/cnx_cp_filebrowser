# A stylish web-based file manager for IBM Connections Component Pack Customizer

This project will add a simple web-based file manager to your Component Pack installation so that you can simply browse your customizer files. This is helpful when you can not simple upload files using FTP to your component pack customizer file store as desicribed by HCL [Managing file resources used in the "include-files" property](https://help.hcltechsw.com/connections/v65/admin/customize/custom_customizer_props_include_files.html) 

This solution is based on [File Browser](https://filebrowser.github.io/)

**You can also check my 2nd solution based on [WebFileSys](https://github.com/becketalservices/cnx_cp_filebrowser/tree/webfilesys)**

## Getting Started

Make sure you already have Component Pack up and running.

## Installation

You need the name of the Storeage Class (Kubernetes default: default) for creating a new persistant volume. This volume will host the database required by filebrowser. You must specify the Storage Class. The helm chart default value _anygthing_ will probably not work.

run on your kubernetes installation host or master:
```
helm install https://github.com/becketalservices/cnx_cp_filebrowser/releases/download/v2.0.0/filebrowser-2.0.0.tgz \
--name filebrowser --set storageClassName=default --namespace connections
```

You can then simply add the tool by adding the usual Proxy Rules inside the HTTP Server config:

```
ProxyPass "/filebrowser" "http://master_node_host_name:31675/filebrowser" 
ProxyPassReverse "/filebrowser" "http://master_node_host_name:31675/filebrowser"
```

The default login is admin:admin  
I recommend to change this and add some additional users.

There is no integration with the Connections Authentication. It is just a stand alone web-based file manager that acts on the same storage as the IBM Connections Component Pack Customizer.

## Configuration

Currently you can configure it only during installation or by modifying the deployment.yaml file.  

you can set this options during installation:
* storageClassName - the storage class for the pvc of the database - Defaults to `anything`
* baseURL - the base URL. Defaults to `filebrowser`
* service.nodePort - the port the service is listening to. Defaults to `31675`
* filestore - the name of the pvc to serve. Defaults to `customizernfsclaim`

