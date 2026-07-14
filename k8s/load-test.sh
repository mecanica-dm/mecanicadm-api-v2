#!/bin/bash
SERVICE_URL="http://localhost:80/actuator/health"
REQUESTS=5000
CONCURRENCY=50

echo "Sending $REQUESTS requests with $CONCURRENCY concurrent connections to $SERVICE_URL"

seq 1 $REQUESTS | xargs -P $CONCURRENCY -I {} curl -s -o /dev/null -w "HTTP %{http_code} - %{time_total}s\n" "$SERVICE_URL"

echo "Done."
