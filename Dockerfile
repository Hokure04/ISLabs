FROM openjdk:17-oracle

LABEL authors="jaba"

WORKDIR /app

COPY target/ .

CMD ["java", "-jar", "FistLab-1.0.0.jar"]