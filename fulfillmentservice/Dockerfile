FROM openjdk:17-jdk-slim
VOLUME /tmp
ADD https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v1.10.0/opentelemetry-javaagent.jar opentelemetry-javaagent.jar
COPY target/fulfillmentservice-0.0.1-SNAPSHOT.jar fulfillmentservice.jar

ENTRYPOINT ["java", "-javaagent:/opentelemetry-javaagent.jar", "-Dotel.javaagent.debug=true", "-jar", "/fulfillmentservice.jar"]