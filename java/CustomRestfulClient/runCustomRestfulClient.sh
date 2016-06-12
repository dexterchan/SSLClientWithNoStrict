export title=testing
export description=funny description
export host=192.168.88.1
java -jar ./out/CustomRestfulClient.jar -MODE POST -MAXTRY 3 -ENV_VAR title,description -URL https://$host:443/todo/api/v1.0/tasks
