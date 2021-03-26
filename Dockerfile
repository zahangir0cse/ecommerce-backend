FROM openjdk:8
ADD target/ecommerce.jar ecommerce.jar
EXPOSE 8086
ENTRYPOINT ["java", "-jar", "ecommerce.jar"]