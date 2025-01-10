#!/bin/bash

# Set the Docker image and necessary parameters
DOCKER_IMAGE="al2023-graalvm21:native-web"
DOCKER_PLATFORM="linux/amd64"
WORK_DIR=$(pwd)
M2_DIR="$HOME/.m2"
MVN_COMMAND="./mvnw clean -Pnative package -DskipTests"
DEPLOY_COMMAND="sam deploy"

# Run the Docker command for building the project
echo "Running Docker container for building the project..."

docker run --platform $DOCKER_PLATFORM \
    -it \
    -v "$WORK_DIR":"$WORK_DIR" \
    -w "$WORK_DIR" \
    -v "$M2_DIR":/root/.m2 \
    $DOCKER_IMAGE \
    bash -c "$MVN_COMMAND"

# Check if the Maven build was successful
if [ $? -eq 0 ]; then
    echo "Build completed successfully. Proceeding to deployment..."
else
    echo "Build failed. Exiting."
    exit 1
fi

# Run the deployment command
echo "Running the deployment command..."
$DEPLOY_COMMAND

# Check if the deployment was successful
if [ $? -eq 0 ]; then
    echo "Deployment completed successfully."
else
    echo "Deployment failed. Exiting."
    exit 1
fi
