## BookingApp

#### Synopsis

This project has been implemented for providing a REST API so that we can create/update/retrieve and delete BOOKINGs as 
well as retrieve/create/delete BLOCKs. 
According to spec a booking is when a guest selects a start and end date and submits a reservation on a property while 
a block is when the property owner selects a start and end date where no one can make a booking on the dates within the date range.
Also api for fetching orders within a time period is also provided. 
  
Technology Details  
- This projects is based on Spring Boot that makes it easy to create stand-alone applications that you can "just run".
- It includes an embedded H2 database (memory based - MySQL mode).  
The console of H2 can be accessed by url: [http://localhost:8080/h2](http://localhost:8080/h2)  
where you can use the following to entry the interface of it  
username: sa  
password:  
jdbcUrl: jdbc:h2:mem:bookingapp

The above properties can be changed via the [application.properties](src/main/resources/application.properties).   
- It integrates with flyway for data migration. This is used only for running the initial script to create the db schema for our project. 
- If you need the storage to be persistent you can also switch to the file based H2 db by changing jdbc url into something like jdbc:h2:~/bookingapp
- It uses Groovy & the Spock Framework for the Unit & Functional testing. 

### Coding Standards/Quality

Generally I tried to follow as much as possible a separation of layers for my implementation in order to keep the structure of our project organized, readable and good looking. So by starting from the bottom which is the DB layer towards up (API) you can notice the following:
   
a) Domain models/entities (or in other words POJOS) that are used to link db entities into java objects.
b) Repositories that are the interfaces used to interact with the database relying on the JpaRepository of Spring Data. 
c) On top of repositories we can find the DAO which is another layer giving us the option of restricting the available operations to the db entities.
d) Dao's can be injected in the layer of Services. Services are business logic abstractions mainly used by other services or the api's.
e) Our rest api. Functionality for calculations or any logic normally should not happen here but in services. This is why other than some basic validation of the request input nothing else is done on their own. Services components can be injected here.
f) The API's DTOs (Data Transfer Objects) which have the role of request/response objects. 
   
For the static code analysis of the java source code these [java rules](rules/checkstyle.xml) are applied as part of the checkstyle gradle plugin. Any exclusions for specific cases can be included in the [suppressions file](rules/checkstyle-suppressions.xml). 
The checkstyle runs as part of the build process, nevertheless you can run it with the following commands:

    ./gradlew clean checkstyleMain 
    
After checkstyle runs, a report file is being created [checkstyle report file](builds/checkstyleReports/main.html)

For the static code analysis of the groovy test source code these [groovy rules](rules/codenarc.groovy) are applied as part of the codenarc gradle plugin.  
The codenarc runs as part of the build process, nevertheless you can run it with the following commands:

    ./gradlew clean codenarcTest
    
After codenarc runs, a report file is being created.  
For unit test you can find it here: [codenarc tests report file](builds/reports/codenarc/test.html)

#### Build the project

The following command will compile java and create the executable jar file [bookingapp-1.0.0.jar](build/libs/bookingapp-1.0.0.jar).

    ./gradlew clean build  
    
#### Run the application with bootRun

    ./gradlew clean bootRun  
    
#### Run the application using the executable jar file

The following command will run the application:

    java -jar ./build/libs/bookingapp-1.0.0.jar
    
#### API's:

API's as defined in the [BookingApi](src/main/java/com/market/bookingapp/api/BookingApi.java):
 
- GET localhost:8080/api/bookings, retrieves all bookings 
- GET localhost:8080/api/bookings/{ID}, retrieve a specific booking (if not found returns respective error message)
- POST localhost:8080/api/bookings, saves a booking in db (a check against other time allocation is done in
  order to avoid conflict. If other blocks or (active) bookings exist with an overlapping date range then a respective error
  is thrown)
  - PUT localhost:8080/api/bookings, updates an existing booking in db. In case it does not exist, throws the respective error
  This update method api can be used for making a booking from ACTIVE -> CANCELLED or the other way around. Once a booking is becoming 
  from cancelled to active or when it is active and changes its date range a check against other time allocation is done in 
  order to avoid conflict. If other blocks or (active) bookings exist with an overlapping date range then a respective error 
  is thrown 
- DELETE localhost:8080/api/bookings/{ID}, deletes a specific booking (if not found returns respective error message)

- GET localhost:8080/api/blocks, retrieves all blocks
- POST localhost:8080/api/blocks, saves a block in db (a check against other time allocation is done in
  order to avoid conflict. If other blocks or (active) bookings exist with an overlapping date range then a respective error
  is thrown)
- DELETE localhost:8080/api/blocks/{ID}, deletes a specific block (if not found returns respective error message)

Date parameters currently are given in a specific format "yyyy-MM-dd HH:mm:ss" (e.g. 2022-03-02 12:57:41) and dates are 
considered to be in the timezone of "Europe/Athens". 
Of course these are configurable properties that can be easily changed via this [environment.default.properties](src/main/resources/environment.default.properties)] 

[You can find a postman collection with samples of those api's at the [Bookingapp.postman_collection.json](postman/Bookingapp.postman_collection.json)]   

### Test Automation:
I implemented Unit tests as part of this project, and I used the Spock framework which gives us the option of creating the automated tests cases with much flexibility in the Groovy language. 
This type of tests exercise the smallest piece of testable software in the application to determine whether it behaves as expected.These are tests at the lowest level and they test methods in Java classes. 
Tests run as part of the build process, nevertheless you can run them with the following command:
          
              ./gradlew clean test
              
#### Author  
Nick Drakopoulos (nickdrakop@gmail.com)


  
