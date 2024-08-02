FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY target/frontendservice-0.0.1-SNAPSHOT.jar frontendservice.jar
ENTRYPOINT ["java","-jar","/frontendservice.jar"]