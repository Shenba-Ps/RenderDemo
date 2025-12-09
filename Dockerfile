FROM openjdk:8
EXPOSE 8080
ADD target/render-demo.jar render-demo.jar
ENTRYPOINT ["java","-jar","/render-demo.jar"]