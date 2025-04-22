# Use a base image with Java pre-installed
FROM eclipse-temurin:17-jdk as build
VOLUME /tmp

# Copy the Gradle wrapper files
COPY gradlew .
COPY gradle gradle

# Copy the build configuration files
COPY settings.gradle .
COPY build.gradle .

# Copy the source code
COPY src src

# Download and cache Gradle dependencies
RUN chmod +x gradlew
RUN ./gradlew --version
RUN ./gradlew dependencies --no-daemon

# Build the application
RUN ./gradlew clean build --no-daemon

FROM eclipse-temurin:17-jdk
VOLUME /tmp

WORKDIR /app

# Copy the JAR file into the container
COPY --from=build build/libs/application.jar application.jar
COPY ./startup.sh startup.sh

RUN chmod +x startup.sh

# Expose the port that your application listens on
EXPOSE 8080

# Set the command to run your application
CMD ["./startup.sh"]
