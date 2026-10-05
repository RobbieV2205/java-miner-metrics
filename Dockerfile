FROM openjdk:28-ea-slim
ARG JAR_FILE=target/*.jar
COPY ./target/java-miner-metrics-0.1.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]