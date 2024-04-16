FROM openjdk:17-jdk-slim
EXPOSE 7050
COPY --from=build /build/libs/EcoTS-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar","app.jar"]