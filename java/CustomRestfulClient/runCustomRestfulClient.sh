export title=testing
export description=funny description
export host=192.168.88.1
#java -jar ./target/CustomRestfulClient-0.0.1.jar --name="Spring" -MODE POST -MAXTRY 3 -ENV_VAR title,description -URL https://$host:443/todo/api/v1.0/tasks

java -jar ./target/CustomRestfulClient-0.0.1.jar --name="Spring" -RUNTEMPL ToDoWebActionTemplate -ACTION insert
