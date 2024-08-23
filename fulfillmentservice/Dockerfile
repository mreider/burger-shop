FROM openjdk:17-jdk-slim
VOLUME /tmp
ADD https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v1.10.0/opentelemetry-javaagent.jar opentelemetry-javaagent.jar
COPY target/fulfillmentservice-0.0.1-SNAPSHOT.jar fulfillmentservice.jar
COPY startup.sh /startup.sh

# Install jq for JSON parsing
RUN apt-get update && apt-get install -y jq

# Make the startup script executable
RUN chmod +x /startup.sh

ENTRYPOINT ["/startup.sh"]
