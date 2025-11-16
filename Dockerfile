# ---------- Stage 1: build ----------
FROM maven:3.9-openjdk-17 AS build
WORKDIR /workspace

COPY pom.xml .

COPY src ./src
RUN mvn clean package -DskipTests

# ---------- Stage 2: runtime ----------
FROM openjdk:17-slim
WORKDIR /app

COPY --from=build /workspace/target/*.jar ./app.jar

ENV JAVA_OPTS=""

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/app.jar"]
