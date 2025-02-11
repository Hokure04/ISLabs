FROM openjdk:17-oracle

LABEL authors="jaba"

WORKDIR /app


COPY target/*.jar app.jar

CMD ["java", "-jar", "app.jar"]