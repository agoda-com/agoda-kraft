#!/bin/bash
SECONDS=0

# Config
INTERVAL=30
TIMEOUT=600

# Validate SONATYPE credentials
if [ -z "$SONATYPE_USERNAME" ] || [ -z "$SONATYPE_PASSWORD" ]; then
    echo "SONATYPE_USERNAME and SONATYPE_PASSWORD environment variables must be set"
    exit 1
fi
AUTH_TOKEN=$(echo "$SONATYPE_USERNAME:$SONATYPE_PASSWORD" | base64)

# Initialize variables
NAMESPACE=""
BUNDLE_NAME=""
BUNDLE_VERSION=""

# Parse command-line arguments
while [ $# -gt 0 ]; do
  case "$1" in
    --namespace)
      NAMESPACE="$2"
      shift 2
      ;;
    --bundle-name)
      BUNDLE_NAME="$2"
      shift 2
      ;;
    --bundle-version)
      BUNDLE_VERSION="$2"
      shift 2
      ;;
    *)
      echo "Unknown option: $1"
      exit 1
      ;;
  esac
done

# Validate required arguments
if [ -z "$NAMESPACE" ] || [ -z "$BUNDLE_NAME" ] || [ -z "$BUNDLE_VERSION" ]; then
  echo "Usage: $0 --namespace <namespace> --bundle-name <bundle_name> --bundle-version <bundle_version>"
  exit 1
fi

URL="https://central.sonatype.com/api/v1/publisher/published?namespace=$NAMESPACE&name=$BUNDLE_NAME&version=$BUNDLE_VERSION"

while [ $SECONDS -lt $TIMEOUT ]; do
    RESPONSE=$(curl --header "Authorization: Bearer ${AUTH_TOKEN}" -s $URL)
    echo $RESPONSE
    if echo "$RESPONSE" | grep -q '"published":true'; then
        echo "Successfully Published!"
        exit 0
    else
        echo "Published is not true yet. Checking again in $INTERVAL seconds."
    fi
    sleep $INTERVAL
done

if [ $SECONDS -ge $TIMEOUT ]; then
    echo "Timeout reached. Exiting after $TIMEOUT seconds."
fi
exit 1