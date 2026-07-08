FROM eclipse-temurin:17-jdk-jammy
COPY target/reportes-service-0.0.1-SNAPSHOT.jar reportes-service-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/reportes-service-0.0.1-SNAPSHOT.jar"]
