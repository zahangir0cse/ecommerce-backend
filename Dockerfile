FROM openjdk:8
ADD target/spring-boot-docker.jar ecommerce.jar
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "ecommerce.jar"]