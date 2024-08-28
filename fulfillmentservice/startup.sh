#!/bin/sh

# Read Kubernetes metadata and set as environment variables
for file in /var/lib/dynatrace/enrichment/dt_metadata.json; do
  if [ -f "$file" ]; then
    echo "Processing file: $file"
    python3 -c "
import json
import os

with open('$file') as f:
    data = json.load(f)
    for key, value in data.items():
        env_key = key.replace('.', '_')
        print(f'export {env_key}={value}')
" > /tmp/env_vars.sh
    cat /tmp/env_vars.sh
    . /tmp/env_vars.sh
  fi
done

# Set the service name attribute
SERVICE_NAME="service.name=fulfillmentservice"

# Convert underscores back to dots for OpenTelemetry attributes
K8S_RESOURCE_ATTRIBUTES="KUBERNETES_CONTAINER_NAME=fulfullmentservice,k8s.pod.name=${k8s_pod_name:-unknown},k8s.namespace.name=${k8s_namespace_name:-unknown},k8s.node.name=${k8s_node_name:-unknown},k8s.pod.uid=${k8s_pod_uid:-unknown},dt.kubernetes.workload.kind=${dt_kubernetes_workload_kind:-unknown},dt.kubernetes.workload.name=${dt_kubernetes_workload_name:-unknown},dt.kubernetes.cluster.id=${dt_kubernetes_cluster_id:-unknown}"
K8S_RESOURCE_ATTRIBUTES=$(echo $K8S_RESOURCE_ATTRIBUTES | sed 's/_/./g')

# Combine service name and Kubernetes attributes
OTEL_RESOURCE_ATTRIBUTES="$SERVICE_NAME,$K8S_RESOURCE_ATTRIBUTES"

# Print environment variables for debugging
echo "OTEL_EXPORTER_OTLP_ENDPOINT: $OTEL_EXPORTER_OTLP_ENDPOINT"
echo "OTEL_EXPORTER_OTLP_TRACES_PROTOCOL: $OTEL_EXPORTER_OTLP_TRACES_PROTOCOL"
echo "Converted OTEL_RESOURCE_ATTRIBUTES: $OTEL_RESOURCE_ATTRIBUTES"

# Start the Java application
exec java -javaagent:/opentelemetry-javaagent.jar \
  -Dotel.resource.attributes="$OTEL_RESOURCE_ATTRIBUTES" \
  -jar /fulfillmentservice.jar
