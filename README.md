# tegnelabben-BE
Backend of Tegnelabben


This project requires java jdk 11 and maven.

Clone the repository.
```
git clone https://github.com/Jennyfb/tegnelabben-BE.git
```
Add a the file application.properties under src/main/resources. The file should contail the following:
```
server.port=8081
server.error.include-message=always

spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5Dialect
spring.jpa.hibernate.ddl-auto=update

spring.datasource.url=jdbc:mysql://tegnelabben-db.cficpykqobx6.eu-west-1.rds.amazonaws.com:3306/Tegnelabben
spring.datasource.username=<username>
spring.datasource.password=<password>
```

Install dependencies and run api:
```
mvn install
mvn exec:java -Dexec.mainClass="com.example.tegnelabben.TegnelabbenApplication"
```
Or use ide to run api.

