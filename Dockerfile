# 1. Use a lightweight Java 21 image
FROM eclipse-temurin:21-jdk-alpine

# 2. Set the working directory inside the container
WORKDIR /app

# 3. Copy the compiled JAR file into the container
# (We will create this jar in the next step)
COPY target/*.jar app.jar

# 4. Expose the port the app runs on
EXPOSE 8080

# 5. Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]