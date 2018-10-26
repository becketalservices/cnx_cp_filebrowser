# A stylish web-based file manager for IBM Connections Component Pack Customizer

This project will add a simple web-based file manager to your Component Pack installation so that you can simply browse your customizer files. This is helpful wen you can not siple upload files using FTP to your component pack customizer file store as desicribed by IBM https://www.ibm.com/support/knowledgecenter/en/SSYGQH_6.0.0/admin/customize/custom_customizer_props_include_files.html

This solution is based on [File Browser](https://filebrowser.github.io/)

## Getting Started

Make shure you already have Comonent Pack up and running.

## Installation

You need the name of the Storeage Class (in my case: aws-efs) for creating a new persistant volume. This volume will host the database requied by filebrowser.

run
```
helm install https://github.com/becketalservices/cnx_cp_filebrowser/releases/download/v1.0.0/filebrowser-1.0.0.tgz --name filebrowser --set storageClassName=aws-efs --namespace connections
```

You can then simply add the tool by adding the usual Proxy Rules inside the HTTP Server config:

```
ProxyPass "/filebrowser" "http://master_node_host_name:31675/filebrowser" 
ProxyPassReverse "/filebrowser" "http://master_node_host_name:31675/filebrowser"
```

The default login is admin:admin  
I recommend to change this and add some additional users.

There is no integration with the Connections Authentication. It is just a stand alone web-based file manager that acts on the same storage as the IBM Connections Component Pack Customizer.


