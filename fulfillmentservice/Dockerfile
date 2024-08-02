FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY target/fulfillmentservice-0.0.1-SNAPSHOT.jar fulfillmentservice.jar
ENTRYPOINT ["java","-jar","/fulfillmentservice.jar"]