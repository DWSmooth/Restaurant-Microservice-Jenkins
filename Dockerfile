FROM openjdk:17-oracle
    LABEL MAINTAINER = "MegaBytes"
    # for volume mapping from container to host OS
    VOLUME /tmp
    # adding jar file to image
    ADD restaurant-microservice/target/restaurant-microservice-0.0.1-SNAPSHOT.jar app.jar
    # run this in command line
    CMD [ "java", "-jar", "app.jar" ]
    EXPOSE 8082
