FROM ubuntu:latest AS BUILD

RUN apt-get update
RUN apt-get install -y openjdk-17-jdk


COPY . .

RUN apt-get install -y maven
RUN mvn clean install


EXPOSE 8080

COPY --from=BUILD /target/todolist-1.0.0.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]