<br />
<div align="center">
<h3 align="center">Franchise-MNGR</h3>
  <p align="center">
    Manager to administrate API franchises with AWS.
  </p>
</div>

### Built With

* ![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=java&logoColor=white)
* ![Spring](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
* ![Gradle](https://img.shields.io/badge/Gradle-02303A.svg?style=for-the-badge&logo=Gradle&logoColor=white)
* ![Postgres](https://img.shields.io/badge/PostGres-08B4CA?style=for-the-badge&logo=postgresql&logoColor=white)


<!-- GETTING STARTED -->
## Getting Started

To get a local copy up and running follow these steps.

### Prerequisites

* JDK 21 [https://jdk.java.net/java-se-ri/21](https://jdk.java.net/java-se-ri/11)
* Gradle [https://gradle.org/install/](https://gradle.org/install/)
* Postgres [https://www.postgresql.org/download/](https://www.postgresql.org/download/)

### Recommended Tools
* IntelliJ Community [https://www.jetbrains.com/idea/download/](https://www.jetbrains.com/idea/download/)
* Postman [https://www.postman.com/downloads/](https://www.postman.com/downloads/)

### Installation

1. Clone the repo
2. Change directory
   ```sh
   cd nf-franchise-mngr
   cd app
   ```
3. Create a new database in PostgreSQL, schema and table, the SQL query is in 
   ```yml
   # app/DB_QUERY.SQL
   ```
4. Update the database connection settings or create this environment variables 
   ```yml
   # app/applications/app-service/src/main/resources/application.yaml  
   adapters:
     r2dbc:
        host: ${DB_HOST}
        port: ${DB_PORT}
        database: ${DB_DATABASE}
        schema: ${DB_SCHEMA}
        username: ${DB_USERNAME}
        password: ${DB_USER_PASSWORD}
   ```
## Build

1. exec comand to create a jar file 
   ```sh
   #app/applications/app-service/build/libs/franchises.jar
      
      gradle clean build -x test
   
   ```

<!-- USAGE -->
## Usage

1. Right-click the class MainApplication and choose Run Application
2. Open [http://localhost:8080/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html) in your web browser

