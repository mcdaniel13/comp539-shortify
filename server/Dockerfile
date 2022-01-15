FROM adoptopenjdk/openjdk11:alpine-jre
ARG JAR_FILE=target/server-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE}  docker-springboot.jar
WORKDIR docker-springboot
COPY  service-account.json service-account.json
EXPOSE 8080
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-jar","/docker-springboot.jar"]
