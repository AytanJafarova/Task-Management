# Use Alpine Linux base image
FROM alpine:latest

# Install OpenJDK 21 manually
RUN apk --no-cache add openjdk21

# Create a group and user named "app"
RUN addgroup -S app && adduser -S app -G app

# Switch to the "app" user
USER app

# Copy the JAR file into the container
COPY ./target/*.jar app.jar

# Set the entry point to run the JAR file when the container starts
ENTRYPOINT ["java", "-jar", "/app.jar"]
