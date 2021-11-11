# blackswan-task

Used h2 in-memory db for simplicity

1. create package by running mvn clean package
2. create docker image by running "docker build -t java-docker-api ." in the project folder
3. run docker image in a container "docker container run -p 8080:8080 java-docker-api"

endpoint is available under http://localhost:8080/api/


