set title=testing
set description=funny description
java -jar target\CustomRestfulClient-0.0.1.jar -MODE POST -MAXTRY 3 -ENV_VAR title,description -URL https://localhost:443/todo/api/v1.0/tasks