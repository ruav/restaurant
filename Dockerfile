#FROM gradle:4.10.2-jdk8-alpine as builder
#
#ARG VERSION
#ENV SERVICE_DIR /opt/service
#
#COPY . ${SERVICE_DIR}
#WORKDIR ${SERVICE_DIR}
#USER root
#RUN mvn clean package
#
#FROM openjdk:8-jdk-alpine
#ARG VERSION
#ENV SERVICE_DIR /opt/service
#ENV JAR_EXEC elastic-search-sync-${VERSION}.jar
#
#COPY --from=builder ${SERVICE_DIR}/build/libs/${JAR_EXEC} /opt/${JAR_EXEC}
#ENTRYPOINT java -Dspring.profiles.active=postgres -jar /opt/${JAR_EXEC}
##ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom ","-jar","/app.jar"]



#FROM maven:3.3-jdk-8-onbuild
#
#ENV SERVICE_DIR /opt/service
##
#COPY . ${SERVICE_DIR}
#WORKDIR ${SERVICE_DIR}
#
#FROM java:8
#COPY --from=0 /usr/src/app/target/restaurant-1.0-SNAPSHOT.jar /opt/restaurant.jar
#CMD ["java","-jar","/opt/demo.jar"]


FROM java:8
ARG VERSION
EXPOSE 8090
#ENV JAR_EXEC restaurant-${VERSION}.jar
ADD /target/zabei-server-0.0.1-SNAPSHOT.jar zabei.jar
ENTRYPOINT ["java", "-jar","zabei.jar"]