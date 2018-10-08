# Basic recruiter [![Build Status](https://travis-ci.org/mamunsrdr/basic-recruiter.svg?branch=master)](https://travis-ci.org/mamunsrdr/basic-recruiter)
[The basic recruiting process problem](https://github.com/mamunsrdr/basic-recruiter/wiki/Problem)'s solution

### What's inside
* jdk 8
* spring boot 2.0.5

### Project structure
The project source code structure
```
com.heavenhr.recruiter
    - app
        command
        config
        dto
        type
    - controller
    - event
        publisher
        subscriber
        topic
    - persistence
        entity
        repo
    - service
```
###Sample commands
**Clean:** `./gradlew build`  
**Build:** `./gradlew build`    
**Test:** `./gradlew test`  
**Run:** `./gradlew bootRun`    


###Play with REST api
With **Swagger-UI** (after run): [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)  
With **Postman** (after run): [api_postman_collection.json](https://raw.githubusercontent.com/mamunsrdr/basic-recruiter/master/api_postman_collection.json)


###Peek into **db2 console**:  
Url: [http://localhost:8080/h2](http://localhost:8080/h2)  
JDBC URL: `jdbc:h2:mem:recruiter`  
User Name: `heavenhr`  
Password: `1234`

###Application status change notification
```
c.h.r.e.subscriber.JobAppStatusEventSub - *************************************************
c.h.r.e.subscriber.JobAppStatusEventSub - Status change event received: JobAppStatusChangeEvent(id=2, oldStatus=HIRED, newStatus=HIRED)
c.h.r.e.subscriber.JobAppStatusEventSub - *************************************************
```