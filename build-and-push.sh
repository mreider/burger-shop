#!/bin/bash

# Function to update image version in YAML file
update_image_version() {
  local file=$1
  local service=$2
  local current_version=$(grep "image: mreider/$service:" $file | awk -F: '{print $3}')
  local new_version=$((current_version + 1))
  sed -i "s/image: mreider\/$service:$current_version/image: mreider\/$service:$new_version/" $file
  echo $new_version
}

# Update image versions in YAML files
new_fulfillmentservice_version=$(update_image_version "k8s/fulfillmentservice.yaml" "fulfillmentservice")
new_orderservice_version=$(update_image_version "k8s/orderservice.yaml" "orderservice")

# Build and push fulfillmentservice Docker image
cd fulfillmentservice/
mvn clean package -DskipTests
sudo -S docker buildx build --platform linux/amd64,linux/arm64 -t mreider/fulfillmentservice:$new_fulfillmentservice_version --push .

# Build and push orderservice Docker image
cd ../orderservice/
mvn clean package -DskipTests
sudo -S docker buildx build --platform linux/amd64,linux/arm64 -t mreider/orderservice:$new_orderservice_version --push .

cd ../k8s/
kubectl apply -f fulfillmentservice.yaml
kubectl apply -f orderservice.yaml

# Commit changes to GitHub
git add .
git commit -m "version $new_fulfillmentservice_version"
git push origin main
