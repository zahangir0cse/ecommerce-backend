FROM openjdk:8
ADD target/ecommerce.jar ecommerce.jar
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "ecommerce.jar"]