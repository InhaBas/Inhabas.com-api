FROM eclipse-temurin:17-jre-jammy AS spring

ARG JAR_FILE=./resource-server/build/libs/resource-server-0.0.1-SNAPSHOT.jar

EXPOSE 8080
EXPOSE 8081

COPY ${JAR_FILE} app.jar

CMD ["java","-jar", "app.jar"]