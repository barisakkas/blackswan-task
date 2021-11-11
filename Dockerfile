FROM openjdk
WORKDIR /application

COPY target/spring-boot-rest-api-tutorial-0.0.1-SNAPSHOT.jar /application/app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]