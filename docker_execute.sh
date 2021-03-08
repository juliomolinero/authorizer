# This will execute and remove the container when is done
# IMPORTANT!, no matter if you want to use another volume "utils" folder MUST exist
# or the application won't be able to write results
docker run -i --rm \
  --volume $(pwd)/utils/:/utils/ \
  nubank-auth-svc:latest /utils/input_operations
