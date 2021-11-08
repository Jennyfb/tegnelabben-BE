# tegnelabben-BE
Backend of Tegnelabben


This project requires java jdk 11 and maven.

Clone the repository.
```
git clone hhttps://github.com/Jennyfb/tegnelabben-BE.git
```
Add a the file application.properties under src/main/resources. The file should contail the following:
```
server.port=8081
server.error.include-message=always

spring.datasource.url=jdbc:mysql://tegnelabben-db.cficpykqobx6.eu-west-1.rds.amazonaws.com:3306/tegnelabben
spring.datasource.username=<username>
spring.datasource.password=<password>
```

Recomend using IDE to tun API.
