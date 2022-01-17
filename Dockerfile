FROM adoptopenjdk:11-jre-hotspot

RUN mkdir /app

WORKDIR /app

ADD ./api/target/payment-api-1.0-SNAPSHOT.jar /app

EXPOSE 8080

CMD ["java", "-jar", "payment-api-1.0-SNAPSHOT.jar"]