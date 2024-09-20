FROM eclipse-temurin:22-alpine

COPY target/ipl-assignment1-0.1.jar app.jar
EXPOSE 8085
ENV KAFKA_HOST=host.docker.internal:9094
ENV REDIS_HOST=host.docker.internal
ENV MYSQL_URL=jdbc:mysql://host.docker.internal:1000/ipl_match
ENTRYPOINT ["java", "-jar", "-Dspringcd.kafka.bootstrap-servers=${KAFKA_HOST}", "-Dspring.data.redis.host=${REDIS_HOST}", "-Dspring.datasource.url=${MYSQL_URL}", "/app.jar"]