# user-registration-service
Spring REST service to handle user registration


How to run
----------
This is a simple maven spring boot application and it needs to import and run as a
simple java application from UserRegistrationApplication class

or

From application root directory create jar file with maven command:
- mvn package

Then run command:
- java -jar target/user-registration-service-0.0.1-SNAPSHOT.jar

Also, possible to run from script file:

For Windows run:
- start.cmd

For Linux system run:
- ./start.sh

Usage
------
1) from command line:
    curl -d '{"firstName": "First Name", "lastName": "Last Name", "userName": "User Name", "password": "pass1234"}' -H "Content-Type: application/json" -X POST  http://localhost:8080/userservice/register
2) use any REST client:
    - POST request to http://localhost:8080/userservice/register with body {"firstName": "First Name", "lastName": "Last Name", "userName": "User Name", "password": "pass1234"}
3) run unit tests


Notes
-----
- The service implemented without a database and stores users in map
- Validation constraints putted with some logical assumptions
- Assumed that all fields are mandatory