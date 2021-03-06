Retail Micro Service
=========================


Developer Information
---

To launch this service locally via command line run `mvn spring-boot:run`, then connect to `localhost:9001`

To modify / add new configuration profiles add them to the `ssp-configuration-repo` under service as `retail.yml`

To generate API document via command line run `mvn swagger:generate`, then open target/api-docs/api.html

**Docker**

This is tailored around our typical development machines (Windows and Mac) using Docker Toolbox which allows you to run docker locally.  This can be handy for starting up micro-services locally for a virtual environment.


**Building a local DEV image**
- Build: `./mvn clean package docker:build`
- Run: `docker run -p 9001:9001 index.dev.prod.auction.local:5000/ms-ssp-retail:DEV` 

**Using the CI built image**

To use an image built by our CI system the command is nearly identical but without the `:DEV` tag at the end

- Run: `docker run -p 9001:9001 index.dev.prod.auction.local:5000/ms-ssp-retail`

Technologies:
----

  - Java 8 
  - Spring Boot - [Reference](https://spring.io/guides/gs/spring-boot/)
  - Maven - [Build tool - Reference](http://apache.maven.org)
