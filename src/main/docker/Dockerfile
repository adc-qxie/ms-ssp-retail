FROM java:8
VOLUME /tmp
EXPOSE 9001
ADD ms-ssp-retail.jar app.jar
ENV SPRING_PROFILES_ACTIVE="dev,docker"
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java","-Xmx512m", "-Djava.security.egd=file:/dev/./urandom", "-jar","/app.jar"]
