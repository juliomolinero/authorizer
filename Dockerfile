FROM adoptopenjdk/openjdk11:latest

VOLUME /tmp
COPY target/ /app/
RUN mv /app/nubank-authorizer-1.0.jar /app/nubank.jar
ADD src/main/resources/log4j.properties /app/log4j.properties

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom -Dlog4j.configuration=classpath:file:/app/log4j.properties", "-jar", "/app/nubank.jar"]