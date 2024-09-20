# Spring Boot API
[![Docker Pulls](https://badgen.net/docker/pulls/guibitencurt/spring-boot-api-ci)](https://hub.docker.com/r/guibitencurt/spring-boot-api-ci)
[![Java Build and Publish a Docker Image](https://github.com/Guilhermebit/spring-boot-rest-api/actions/workflows/ci.yml/badge.svg)](https://github.com/Guilhermebit/spring-boot-rest-api/actions/workflows/ci.yml)
[![licence mit](https://img.shields.io/badge/licence-MIT-blue.svg)](https://github.com/Guilhermebit/spring-boot-rest-api/blob/master/LICENSE)

Welcome to my Spring Boot app!

This project was developed to demonstrate how to build an API using Spring Boot. It has table relationships in the database and comprehensive unit and integration testing.

## This project has the following architecture: 

![spring_boot_api_diagram](https://github.com/user-attachments/assets/b4a65d4e-fdb0-4c01-9fcd-0c4bed99af42)

## Tecnologies
- ✅ Java 17
- ✅ SpringBoot
- ✅ Spring MVC
- ✅ Spring Data JPA
- ✅ SpringSecurity
- ✅ PostgreSQL
- ✅ JWT
- ✅ Docker
- ✅ JUnit 5 + Mockito(unit tests) and MockMvc(integration tests)
## Practices Adopted
- SOLID
- Containerization with Docker
- Dependency Injection
- Queries with SpringData JPA
- Error Handling
## :rocket: Installation
1. Clone the repository:
```
$ git clone https://github.com/Guilhermebit/spring-boot-rest-api.git
```
2. Install dependencies with Maven

3. Install docker: https://docs.docker.com/engine/install/

4. Run the following command:
```
./mvnw clean install
```
## Usage
1. Run the following command:
```
docker-compose up 
```
2. The API will be accessible at http://localhost:8081

## ⚙️ Build Script  
This project includes a [Python script](./build.py) that automates the application build process.  

Here you can download Python: https://www.python.org/downloads/  

**To run the script, navigate to the project directory and execute:**  

+ On Windows:  
```
py build.py
```
+ On Unix-based systems:  
```
python build.py
```

The script performs the following steps:  

+ Compiling an application: Use Maven Wrapper to compile an application with the correct command for the operating system:  
 
  + **Windows:** ```mvnw.cmd clean package```
  + **Unix systems (Linux/Mac):** ```./mvnw clean package```
 
+ Stop and remove all Docker containers: Run ```docker-compose down``` to ensure all containers are stopped and removed.  
+ Building and starting Docker containers: Use ```docker-compose up --build -d``` to build Docker images and start containers in the background.  

The script was designed to simplify the process of building, stopping and restarting containers, saving time and making the process faster.  

# Api EndPoints
To test the HTTP requests below, the Postman tool was used.  
If you choose to use `Postman`, you can download the `Collection` by clicking [here](https://github.com/Guilhermebit/spring-boot-rest-api/blob/master/Contents/Spring%20Boot%20API.postman_collection.json), and import it into your Postman.  
Here you can download Postman: https://www.postman.com/downloads/
+ The user must have the **`TOKEN`** and an **`ADMIN`** role to access the routes: 
    + `POST /product`
    + `PUT /product/{id}`
    + `DELETE /product/{id}`
## Register a new user 
`POST /auth/register`
+ Request (application/json)

    + Body
 
       ```json
       {
           "login": "User1",
           "password": 1234,
           "role": "USER"
       }
       ```

+ Response 201 (application/json)
    + Body
  
      ```json
      {
           "data": "null",
           "message": "Your registration was successful",
           "status": 201
      }
      ```
## Login 
`POST /auth/login`
+ Request (application/json)
    + Body
 
       ```json
       {
           "login": "User1",
           "password": 1234,
       }
       ```
       
+ Response 200 (application/json)
    + Body
  
      ```json
      {
          "data": [
          {
                  "token": "access_token",
          }
          ],
           "message": "access_token",
           "status": 200
      }
      ```
## Insert a new product 
`POST /product`
+ Request (application/json)
    + Headers
      
         Authorization: Bearer [access_token]

    + Body
 
       ```json
       {
           "name": "t-shirt",
           "price_in_cents": 5000
       }
       ```
      
+ Response 201 (application/json)
    + Body
  
      ```json
      {
          "data": [
          {
                  "id": "c2fc6ab7-cdf1-46ee-be87-804df6be6731",
                  "name": "t-shirt",
                  "price_in_cents": 5000
          }
          ],
           "message": "",
           "status": 201
      }
      ```
## Get all products 
`GET /product`
+ Request (application/json)
    + Headers
      
         Authorization: Bearer [access_token]
      
+ Response 200 (application/json)
    + Body
  
      ```json
      {
          "data": [
          {
                  "id": "c2fc6ab7-cdf1-46ee-be87-804df6be6731",
                  "name": "t-shirt",
                  "price_in_cents": 5000
          }
          ],
           "message": "",
           "status": 200
      }
      ```
## Get one product 
`GET /product/{id}`
+ Request (application/json)
    + Headers
      
         Authorization: Bearer [access_token]

+ Response 200 (application/json)
    + Body
  
      ```json
      {
          "data": [
          {
                  "id": "c2fc6ab7-cdf1-46ee-be87-804df6be6731",
                  "name": "t-shirt",
                  "price_in_cents": 5000
          }
          ],
           "message": "",
           "status": 200
      }
      ```
## Get the product between a range of values 
`GET /product/value/{3000}/{5000}`
+ Request (application/json)
    + Headers
      
         Authorization: Bearer [access_token]

+ Response 200 (application/json)
    + Body
  
      ```json
      {
          "data": [
          {
                  "id": "c2fc6ab7-cdf1-46ee-be87-804df6be6731",
                  "name": "t-shirt",
                  "price_in_cents": 5000
          }
          ],
           "message": "",
           "status": 200
      }
      ```
## Update a product
`PUT /product/{id}`
+ Request (application/json)
    + Headers
      
         Authorization: Bearer [access_token]

    + Body
 
       ```json
       {
           "name": "t-shirt blue",
           "price_in_cents": 3000
       }
       ```

+ Response 200 (application/json)
    + Body
  
      ```json
      {
          "data": [
          {
                  "id": "c2fc6ab7-cdf1-46ee-be87-804df6be6731",
                  "name": "t-shirt blue",
                  "price_in_cents": 3000
          }
          ],
           "message": "",
           "status": 200
      }
      ```
## Delete a product
`DELETE /product/{id}` 
- OBS: The data is not deleted directly, instead its status is changed to "false" 
+ Request (application/json)
    + Headers
      
         Authorization: Bearer [access_token]

+ Response 204 (application/json)
    + Body
  
      ```json
      {
          "data": null,
          "message": "Product successfully deleted.",
          "status": 204
      }
      ```
