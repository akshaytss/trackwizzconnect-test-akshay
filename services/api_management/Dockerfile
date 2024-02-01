# Use the official OpenJDK 17 base image
FROM openjdk:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file into the container at /app
COPY build/libs/*.jar /app/api_management.jar

# Expose the port that the application will run on
EXPOSE 7002

# Command to run the application
CMD ["java", "-Dspring.profiles.active=dev", "-jar", "api_management.jar"]