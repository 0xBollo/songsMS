# songsMS

## Build And Run Locally Without Docker Compose
Requirements: JDK 17, Maven, Git, Docker
1. Open a terminal and navigate to a directory where you want to save the project.\
`cd <path-to-your-directory>`
2. Clone the repository.\
`git clone https://github.com/0xBollo/songsMS.git`
3. Navigate to the newly created project directory.\
`cd songsMS`
4. Make sure the Docker daemon is running.
5. Start the local Postgres instance for the authentication service.\
`docker run -d --rm -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=auth_service -p 5433:5432 postgres:16.0-alpine3.18`
6. Start the local Postgres instance for the song service.\
`docker run -d --rm -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=song_service -p 5434:5432 postgres:16.0-alpine3.18`
7. Start the local Mongo instance for the statistics service.\
`docker run -d --rm -p 27018:27017 mongo:7.0.2-jammy`
8. Start the local RabbitMQ instance.
`docker run -d --rm --name songs_rabbit -e RABBITMQ_DEFAULT_USER=guest -e RABBITMQ_DEFAULT_PASS=guest -p 15672:15672 -p 5672:5672 rabbitmq:3.12.7-management`
9. Build the project.\
`mvn clean package`
10. Restart the RabbitMQ container.\
`docker restart songs_rabbit`
11. Open a new terminal window and start the discovery server.\
`java -jar <path-to-your-directory>/songsMS/discovery-server/target/discovery-server-1.0-SNAPSHOT.jar`
12. Open a new terminal window and start the api gateway.\
`java -jar <path-to-your-directory>/songsMS/api-gateway/target/api-gateway-1.0-SNAPSHOT.jar`
13. Open a new terminal window and start the authentication service.\
`java -jar <path-to-your-directory>/songsMS/auth-service/target/auth-service-1.0-SNAPSHOT.jar`
14. Open a new terminal window and start the song service.\
`java -jar <path-to-your-directory>/songsMS/song-service/target/song-service-1.0-SNAPSHOT.jar`
15. Open a new terminal window and start the statistics service.\
`java -jar <path-to-your-directory>/songsMS/statistics-service/target/statistics-service-1.0-SNAPSHOT.jar`

## Run Locally With Docker Compose
Requirements: Docker, Docker Compose
1. Download the `docker-compose.yml` from the root-directory of this repository and move it to a directory where
you want to run the project.
2. Open a terminal and navigate to this directory.\
`cd <directory-with-docker-compose.yml>`
3. Start the container setup.\
`docker-compose up -d`


