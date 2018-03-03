# Spring boot sample app


Sample app using [Spring Boot](http://projects.spring.io/spring-boot/).

## Requirements

For building and running the application you need:

- [JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Maven 3](https://maven.apache.org)

## Running the application locally

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the `com.jmwangi.bankaccount.BankAccountApplication` class from your IDE.

Alternatively you can use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:

```shell
mvn spring-boot:run
```

There application will run at 

``
http://localhost:8080
``

You can change the port at which the application runs by going to the resources folder and adding the following to the `application.properties` file

``
server.port = PORT
``

## API Docs

To view the api docs you can go to

`` 
http://localhost:PORT/swagger-ui.html
``

## Running tests
You can use Maven to run tests
```shell
mvn test
```

