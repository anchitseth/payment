FROM openjdk:8
ADD target/payment-microservice.jar payment-microservice.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "payment-microservice.jar"]