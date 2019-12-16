# Payconiq Stocks Application

This a small RESTful Stocks application with next features.  
All changes are kept in-memory (with H2 database).  
Application have some initial not-empty stocks, which will be same each time after application restart.

## Features
Supports next REST operations:
 - *GET /api/stocks* - To get information all application stocks.  
 Information does not contain price history
 - *GET /api/stocks/{id}* - To get information about particular stock by id
 - *GET /api/stocks/{id}/history* - To get price history for particular stock
 - *PUT /api/stocks/{id}* - To update particular stock price.  
 Price History will be updated too
 - *POST /api/stocks* - Add new Stock to application

## How to build and start server
*Note*: Java 11 is required  

Command to build with maven:  
> mvn clean install 

Or if you want to skip tests:  
> mvn clean install -DskipTests

Command to start after build (on port 8080):  
> java -jar target/payconiq-assignment-1.0.jar

To start server on a specific port:
> java -jar target/payconiq-assignment-1.0.jar --server.port=8081

## Front End
Web server is started by request after application startup.
To open it, please follow (port may be different):
> localhost:8080

It contains small API which allows to execute each REST request from the list upper.  

*Note*: Web was tested only on Google Chrome browser, so, use please it  
if any issues will appear with other browser.
