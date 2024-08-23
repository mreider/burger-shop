#!/bin/sh

# Read Kubernetes metadata and set as environment variables
for file in /var/lib/dynatrace/enrichment/dt_metadata.json; do
  if [ -f "$file" ]; then
    echo "Processing file: $file"
    jq -r 'to_entries | map("\(.key | gsub(\"\\.\", \"_\"))=\(.value | tostring)") | .[]' $file | sed 's/^/export /' > /tmp/env_vars.sh
    cat /tmp/env_vars.sh
    . /tmp/env_vars.sh
  fi
done

# Print environment variables for debugging
echo "OTEL_EXPORTER_OTLP_ENDPOINT: $OTEL_EXPORTER_OTLP_ENDPOINT"
echo "OTEL_RESOURCE_ATTRIBUTES: $OTEL_RESOURCE_ATTRIBUTES"

# Convert underscores back to dots for OpenTelemetry attributes
OTEL_RESOURCE_ATTRIBUTES="k8s.pod.name=${k8s_pod_name:-unknown},k8s.namespace.name=${k8s_namespace_name:-unknown},k8s.node.name=${k8s_node_name:-unknown},k8s.pod.uid=${k8s_pod_uid:-unknown},dt.kubernetes.workload.kind=${dt_kubernetes_workload_kind:-unknown},dt.kubernetes.workload.name=${dt_kubernetes_workload_name:-unknown},dt.kubernetes.cluster.id=${dt_kubernetes_cluster_id:-unknown}"
OTEL_RESOURCE_ATTRIBUTES=$(echo $OTEL_RESOURCE_ATTRIBUTES | sed 's/_/./g')

# Print converted attributes for debugging
echo "Converted OTEL_RESOURCE_ATTRIBUTES: $OTEL_RESOURCE_ATTRIBUTES"

# Start the Java application
exec java -javaagent:/opentelemetry-javaagent.jar \
  -Dotel.resource.attributes="$OTEL_RESOURCE_ATTRIBUTES" \
  -jar /fulfillmentservice.jar
