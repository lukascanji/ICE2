##I DONT KNOW THE TYPE OF FILE I NEED TO CREATE -- I COPIED THE CODE BUT DONT HAVE

##______________________________
##build stage
##______________________________
##base image
FROM gradle:8-jdk17-alpine AS builder

#copy the application files from the host machine to the image filesystem
COPY --chown=gradle:gradle . /home/gradle/src

#set the directory within the image for executing future commands
WORKDIR /home/gradle/src

# run gradle build within the container - not the host machine
RUN gradle build -x -test

##dockerfile must be capitalized, and located within the product-service directory

##______________________________
##package stage
##______________________________

#base image
FROM openjdk:17-alpine

RUN mkdir /app

COPY --from=builder /home/gradle/src/build/libs/*.jar /app/product-service.jar

##check in application.properties file for credentials
ENV MONGO_DB_USERNAME=mongoadmin \
    MONGO_DB_PWD=password

##probably want to create a word file with every service we create with the service name and port
EXPOSE 8084




ENTRYPOINT ["java", "-jar", "/app/product-service.jar"]



## docker build -t product-service:1.0

##this is not working for me ^^^^^^^^ (perhaps i can have this work by doing the monday code first)

##after running the above code you should be able to build the dockerfile and it should show up in the
##docker dashboard -> images as product-service

##now create a docker compose file -> this will be done in the microservices-parent and only needs to be done once

##regular dockerfiles will need to be in the product-service and created for each microservice