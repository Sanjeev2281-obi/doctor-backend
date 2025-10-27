# Step 1: Use OpenJDK 17
FROM openjdk:17-jdk-slim

# Step 2: Set working directory
WORKDIR /app

# Step 3: Copy jar file into container
COPY target/backend-0.0.1-SNAPSHOT.jar app.jar

# Step 4: Expose port
EXPOSE 8080

# Step 5: Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
