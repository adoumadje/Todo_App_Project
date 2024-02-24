# Stage 1: Build the application
FROM maven:3.9.5 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean install

# Stage 2: Run the application
FROM openjdk:17-alpine
WORKDIR /app
COPY --from=build /app/target/todo-app-0.0.1-SNAPSHOT.jar ./todo-app.jar
CMD ["/bin/sh", "-c", "while ! nc -z todo_app_database_c0 5432; do sleep 1; done && java -jar todo-app.jar"]