## About
This is a Java implementation of LDAP authentication with Spring Boot support. Authenticator uses embedded login form of the browser and runs on embedded tomcat comes with Spring Boot.

## Tech Stack
Tech|Version
---|---
Maven|3.2.5
JDK|1.7
Spring Boot|1.4.1.RELEASE
Logback|1.1.7  

## Installation
* Run the following `git` command to clone: `git clone https://github.com/kahramani/ldap-authenticator.git`
* Run the following `maven` command to install dependencies: `mvn install`
* Open project in your editor/ide and follow the `TODO`s to configure ldap server specifications
* Then run the project and open [http://localhost:8080/ldap-authenticator/checkAuth](http://localhost:8080/ldap-authenticator/checkAuth "localhost") in your browser

Note: If you will use ldap server via ssl, you must import certificates of the ldap server to java's keystore. Otherwise, handshake error will happen. 

## License
GNU GPLv3
