# Waardelijsten

# Start app
The default profile is test which uses a h2 database
```
$ mvn spring-boot:run 
```
# Building
When all sources are checked in (no local changes)
```
mvn clean package
```
As an alternative without checking for local changes
``` 
mvn -Dmaven.buildNumber.doCheck=false clean package 
```

# References

## Active profile
- http://dolszewski.com/spring/spring-boot-properties-per-maven-profile/

## Authentication
- https://www.ekiras.com/2016/04/authenticate-user-with-custom-user-details-service-in-spring-security.html

## Caching
- documentation https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-caching.html
- multiple keys https://stackoverflow.com/questions/14072380/cacheable-key-on-multiple-method-arguments
- https://www.keycdn.com/blog/http-cache-headers
- https://www.baeldung.com/spring-security-cache-control-headers

## Custom queries
- Extending repository https://dzone.com/articles/add-custom-functionality-to-a-spring-data-reposito

## h2 standalong
Start java -jar ~/.m2/repository/com/h2database/h2/1.4.199/h2-1.4.199.jar
which gives access to webconsole

- runtime webconsole https://springframework.guru/using-the-h2-database-console-in-spring-boot-with-spring-security/

## Liquibase 
- documentation https://www.liquibase.org/documentation/index.html
- properties https://javadeveloperzone.com/spring-boot/spring-boot-liquibase-example/

## Rest
- http://restcookbook.com/

## Testing
- https://restlet.com/documentation/client/user-guide/test/make-your-requests-and-assertions-dynamic/functions

## Version information
https://repository.sonatype.org/#welcome
