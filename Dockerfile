# Use an official OpenJDK runtime as a parent image
FROM amazoncorretto:21-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from your host machine to the container
COPY build/libs/discord-bot-fat.jar /app/discord-bot.jar

## Expose the port your Spring application is listening on
#EXPOSE 8080

# Define the command to run your application
ENTRYPOINT ["java", "-jar", "discord-bot.jar"]