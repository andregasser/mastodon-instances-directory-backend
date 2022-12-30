# Running Locally
In case you're using Docker Desktop on Windows and the following error occurse:

Cannot start service rabbitmq: Ports are not available: listen tcp 0.0.0.0:15672

then use:

net stop winnat
docker-compose up ...
net start winnat

# Docker Compose
- Copy file `docker-local/docker-compose-template.yml` into `docker-local/docker-compose.yml` and
  set values for `MONGO_INITDB_ROOT_USERNAME` and `MONGO_INITDB_ROOT_PASSWORD`
- Switch into `docker-local`folder and run `docker-compose up`
- Docker containers should now be up and running

# Backend Application



