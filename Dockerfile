FROM eclipse-temurin:17-jdk-alpine
EXPOSE 8080
ADD target/render-demo.jar render-demo.jar
ENTRYPOINT ["java","-jar","/render-demo.jar"]