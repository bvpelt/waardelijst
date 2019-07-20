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

## liquibase 
- documentation https://www.liquibase.org/documentation/index.html
- properties https://javadeveloperzone.com/spring-boot/spring-boot-liquibase-example/

## h2 standalong
Start java -jar ~/.m2/repository/com/h2database/h2/1.4.199/h2-1.4.199.jar
which gives access to webconsole

- runtime webconsole https://springframework.guru/using-the-h2-database-console-in-spring-boot-with-spring-security/

## version information
https://repository.sonatype.org/#welcome